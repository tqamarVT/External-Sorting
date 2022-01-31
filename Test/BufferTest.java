import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Test class for the Buffer class.
 * 
 * @author Taimoor Qamar
 *
 */
public class BufferTest extends student.TestCase {
    // FIELDS
    private InputBuffer testInputBuffer;
    private OutputBuffer testOutputBuffer;


    // -------------------------------------------------------------------------
    /**
     * Initializes the test-cases for the Buffer class.
     * 
     * @throws IOException
     */
    public void setUp() throws IOException {
        testInputBuffer = new InputBuffer("bufferTestFile_8_Blocks");
        testOutputBuffer = new OutputBuffer("outputBufferTestFile_One_Block");

    }


    // -------------------------------------------------------------------------
    /**
     * Tests that objects were initialized correctly.
     */
    public void testSetup() {
        assertNotNull(testInputBuffer);
        assertNotNull(testOutputBuffer);

    }


    // -------------------------------------------------------------------------
    /**
     * Tests the fillBuffer method.
     * 
     * @throws IOException
     */
    public void testFillBuffer() throws IOException {
        assertFalse(testInputBuffer.hasInput()); // assert buffer has no input
        testInputBuffer = new InputBuffer("bufferTestFile");
        testInputBuffer.fillBuffer(); // fill one full block of the buffer
        assertTrue(testInputBuffer.hasInput()); // assert buffer has input

    }


    // -------------------------------------------------------------------------
    /**
     * Tests the nextInput method.
     * 
     * @throws IOException
     */
    public void testNextInput() throws IOException {
        // FULL BLOCK FILE TEST
        Node<Double, Long> testNode = null; // test node to hold nextInput value
                                            // from buffer.
        assertNull(testNode);
        testInputBuffer = new InputBuffer("bufferTestFile");
        testInputBuffer.fillBuffer();
        int records = 0;
        while (testInputBuffer.hasInput()) {
            testNode = testInputBuffer.nextInput();
            records++;
            System.out.print(testNode.toString() + " records =" + records
                + "\n");
            // visual test, see that the records match the
            // "bufferTestFile.txt" records. Can change to asserts
            // later.
        }
        assertNotNull(testNode);

        // PARTIAL BLOCK FILE TEST
        testNode = null;
        records = 0;
        testInputBuffer = new InputBuffer("1.5_Block_Input_File");
        testInputBuffer.fillBuffer();
        while (testInputBuffer.hasInput()) {
            testNode = testInputBuffer.nextInput();
            records++;
            System.out.print(testNode.toString() + " records =" + records
                + "\n");
            // visual test, see that the records match the
            // "bufferTestFile.txt" records. Can change to asserts
            // later.
        }
        testInputBuffer.fillBuffer();
        while (testInputBuffer.hasInput()) {
            testNode = testInputBuffer.nextInput();
            records++;
            System.out.print(testNode.toString() + " records =" + records
                + "\n");
            // visual test, see that the records match the
            // "bufferTestFile.txt" records. Can change to asserts
            // later.
        }
        assertNotNull(testNode);
    }


    // -------------------------------------------------------------------------
    /**
     * Tests the nextBinaryInput method.
     * 
     * @throws IOException
     */
    public void testNextBinaryInput() throws IOException {

        // FULL BLOCK FILE TEST
        byte[] testByteArr = null; // test byte Aray to hold nextInput value
        // from buffer.
        assertNull(testByteArr);
        testInputBuffer = new InputBuffer("bufferTestFile");
        testInputBuffer.fillBuffer();
        int records = 0;
        while (testInputBuffer.hasInput()) {
            testByteArr = testInputBuffer.nextBinaryInput();
            ByteBuffer testByteBuffer = ByteBuffer.wrap(testByteArr);
            records++;
            Long pid = testByteBuffer.getLong();
            Double Ascore = testByteBuffer.getDouble();
            System.out.print("Key: " + Ascore + " | Value: " + pid
                + " records =" + records + "\n"); // visual test, see that the
                                                  // records match the
                                                  // "bufferTestFile.txt"
                                                  // records. Can change to
                                                  // asserts later.
        }
        assertNotNull(testByteArr);

        // PARTIAL BLOCK FILE TEST
        testByteArr = null;
        records = 0;
        testInputBuffer = new InputBuffer("1.5_Block_Input_File");
        testInputBuffer.fillBuffer();
        while (testInputBuffer.hasInput()) {
            testByteArr = testInputBuffer.nextBinaryInput();
            ByteBuffer testByteBuffer = ByteBuffer.wrap(testByteArr);
            records++;
            Long pid = testByteBuffer.getLong();
            Double Ascore = testByteBuffer.getDouble();
            System.out.print("Key: " + Ascore + " | Value: " + pid
                + " records =" + records + "\n");
            // visual test, see that the records match the
            // "bufferTestFile.txt" records. Can change to asserts
            // later.
        }
        testInputBuffer.fillBuffer();
        while (testInputBuffer.hasInput()) {
            testByteArr = testInputBuffer.nextBinaryInput();
            ByteBuffer testByteBuffer = ByteBuffer.wrap(testByteArr);
            records++;
            Long pid = testByteBuffer.getLong();
            Double Ascore = testByteBuffer.getDouble();
            System.out.print("Key: " + Ascore + " | Value: " + pid
                + " records =" + records + "\n");
        }
    }


