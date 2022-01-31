import java.io.IOException;

public class TestGenerator {
    // FIELD
    FileGenerator fileGen;

    public TestGenerator() {
        fileGen = new FileGenerator();
    }

    /**
     * Generates a set of 8 block files.
     * 
     * @throws IOException
     */
    public void createSmallFiles() throws IOException {
        fileGen.generateSortedFiles(new String[] { "8 Block File", "8" });
    }
    
    public void createMediumFiles() throws IOException {
        fileGen.generateSortedFiles(new String[] { "32 Block File", "32" });
    }
}
