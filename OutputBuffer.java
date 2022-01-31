import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;

/**
 * Buffer class. Holds a byte array as data and a boolean flag that changes if
 * any of the data in the array is altered.
 * 
 * @author Taimoor Qamar
 * @version 2019.10.23
 */
public class OutputBuffer {
    private byte[] byteArr; // The underlying byte array for the byte buffer
    private static final int BLOCK_SIZE = 16384; // Block size in bytes, may
                                                 // change to be a function of
                                                 // fileSize later.
    private ByteBuffer byteBuffer; // Used to wrap our byteArray
    private RandomAccessFile r; // Input stream from input file
    private String fileName; // name of file.


    // -------------------------------------------------------------------------
    /**
     * Constructor for Input Buffer.
     * 
     * @param the
     *            fileName of the binary file to read from.
     * 
     */
    public OutputBuffer(String fileName) {
        try {
            this.fileName = fileName;
            r = new RandomAccessFile(new File(fileName), "rw");
        }
        catch (FileNotFoundException e) {
            System.out.print("File Not Found!");
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
        return byteArr != null;

    }


    // -------------------------------------------------------------------------
    /**
     * Checks if byteArrPosition is still less than the total BLOCK_SIZE (and
     * thus, buffer is not full yet.
     * 
     * @return true or false
     */
    public boolean bufferFull() {
        if (byteBuffer == null) {
            return false;
        }
        return byteBuffer.position() == byteArr.length;

    }


    // -------------------------------------------------------------------------
    /**
     * Converts a student record into bytes and inserts it into the byteArray.
     * 
     * @param record
     */
    public void insertRecord(Node<Double, Long> record) {
        if (record == null) {
            System.out.print("Invalid record!");
        }
        else if (byteArr == null) { // if insert has not been called on output
                                    // buffer yet.
            byteArr = new byte[BLOCK_SIZE];
            byteBuffer = ByteBuffer.wrap(byteArr);
            byteBuffer.putLong(record.getValue());
            byteBuffer.putDouble(record.getKey());

        }
        else {
            byteBuffer.putLong(record.getValue());
            byteBuffer.putDouble(record.getKey());
            if (bufferFull()) {
                flush();
            }
        }

    }


    // -------------------------------------------------------------------------
    /**
     * Takes a student record in bytes and inserts it into the byteArray.
     * 
     * @param record
     */
    public void insertBinaryRecord(byte[] record) {
        if (record == null) {
            System.out.print("Invalid record!");
        }
        else if (byteArr == null) { // if insert has not been called on output
                                    // buffer yet.
            byteArr = new byte[BLOCK_SIZE];
            byteBuffer = ByteBuffer.wrap(byteArr);
            byteBuffer.put(record);

        }
        else {
            byteBuffer.put(record);
            if (bufferFull()) {
                flush();
            }
        }

    }


    // -------------------------------------------------------------------------
    /**
     * Resets the buffer and closes any appropriate input or output stream.
     */
    public void clear() {
        byteArr = null;
        byteBuffer = null;
        if (r != null) {
            try {
                r.close();
                r = null;
            }
            catch (IOException e) {
                System.out.print("Output file failure!");
            }
        }
    }


    // -------------------------------------------------------------------------
    /**
     * Flushes all bytes in the buffer to the output file and resets the buffer.
     */
    public void flush() {
        //
        try {
            if(bufferFull()) {
            r.write(byteArr);
            }
            else { //don't want to write a bunch of 0's to run file
                byte[] trimmedByteArr = new byte[byteBuffer.position()];
                for(int i = 0; i < trimmedByteArr.length; i++) {
                    trimmedByteArr[i] = byteArr[i];
                }
                r.write(trimmedByteArr);
            }
            byteArr = new byte[BLOCK_SIZE];
            byteBuffer = ByteBuffer.wrap(byteArr);
        }
        catch (IOException e) {
            System.out.println("File Write Failure!");
        }
    }


    // -------------------------------------------------------------------------
    /**
     * Returns the current position of the file offset.
     * 
     * @return buffer position.
     */
    public long getPosition() {
        try {
            return r.getFilePointer();            
        }
        catch (IOException e) {
            System.out.print("File I/O Error!");
            return 0;
        }
    }


    // -------------------------------------------------------------------------
    /**
     * Method that returns the fileSize at the moment it's invoked.
     * 
     * @return fileSize
     */
    public long getFileSize() {
        try {
            return r.length();
        }
        catch (IOException e) {
            System.out.print("File I/O Error!");
            return 0;
        }
    }


    // ----------------------------------------------------------------------------------
    /**
     * Returns the fileName of file in outputBuffer.
     * 
     * @return filename
     */
    public String getFileName() {
        return fileName;
    }

}