    // -------------------------------------------------------------------------
    /**
     * Tests the initialLoad method.
     * 
     * @throws IOException
     */
    public void testInitialLoad() throws IOException {

        // FULL BLOCK FILE TEST
        Node<Double, Long>[] testRecordsArr = null; // create node array to hold
                                                    // initialLoad return value.
        assertNull(testRecordsArr);
        int records = 0;
        testRecordsArr = testInputBuffer.initialLoad(); // execute initialLoad
        assertNotNull(testRecordsArr);
        assertEquals(testRecordsArr.length, 8192); // assert node array is 8192
                                                   // records (8 blocks) long
        for (int i = 0; i < testRecordsArr.length; i++) {
            records++;
            System.out.print(testRecordsArr[i].toString() + " records ="
                + records + "\n"); // visual check with
                                   // "bufferTestFile_8_Blocks.docx file"F
            if (records == 1024) {
                System.out.print("================================"
                    + "=============================="
                    + "====NEXT BLOCK================="
                    + "================================"
                    + "================= \n");
                records = 0; // Print out a marker when next block is reached,
                             // and reset block check marker.
            }
        }
    }


    // -------------------------------------------------------------------------
    /**
     * Tests the initialBinaryLoad method.
     * 
     * @throws IOException
     */
    public void testInitialBinaryLoad() throws IOException {
        byte[][] testBinaryRecordsArr = null; // create node array to hold
                                              // initialLoad return value.
        assertNull(testBinaryRecordsArr);
        int records = 0;
        testBinaryRecordsArr = testInputBuffer.initialBinaryLoad(); // execute
                                                                    // initialLoad
        assertNotNull(testBinaryRecordsArr);
        assertEquals(testBinaryRecordsArr.length, 8192); // assert node array is
                                                         // 8192 records (8
                                                         // blocks) long
        for (int i = 0; i < testBinaryRecordsArr.length; i++) {
            records++;
            ByteBuffer testByteBuffer = ByteBuffer.wrap(
                testBinaryRecordsArr[i]);
            Long pid = testByteBuffer.getLong();
            Double Ascore = testByteBuffer.getDouble();
            System.out.print("Key: " + Ascore + " | Value: " + pid
                + " records =" + records + "\n");

            if (records == 1024) {
                System.out.print("================================"
                    + "=============================="
                    + "====NEXT BLOCK================="
                    + "================================"
                    + "================= \n");
                records = 0; // Print out a marker when next block is reached,
                             // and reset block check marker.
            }
        }
    }


    // -------------------------------------------------------------------------
    /**
     * Tests the insertRecord method for various conditions.
     * 
     * @throws IOException
     */
    public void testInsertRecord() throws IOException {
        Node<Double, Long>[] testInputRecordsArr = null; // node array to hold
                                                         // inputBuffer's
                                                         // initialLoad value.
        @SuppressWarnings("unchecked")
        Node<Double, Long>[] testOutputRecordsArr =
            (Node<Double, Long>[])new Node[1024]; // node array to hold
                                                  // inputBuffer's nextInput
                                                  // value once outputBuffer's
                                                  // file is read.

        testInputRecordsArr = testInputBuffer.initialLoad(); // load inputBuffer
                                                             // with 8 block
                                                             // file.

        // fill outputBuffer with the first 1024 records from the 8 block file
        // loaded into inputBuffer. Once it is full, it will write one block to
        // the "outputBufferTestFile_One_Block" file.
        for (int i = 0; i < 1024; i++) {
            testOutputBuffer.insertRecord(testInputRecordsArr[i]);
        }
        testInputBuffer.clear(); // clear the buffer to close the input stream.
        testOutputBuffer.clear(); // clear the buffer to close the output
                                  // stream.

        // load the inputBuffer with the newly created
        // outputBufferTestFile_One_Block file.
        testInputBuffer = new InputBuffer("outputBufferTestFile_One_Block"); // Change
        testInputBuffer.fillBuffer(); // Fill one block

        // Fill the output node array with 1024 records (1 block) from the
        // inputBuffer.
        for (int i = 0; i < 1024; i++) {
            testOutputRecordsArr[i] = testInputBuffer.nextInput();
        }

        // Assert that the first 1024 nodes from the input node array equals the
        // 1024 nodes in the new output node array.
        for (int i = 0; i < 1024; i++) {
            assertTrue(testInputRecordsArr[i].equals(testOutputRecordsArr[i]));
        }
    }
}
