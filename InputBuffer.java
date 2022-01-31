import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.io.RandomAccessFile;

/**
 * Buffer class. Holds a byte array as data and several methods to process a
 * binary file.
 * 
 * @author Taimoor Qamar
 * @version 2019.10.23
 */
public class InputBuffer {
    private byte[] byteArr; // The underlying byte array for the byte buffer
    public static final int BLOCK_SIZE = 16384; // Block size in bytes, may
                                                // change to be a function of
                                                // fileSize later.
    public static final int RECORD_SIZE = 16; // Size of one record in bytes,
                                              // may change to be a function of
                                              // fileSize later.
    public static final int MAX_BLOCKS = 8;
    public static final int HEAP_SIZE = BLOCK_SIZE * MAX_BLOCKS; // Initial Heap
                                                                 // size.
    private int prevBlock; // Previously loaded block number.
    private ByteBuffer byteBuffer; // Used to wrap our byteArray
    private RandomAccessFile r; // Input stream from input file
    private long fileSize; // File size of the input file.


    // -------------------------------------------------------------------------
    /**
     * Constructor for Input Buffer.
     * 
     * @param the
     *            fileName of the binary file to read from.
     * 
     */
    public InputBuffer(String fileName) {
        prevBlock = 0;
        try {
            r = new RandomAccessFile(new File(fileName), "rw");
            fileSize = r.length();
        }
        catch (FileNotFoundException e) {
            System.out.print("File Not Found!");
        }
        catch (IOException e) {
            System.out.print("File Not Found!");
        }

    }


