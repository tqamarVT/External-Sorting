import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.File;

/**
 * @author Taimoor Qamar
 * @version 10-23-19
 */
public class Ascoresorting {
  
    public static void main(String[] args) {
        String recordFile = args[0];
        String studentFile = args[1];
        String runFile = "peter8runs.bin";
        new File(runFile).delete(); // might remove later
        InputBuffer in = new InputBuffer(recordFile);
        OutputBuffer out = new OutputBuffer(runFile);
        byte[][] memory = null;
        try {
            memory = in.initialBinaryLoad();
        }
        catch (IOException e) {
            System.out.print("File Read Error!");
        }
        ExternalSorter replacementSelection = new ExternalSorter();
        DLList<Run> newRunTracker = replacementSelection.replacementSelection(
            in, out, memory);
        new File(recordFile).delete(); // might remove later
        if (newRunTracker.size() == 1) { // saves some time
            new File(runFile).renameTo(new File(recordFile));
        }
        int numPasses = 0; // hope it doesn't overflow :)
        // while there are multiple runs:
        while (newRunTracker.size() > 1) {
            MultiwayMerge mwm = new MultiwayMerge(runFile, recordFile,
                newRunTracker);
            newRunTracker = mwm.binaryMerge(newRunTracker);
            mwm = null;
            numPasses++;
            if (newRunTracker.size() > 1) {
                try {
                    Files.delete(Paths.get(runFile));
                } // make space for second pass
                catch (IOException e) {
                    e.printStackTrace();
                }
                mwm = new MultiwayMerge(recordFile, runFile, newRunTracker);
                newRunTracker = mwm.binaryMerge(newRunTracker);
                mwm = null;
                numPasses++;
                try {
                    Files.delete(Paths.get(recordFile));
                } // make space for third pass or for end
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (numPasses % 2 == 0) {
            new File(runFile).renameTo(new File(recordFile));
        }
        Printer printer = new Printer(recordFile, studentFile);
        printer.printTop100(printer.findTop100());
    }


    public static void testMain(String[] args) {
        String recordFile = args[0];
        String runFile = "peter8runs.bin";
        new File(runFile).delete(); // might remove later
        InputBuffer in = new InputBuffer(recordFile);
        OutputBuffer out = new OutputBuffer(runFile);
        byte[][] memory = null;
        try {
            memory = in.initialBinaryLoad();
        }
        catch (IOException e) {
            System.out.print("File Read Error!");
        }
        ExternalSorter replacementSelection = new ExternalSorter();
        DLList<Run> newRunTracker = replacementSelection.replacementSelection(
            in, out, memory);
        new File(recordFile).delete(); // might remove later
        if (newRunTracker.size() == 1) { // saves some time
            new File(runFile).renameTo(new File(recordFile));
        }
        int numPasses = 0; // hope it doesn't overflow :)
        // while there are multiple runs:
        while (newRunTracker.size() > 1) {
            MultiwayMerge mwm = new MultiwayMerge(runFile, recordFile,
                newRunTracker);
            newRunTracker = mwm.binaryMerge(newRunTracker);
            mwm = null;
            numPasses++;
            if (newRunTracker.size() > 1) {
                try {
                    Files.delete(Paths.get(runFile));
                } // make space for second pass
                catch (IOException e) {
                    e.printStackTrace();
                }
                mwm = new MultiwayMerge(recordFile, runFile, newRunTracker);
                newRunTracker = mwm.binaryMerge(newRunTracker);
                mwm = null;
                numPasses++;
                try {
                    Files.delete(Paths.get(recordFile));
                } // make space for third pass or for end
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        if (numPasses % 2 == 0) {
            new File(runFile).renameTo(new File(recordFile));
        }
    }
}
