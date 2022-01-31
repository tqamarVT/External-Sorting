import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Scanner;
import java.util.ArrayList;
import java.nio.file.Paths;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.util.Iterator;

/**
 * Handles reading from and writing to .csv and .data files
 * 
 * @author Taimoor Qamar
 * @version 10-23-19
 *
 */
public class SaveAndLoad {
    private final int initialCapacity = 100000;
    private String filename;


    /**
     * Makes a new SaveAndLoad
     * 
     * @param fname
     *            the file name used for saving and loading
     */
    public SaveAndLoad(String fname) {
        filename = fname;

    }


    /**
     * Gives the filename associated with this SaveAndLoad
     * 
     * @return filename
     */
    public String getFilename() {
        return filename;
    }


    /**
     * Sets the filename to be newFileName
     * 
     * @param newFileName
     *            the String to become the file name.
     */
    public void setFilename(String newFileName) {
        filename = newFileName;
    }


    /**
     * Reads all of the student info from this SaveAndLoad's filename and
     * returns
     * an array of DetailedStudents with the corresponding information
     * 
     * @return an array of DetailedStudents with the corresponding
     *         information or null if error
     */
    public DetailedStudent[] loadStudentData() {
        ArrayList<DetailedStudent> studs = new ArrayList<>(initialCapacity);
 if (filename.contains(".data")) {
            try {
                byte[] bytes = Files.readAllBytes(Paths.get(filename));
                int numRecords = ((0xFF & bytes[10]) << 24) | ((0xFF
                    & bytes[11]) << 16) | ((0xFF & bytes[12]) << 8) | (0xFF
                        & bytes[13]); // converts byte array [10, 13] to int
                long pid = -1;
                String firstName = null;
                String middleName = null;
                String lastName = null;
                int currentIndex = 14;
                // System.out.println("numRecords: " + numRecords);
                for (int i = 0; i < numRecords; i++) {
                    pid = ((0xFF & bytes[currentIndex++]) << 56) | ((0xFF
                        & bytes[currentIndex++]) << 48) | ((0xFF
                            & bytes[currentIndex++]) << 40) | ((0xFF
                                & bytes[currentIndex++]) << 32) | ((0xFF
                                    & bytes[currentIndex++]) << 24) | ((0xFF
                                        & bytes[currentIndex++]) << 16) | ((0xFF
                                            & bytes[currentIndex++]) << 8)
                        | (0xFF & bytes[currentIndex++]);
                    byte[] fnBytes = new byte[16];
                    int tempIndex = 0;
                    while (bytes[currentIndex] != 0x24) { // 0x24 == $
                        // System.out.println(String.valueOf(currentIndex) + ":
                        // "
                        // + bytes[currentIndex]);
                        fnBytes[tempIndex] = bytes[currentIndex];
                        currentIndex++;
                        tempIndex++;
                    }
                    currentIndex++; // because $
                    firstName = new String(fnBytes, "UTF-8").trim();
                    byte[] mnBytes = new byte[16];
                    tempIndex = 0;
                    while (bytes[currentIndex] != 0x24) { // 0x24 == $
                        mnBytes[tempIndex] = bytes[currentIndex];
                        currentIndex++;
                        tempIndex++;
                    }
                    currentIndex++;
                    middleName = new String(mnBytes, "UTF-8").trim();
                    byte[] lnBytes = new byte[16];
                    tempIndex = 0;
                    while (bytes[currentIndex] != 0x24) { // 0x24 == $
                        lnBytes[tempIndex] = bytes[currentIndex];
                        currentIndex++;
                        tempIndex++;
                    }
                    currentIndex++;
                    lastName = new String(lnBytes, "UTF-8").trim();
                    studs.add(new DetailedStudent((int)pid, firstName,
                        middleName, lastName));
                    currentIndex += 8; // because GOHOKIES
                }
            }
            catch (IOException e) {
                // System.out.println("binary file not found to load student
                // data");
                return null;
            }
        }
        else

        {
            System.out.println("Unable to read file to load student data");
            return null;
        }
 //       System.out.println(filename + " successfully loaded"); // won't test
        return studs.toArray(new DetailedStudent[1]);
    }
}