    // -------------------------------------------------------------------------
    /**
     * Fill the buffer with 1 (or remainder of final) block from the disk.
     * 
     * @throws IOException
     */
    public void fillBuffer() {
        if (endOfFile()) {
            System.out.print("At End of File! \n");
            return;
        }
        try {
            r.seek(prevBlock * BLOCK_SIZE); // move the file pointer
            // If the remaining bytes are less than one block
            if (fileSize - r.getFilePointer() < BLOCK_SIZE) {
                byteArr = new byte[(int)(fileSize - r.getFilePointer())];
                r.read(byteArr);
                byteBuffer = ByteBuffer.wrap(byteArr);
                prevBlock++;
            }
            else {
                byteArr = new byte[BLOCK_SIZE];
                r.read(byteArr);
                byteBuffer = ByteBuffer.wrap(byteArr);
                prevBlock++;
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Binary File Not Found!");
        }
        catch (IOException e) {
            System.out.println("File Read Failure!");
        }
    }


    // -------------------------------------------------------------------------
    /**
     * Returns an 8 block sized array of Nodes of Records.
     * 
     * @return array of Nodes containing records.
     * @throws IOException
     */
    public Node<Double, Long>[] initialLoad() throws IOException {
        int numRecords = 0;
        @SuppressWarnings("unchecked")
        Node<Double, Long>[] ret = (Node<Double, Long>[])new Node[HEAP_SIZE
            / RECORD_SIZE];

        while (numRecords < HEAP_SIZE / RECORD_SIZE) {
            if (!hasInput()) {
                fillBuffer();
            }
            ret[numRecords] = nextInput();
            numRecords++;
        }
        return ret;
    }


    // -------------------------------------------------------------------------
    /**
     * Returns an 8 block sized 2d array of binary data.
     * 
     * @return array of Nodes containing records.
     * @throws IOException
     */
    public byte[][] initialBinaryLoad() throws IOException {
        int numRecords = 0;
        byte[][] ret = new byte[HEAP_SIZE / RECORD_SIZE][RECORD_SIZE];

        while (numRecords < HEAP_SIZE / RECORD_SIZE) {
            if (!hasInput()) {
                fillBuffer();
            }
            ret[numRecords] = nextBinaryInput();
            numRecords++;
        }
        return ret;
    }


    // -------------------------------------------------------------------------
    /**
     * Initial load method for the multiway merge; Returns a maximum 8 run, 1
     * block sized 2d array of nodes containing records. If the number of runs
     * in the runfile (N) is less than 8, returns a 2d array of N runs
     * containing 1 block each.
     * 
     * @param mwwIndex
     *            Index of runs and blocks contained in the runfile.
     * @param block
     *            The specific block to load for each run.
     * @return 2d array of nodes containing records from runfile.
     * 
     */
    public Node<Double, Long>[][] mwmLoad(DLList<Run> runsTracker) {
        if (runsTracker == null) {
            System.out.print("runsTracker passed is null! \n");
        }
        int totalRecords = 0;
        int blockRecords = 0;
        int numRuns = MAX_BLOCKS;
        int currentRun = -1;
        if (runsTracker.size() <= MAX_BLOCKS) {
            numRuns = runsTracker.size();
        }
        @SuppressWarnings("unchecked")
        Node<Double, Long>[][] ret =
            (Node<Double, Long>[][])new Node[numRuns][BLOCK_SIZE / RECORD_SIZE];

        while (totalRecords < (numRuns * BLOCK_SIZE) / RECORD_SIZE) {
            if (!hasInput()) {
                blockRecords = 0;
                currentRun++;
                mwmFillBuffer(runsTracker.get(currentRun).getStart(),
                    runsTracker.get(currentRun).getEnd());
                runsTracker.get(currentRun).setStart(runsTracker.get(currentRun)
                    .getStart() + BLOCK_SIZE);
            }
            try {
                ret[currentRun][blockRecords++] = nextInput();
            }
            catch (IOException e) {
                System.out.print("File Read Error!");
            }
            totalRecords++;
        }
        return ret;
    }


    // --------------------------------------------------------------------------
    /**
     * Initial binary load method for the multiway merge; Returns a maximum 8
     * run, 1 block sized 2d array of bytes. If the number of runs
     * in the runfile (N) is less than 8, returns a 2d array of N runs
     * containing 1 block each.
     * 
     * @param runsTracker
     * @return
     */
    public byte[][] mwmBinaryLoad(DLList<Run> runsTracker) {
        if (runsTracker == null) {
            System.out.print("runsTracker passed is null! \n");
        }
        int numRuns = MAX_BLOCKS;
        if (runsTracker.size() <= MAX_BLOCKS) {
            numRuns = runsTracker.size();
        }
        byte[][] ret = new byte[numRuns][BLOCK_SIZE]; // should reuse the memory

        for (int i = 0; i < numRuns; i++) {
            if (!hasInput()) {
                mwmFillBuffer(runsTracker.get(i).getStart(), runsTracker.get(i)
                    .getEnd());
                runsTracker.get(i).setStart(runsTracker.get(i).getStart()
                    + BLOCK_SIZE);
                ret[i] = byteBuffer.array();
                // need to manually change position since not using nextInput
                // call to return data.
                byteBuffer.position(byteArr.length);
            }
        }
        return ret;
    }


    // -------------------------------------------------------------------------
    /**
     * Gets one block of data in the form of an array of nodes containing
     * records from the runfile at the position passed in the parameter.
     * 
     * @param blockPosition
     * @return one block array of nodes
     */
    public Node<Double, Long>[] mwmGetBlock(DLList<Run> runsTracker, int run) {
        if (runsTracker == null || runsTracker.get(run).isComplete()) {
            System.out.print("runsTracker List is null or run: " + run
                + " has been exhausted! \n");
            return null;
        }
        @SuppressWarnings("unchecked")
        Node<Double, Long>[] ret = (Node<Double, Long>[])new Node[BLOCK_SIZE
            / RECORD_SIZE];
        int totalRecords = 0;
        while (totalRecords < (BLOCK_SIZE) / RECORD_SIZE) {
            if (!hasInput()) {
                mwmFillBuffer(runsTracker.get(run).getStart(), runsTracker.get(
                    run).getEnd());
                runsTracker.get(run).setStart(runsTracker.get(run).getStart()
                    + BLOCK_SIZE);
            }
            try {
                ret[totalRecords] = nextInput();
            }
            catch (IOException e) {
                System.out.print("File Read Error!");
            }
            totalRecords++;
        }
        return ret;
    }


    // -------------------------------------------------------------------------
    /**
     * Gets one block of data in the form of an array of bytes
     * from the runfile at the position passed in the parameter.
     * 
     * @param blockPosition
     * @return one block array of nodes
     */
    public byte[] mwmGetBinaryBlock(DLList<Run> runsTracker, int run) {

        if (runsTracker == null || runsTracker.get(run).isComplete()) {
//            System.out.print("runsTracker List is null or run: " + (run + 1)
//                + " has been exhausted! \n");
            return null;
        }
        if (!hasInput()) {
            mwmFillBuffer(runsTracker.get(run).getStart(), runsTracker.get(run)
                .getEnd());
            runsTracker.get(run).setStart(runsTracker.get(run).getStart()
                + BLOCK_SIZE);
        }
        byte[] ret = byteBuffer.array();
        // need to manually change position since not using nextInput
        // call to return data.
        byteBuffer.position(byteArr.length);

        return ret;

    }


    // -------------------------------------------------------------------------
    /**
     * Private helper method for the mwmInitiloadLoad method.
     * 
     * @param runFilePosition
     */
    private void mwmFillBuffer(int runCurrentPosition, int runEnd) {
        try {
            r.seek(runCurrentPosition); // move the file pointer
        }
        catch (IOException e1) {
            System.out.print("File read error!");
        }
        if (endOfFile()) {
            System.out.print("At End of File! \n");
            return;
        }
        try {
            // If the remaining bytes are less than one block
            if (((runEnd - runCurrentPosition) + 1) < BLOCK_SIZE) {
                byteArr = new byte[(runEnd - runCurrentPosition)];
                r.read(byteArr);
                byteBuffer = ByteBuffer.wrap(byteArr);
            }
            else {
                byteArr = new byte[BLOCK_SIZE];
                r.read(byteArr);
                byteBuffer = ByteBuffer.wrap(byteArr);
            }
        }
        catch (FileNotFoundException e) {
            System.out.println("Binary File Not Found!");
        }
        catch (IOException e) {
            System.out.println("File Read Failure!");
        }
    }


    // -------------------------------------------------------------------------
    /**
     * Checks if byteArrPosition is still less than the total BLOCK_SIZE (and
     * thus, not at the end of the byteArr yet).
     * 
     * @return true or false
     */
    public boolean hasInput() {
        if (byteBuffer == null) {
            return false;
        }
        return byteBuffer.position() != byteArr.length;

    }


    // -------------------------------------------------------------------------
    /**
     * Returns the next record in byteArray in Node format.
     * 
     * @return the next record
     * @throws IOException
     */
    public Node<Double, Long> nextInput() throws IOException {
        if (!hasInput()) {
            fillBuffer();
        }
        Long pid = byteBuffer.getLong();
        Double aScore = byteBuffer.getDouble();
        Node<Double, Long> ret = new Node<Double, Long>(aScore, pid);
        return ret;
    }


    // -------------------------------------------------------------------------
    /**
     * Returns the next record in byteArray in Binary format.
     * 
     * @return the next record
     * 
     */
    public byte[] nextBinaryInput() {
        if (!hasInput()) {
            fillBuffer();
        }
        byte[] ret = new byte[16];
        byteBuffer.get(ret, 0, 16);
        return ret;
    }


    // -------------------------------------------------------------------------
    /**
     * Resets the buffer and closes any appropriate input stream.
     */
    public void clear() {
        byteArr = null;
        byteBuffer = null;
        prevBlock = 0;
        if (r != null) {
            try {
                r.close();
                r = null;
            }
            catch (IOException e) {
                System.out.print("Input file failure!");
            }
        }
    }


    // -------------------------------------------------------------------------
    /**
     * Returns the current position of the buffer.
     * 
     * @return buffer position.
     */
    public int getPosition() {
        return byteBuffer.position();
    }


    // -------------------------------------------------------------------------
    /**
     * Returns a boolean indicating whether filePointer is at end of file.
     * 
     * @return filePointer == fileSize
     */
    public boolean endOfFile() {
        try {
            return r.getFilePointer() == fileSize;
        }
        catch (IOException e) {
            System.out.print("File I/O Error!");
            return false;
        }
    }


    // -------------------------------------------------------------------------
    /**
     * Method that returns the fileSize of file passed to inputBuffer.
     * 
     * @return fileSize
     */
    public long getFileSize() {
        return fileSize;
    }

}
