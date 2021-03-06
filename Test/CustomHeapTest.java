import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * @author Taimoor Qamar
 *
 */
public class CustomHeapTest extends student.TestCase {
    // FIELDS
    FileGenerator testFileGen;
    CustomHeap testHeap;
    InputBuffer testInputBuffer;
    OutputBuffer testOutputBuffer;
    byte[][] testByteArr;


    // --------------------------------------------------------------------------------
    /**
     * Initializes test-cases for the CustomHeap class.
     * 
     * @throws IOException
     */
    public void setUp() throws IOException {
        testFileGen = new FileGenerator();
        testFileGen.generateSortedFiles(new String[] { "Prj3 Heap Test-Files",
            "1" });
        testInputBuffer = new InputBuffer("Unsorted Prj3 Heap Test-Files");
        testOutputBuffer = new OutputBuffer(
            "Sorted Prj3 Heap Test-Files Output");
        testHeap = new CustomHeap();
    }


    /**
     * Tests the insert method of the prj3Heap class
     */
    public void testInsert() {
        // CASE 1: FILL THE HEAP WITH ONE BLOCK
        assertEquals(testHeap.getSize(), 0);
        testInputBuffer.fillBuffer();
        while (testInputBuffer.hasInput()) {
            testHeap.insert(testInputBuffer.nextBinaryInput());
        }
        assertEquals(testHeap.getSize(), 1024);
    }


    /**
     * Tests the insert method of the prj3Heap class
     */
    public void testRemove() {
        // CASE 1: FILL THE HEAP WITH ONE BLOCK, THEN REMOVE
        assertEquals(testHeap.getSize(), 0);
        testInputBuffer.fillBuffer();
        while (testInputBuffer.hasInput()) {
            testHeap.insert(testInputBuffer.nextBinaryInput());
        }
        assertEquals(testHeap.getSize(), 1024);

        // SINCE WE HAVE GENERATED A SORTED (MAX TO MIN) FILE FOR THE SAME FILE
        // testInputBuffer READ, READ IT VIA A BUFFER AND ASSERT THAT THE HEAP
        // MAX IS EQUAL TO FIRST VALUE (AND THUS THE MAX) FROM THAT FILE FOR ALL
        // VALUES.
        InputBuffer testInputBuffer2 = new InputBuffer(
            "Sorted Prj3 Heap Test-Files");
        testInputBuffer2.fillBuffer();
        while (testInputBuffer2.hasInput()) {
            byte testByte[] = testInputBuffer2.nextBinaryInput();
            assertEquals(ByteBuffer.wrap(testByte).getDouble(8), ByteBuffer
                .wrap(testHeap.remove()).getDouble(8), 0.01);
        }
    }      
}
