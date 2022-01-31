import java.nio.ByteBuffer;

/**
 * @author Taimoor Qamar
 * @version 10-23-19
**/

public class MultiwayMerge {

    // FIELDS
    public static final int BLOCK_SIZE = 16384; // Block size in bytes
    public static final int RECORD_SIZE = 16;
    public static final int NUM_BLOCKS = 8;
    public static final int RUN_SIZE = BLOCK_SIZE * NUM_BLOCKS;
    private InputBuffer in; // input buffer used to pull data
    private OutputBuffer out; // output buffer used to write data
    private DLList<Run> runsTracker; // list containing byte position
                                     // information of runs
    private Node<Double, Long>[][] runArray; // 2d node array containing run
                                             // data used for merging.
    private byte[][] runByteArray; // 2d byte array containing run data used for
                                   // merging.
    ByteBuffer[] binaryArrTracker;
    private int[][] mwwIndex; // index of run byte position (for testing)
    private Printer printer;


    public MultiwayMerge(String fileName, String recordFile, DLList<Run> runs) {
        in = new InputBuffer(fileName);
        out = new OutputBuffer(recordFile);
        runsTracker = new DLList<Run>();
        for (int i = 0; i < runs.size(); i++) {
            runsTracker.add(new Run(runs.get(i).getNumber(), runs.get(i)
                .getStart(), runs.get(i).getEnd()));
        }
    }


// ------------------------------------------------------------------------------------------------
    /**
     * Initial load for the memory pool for multiway merging. Initializes a 2D
     * array containing each run (max first 8 runs if runfile contains more than
     * 8 runs) and the first block from each run.
     */
    private void initialLoad() {
        runArray = in.mwmLoad(runsTracker);
    }


    // ------------------------------------------------------------------------------------------------
    private void initialBinaryLoad() {
        runByteArray = in.mwmBinaryLoad(runsTracker);
        binaryArrTracker = new ByteBuffer[runByteArray.length];
        for (int i = 0; i < binaryArrTracker.length; i++) {
            binaryArrTracker[i] = ByteBuffer.wrap(runByteArray[i]);
            binaryArrTracker[i].mark();
        }
    }

    // ----------------------------------------------------------------------------------------
// /**
// * Merges the runs into one large run.
// */
// public void merge() {
// initialLoad();
// int[] arrTracker = new int[runArray.length];
// double tempMax = -1.0;
// int tempIndex = 0;
// int count = 0;
// Node<Double, Long> tempNode = null;
// // While output file isnt complete
// while (out.getFileSize() != in.getFileSize()) {
// // While the current runs haven't been exhausted
// while (count != runArray.length) {
// // compare 1st record from 1 block in each run
// for (int i = 0; i < runArray.length; i++) {
// if (runArray[i] == null) {
// continue;
// }
// if (runArray[i][arrTracker[i]].getKey() > tempMax) {
// tempMax = runArray[i][arrTracker[i]].getKey();
// tempNode = runArray[i][arrTracker[i]];
// tempIndex = i;
// }
// }
// tempMax = -1.0;
// // insert largest record into buffer.
// out.insertRecord(tempNode);
//
// // update block position in the run we just inserted a record
// // into the output buffer from.
// arrTracker[tempIndex]++;
// if (arrTracker[tempIndex] == runArray[tempIndex].length) {
// runArray[tempIndex] = in.mwmGetBlock(runsTracker,
// tempIndex);
// // if the run is completely exhausted, updated the runs
// // exhausted count.
// if (runArray[tempIndex] == null) {
// count++;
// }
// arrTracker[tempIndex] = 0;
// }
// }
// // when the number of currently held runs is exhausted, check if
// // runsArray needs reloading.
// count = reload();
// }
// out.clear();
// Printer printer = new Printer("Final Sorted File");
// printer.print();
// }


