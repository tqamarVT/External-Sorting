import java.io.IOException;

/**
 * Sorts the data
 * 
 * @author Taimoor Qamar
 * @version 10-23-19
 *
 */
public class ExternalSorter {
    private byte[][] memory;
    String fileName;


    public ExternalSorter() {

    }


    public ExternalSorter(String filename) {
        this.fileName = filename;
    }


    /**
     * Sorts the data in dataFilename into runs in runFilename
     * 
     * @return a Linked List containing important data for each run created.
     */
    public DLList<Run> replacementSelection(
        InputBuffer in,
        OutputBuffer out,
        byte[][] mem) {
        DLList<Run> result = new DLList<>();
        memory = mem;

        // Load first 8 blocks from Input Buffer into memory (guaranteed at
        // least 8):

//        try {
//            memory = in.initialBinaryLoad();
//        }
//        catch (IOException e) {
//            System.out.print("File Read Error!");
//        }
        Prj3Heap heap = new Prj3Heap(Prj3Heap.max);

        int currentOffset = 0;
        int runNumber = 1;
        heap.buildHeap(memory);
        while (!in.endOfFile() || in.hasInput()) {
            heap.buildHeap(memory);
            while (heap.getSize() != 0 && (!in.endOfFile() || in.hasInput())) {
                heap.replace(in, out);
            }
            if (!in.endOfFile()) {
                out.flush(); // to ensure that the out.getPosition is accurate
                Run currentRun = new Run(runNumber, currentOffset, (int)out
                    .getPosition());
                currentOffset = (int)out.getPosition();
                result.add(currentRun);
                runNumber++;
            }
        }
        int numRecordsToPutInNextRun = heap.finishRun(out);
        out.flush(); // to ensure that the out.getPosition is accurate
        if (currentOffset < out.getPosition()) {
            Run currRun = new Run(runNumber, currentOffset, (int)out
                .getPosition());
            currentOffset = (int)out.getPosition();
            result.add(currRun);
            runNumber++;
        }
        for (int i = 0; i < numRecordsToPutInNextRun; i++) {
            out.insertBinaryRecord(heap.remove());
        }
        out.flush(); // to ensure that the out.getPosition is accurate
        if (currentOffset < out.getPosition()) {
            Run currRun = new Run(runNumber, currentOffset, (int)out
                .getPosition());
            currentOffset = (int)out.getPosition();
            result.add(currRun);
            runNumber++;
        }
        out.flush();
        out.clear();
        in.clear();
        return result;
    }


//    public void multiMerge() {
//        InputBuffer in = new InputBuffer(fileName);
//        OutputBuffer out = new OutputBuffer("External Sorter Runfile");
//        DLList<Run> newRunTracker = replacementSelection(in, out);
//        MultiwayMerge mwm = new MultiwayMerge("External Sorter Runfile",
//            newRunTracker);
//        mwm.binaryMerge();
//
//    }

}
