import java.io.IOException;

public class FileGeneratorTest extends student.TestCase {
    // FIELDS
    private FileGenerator fileGen = new FileGenerator();


    /**
     * Initializes the tests for the FileGenerator Class.
     * 
     * @throws IOException
     */
    public void setUp() throws IOException {
        // fileGen.generateFile(new String[] { "b", "testGenerator", "1" });
    }


    public void testRandom() throws IOException {
        fileGen.generateFile(new String[] {"testGenerator", "1" });
        assertNotNull(fileGen);
    }
    
    /**
     * Using to create an 8.5 block file
     * @throws IOException 
     */
    public void testUnevenBlockFile() throws IOException {
        //fileGen.generatePartialFile(new String[] {"nah", "1"});      
        fileGen.random(100);
        fileGen.random(1000);
        fileGen.random(10);
    }
    
    public void testSortedFiles() throws IOException {
        fileGen.generateSortedFiles(new String[] {"One_Input_Block_Sort_Testfile", "1" });
    }
    
    public void testPartialRunfileGenerator() throws IOException {
    //    fileGen.generatePartialRunFiles(new String[] {"Uneven partial runfiles", "128" });
    }

}
