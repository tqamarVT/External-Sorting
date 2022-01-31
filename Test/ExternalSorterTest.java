import java.io.IOException;

/**
 * Test class for External Sorter.
 */

/**
 * @author Taimoor Qamar
 * @version 2019.10.27
 *
 */
public class ExternalSorterTest extends student.TestCase {
    // FIELDS
    FileGenerator testFileGen;
    InputBuffer testInputBuffer;
    OutputBuffer testOutputBuffer;
    ExternalSorter testSorter;


    // --------------------------------------------------------------------------------
    /**
     * Initializes test-cases for the Prj3Heap class.
     * 
     * @throws IOException
     */
    public void setUp() throws IOException {
        testFileGen = new FileGenerator();
    }


    // --------------------------------------------------------------------------------
// /**
// * Tests the replacementSelection method for all conditions.
// * @throws IOException
// */
// public void testReplacementSelection() throws IOException {
// testFileGen.generateSortedFiles(new String[] {"External Sorter 128 Block
// Test", "128" });
// testSorter = new ExternalSorter("Unsorted External Sorter 128 Block Test");
// // testSorter.multiMerge();
// }

    public void testEverything() throws IOException {
        testFileGen.generateSortedFiles(new String[] {
            "External Sorter 128 Block Test", "256" });
        testSorter = new ExternalSorter(
            "Unsorted External Sorter 128 Block Test");
        // testSorter.multiMerge();
        String[] args = { "Unsorted External Sorter 128 Block Test", "" };
        Ascoresorting.testMain(args);
    }

}