    // ----------------------------------------------------------------------------------------
    /**
     * Merges the runs into one large run.
     * 
     * @param oldRuns
     *            the list of runs before the merge
     * @return the list of runs after merging (should be 1/8th the size)
     */
    public DLList<Run> binaryMerge(DLList<Run> oldRuns) {
        DLList<Run> result = new DLList<Run>();
        int currentOffset = 0;
        int runNumber = 1;
        runsTracker = oldRuns;
        initialBinaryLoad();
        double tempMax = -1.0;
        int tempIndex = 0;
        int count = 0;
        byte[] tempRecord = new byte[16];
        // While output file isnt complete
        while (out.getFileSize() != in.getFileSize()) {
            // While the current runs haven't been exhausted
            while (count != runByteArray.length) {
                // compare 1st record from 1 block in each run
                for (int i = 0; i < runByteArray.length; i++) {
                    if (runByteArray[i] == null
                        || binaryArrTracker[i] == null) {
                        continue;
                    }
                    long tempPID = binaryArrTracker[i].getLong();
                    double currentMax = binaryArrTracker[i].getDouble();

                    if (currentMax > tempMax) {
                        tempMax = currentMax;
                        tempIndex = i;
                    }
                    binaryArrTracker[i].reset();
                }
                tempMax = -1.0;
                binaryArrTracker[tempIndex].get(tempRecord);
                binaryArrTracker[tempIndex].mark();
                // insert largest record into buffer.
                out.insertBinaryRecord(tempRecord);
                // update block position in the run we just inserted a record
                // into the output buffer from.
                if (!binaryArrTracker[tempIndex].hasRemaining()) {
                    runByteArray[tempIndex] = in.mwmGetBinaryBlock(runsTracker,
                        tempIndex);
                    // if the run is completely exhausted, updated the runs
                    // exhausted count.
                    if (runByteArray[tempIndex] == null) {
                        count++;
                    }
                    else {
                        binaryArrTracker[tempIndex] = ByteBuffer.wrap(
                            runByteArray[tempIndex]);
                        binaryArrTracker[tempIndex].mark();
                    }
                }
            }
            // end of run
            out.flush(); // to ensure that the out.getPosition is accurate
            if (currentOffset < out.getPosition()) {
                Run currentRun = new Run(runNumber, currentOffset, (int)out
                    .getPosition());
                currentOffset = (int)out.getPosition();
                result.add(currentRun);
                runNumber++;
            }
            // when the number of currently held runs is exhausted, check if
            // runsArray needs reloading.
            count = binaryReload();
        }
        in.clear();
        out.clear();
        return result;
    }


// -------------------------------------------------------------------------------------
    /**
     * Private method to help reload the runstracker and runsArray in the event
     * that the number of runs exceeds 8.
     * 
     * @return
     */
    private int reload() {
        if (out.getFileSize() == in.getFileSize()) {
            return 0;
        }
        DLList<Run> newRunsTracker = new DLList<Run>();
        for (int i = 0; i < runsTracker.size(); i++) {
            if (runsTracker.get(i).isComplete()) {
                continue;
            }
            newRunsTracker.add(new Run(runsTracker.get(i).getNumber(),
                runsTracker.get(i).getStart(), runsTracker.get(i).getEnd()));
        }
        runsTracker = newRunsTracker;
        initialLoad();
        return 0;
    }


    // -------------------------------------------------------------------------------------
    /**
     * Private method (for binary array) to help reload the runstracker and
     * runsArray in the event that the number of runs exceeds 8.
     * 
     * @return
     */
    private int binaryReload() {
        if (out.getFileSize() == in.getFileSize()) {
            return 0;
        }
        DLList<Run> newRunsTracker = new DLList<Run>();
        for (int i = 0; i < runsTracker.size(); i++) {
            if (runsTracker.get(i).isComplete()) {
                continue;
            }
            newRunsTracker.add(new Run(runsTracker.get(i).getNumber(),
                runsTracker.get(i).getStart(), runsTracker.get(i).getEnd()));
        }
        runsTracker = newRunsTracker;
        initialBinaryLoad();
        return 0;
    }

}
