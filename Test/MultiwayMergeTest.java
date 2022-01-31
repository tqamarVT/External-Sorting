import java.io.IOException;

/**
 * @author Taimoor Qamar
 * @version 10-23-19
 */

public class MultiwayMergeTest extends student.TestCase {
    // FIELDS
    private MultiwayMerge testMM;
    private DLList<Run> testRunsTracker;
    private DLList<Run> testRunsTracker2;
    private FileGenerator mmTestGenerator;
    private ExternalSorter testES;
    private final static int RUN_SIZE = 131072;


    public void setUp() throws IOException {
        mmTestGenerator = new FileGenerator();
        mmTestGenerator.generateSortedFiles(new String[] { "MWM 64 Block File",
            "64" });
        testES = new ExternalSorter("Unsorted MWM 64 Block File");
        testRunsTracker = new DLList<Run>();
        for (int i = 0; i < 8; i++) {
            testRunsTracker.add(new Run(i + 1, i * RUN_SIZE, (i + 1)
                * RUN_SIZE));
        }
// testMM = new MultiwayMerge("Unsorted MWM 64 Block File",
// testRunsTracker);
    }


//    public void testRandom() throws IOException {
//        testRunsTracker2 = mmTestGenerator.generateRunFile(new String[] {
//            "MWM 128 Block File Runfile", "128" });
//        testMM = new MultiwayMerge("MWM 128 Block File Runfile",
//            testRunsTracker2);
//        testMM.binaryMerge();
//
//    }

}
