import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;

/**
 * Modified from Cliff Schaffer's FileGenerator, Generates a binary file of PID
 * and Ascore values for testing.
 * 
 * @author Taimoor Qamar
 * @version 10/25/2019
 */

public class FileGenerator {
    // FIELDS
    private static final int NUM_RECS = 1024;
    private static final int BLOCK_SIZE = 16384;
    static private Random value = new Random(); // Hold the Random class
    private String fileName;
    private DLList<Run> runsTracker = new DLList<Run>();
    private RandomAccessFile r;


    // ---------------------------------------------------------------------------
    /**
     * This function generates a random number.
     *
     * @param n
     *            the ceiling
     * @return a random number
     */
    public int random(int n) {
        return Math.abs(value.nextInt()) % n;
    }


    // ---------------------------------------------------------------------------
    /**
     * This method generates a binary file with full blocks, the number of
     * blocks passed through args.
     *
     * @param args
     *            an array of arguments
     * @throws IOException
     *             an exception
     */
    public void generateFile(String[] args) throws IOException {
        long pID;
        double aScore;
        fileName = args[0];
        int fileSize = Integer.parseInt(args[1]); // Size of file in blocks
        DataOutputStream file = new DataOutputStream(new BufferedOutputStream(
            new FileOutputStream(args[0])));
        System.out.print("TESTFILE NAME IS: " + fileName + "\n");

        for (int i = 0; i < fileSize; i++) {
            System.out.print("BLOCK NUMBER IS: " + (i + 1) + "\n");
            for (int j = 0; j < NUM_RECS; j++) {
                pID = (long)(100000000 + random(900000000)); // to make it a 9
                                                             // digit number
                aScore = (double)(random(10000)); // Just something random,
                                                  // don't know if there are
                                                  // restrictions on Ascore.
                file.writeLong(pID);
                file.writeDouble(aScore);
            }
        }
        file.flush();
        file.close();
    }


    // ---------------------------------------------------------------------------
    /**
     * This method generates a binary file with full blocks and one half block,
     * the number of blocks passed through args.
     *
     * @param args
     *            an array of arguments
     * @throws IOException
     *             an exception
     */
    public void generatePartialFile(String[] args) throws IOException {
        long pID;
        double aScore;
        fileName = args[0];
        int numBlocks = Integer.parseInt(args[1]); // Size of file in blocks
        DataOutputStream file = new DataOutputStream(new BufferedOutputStream(
            new FileOutputStream(args[0])));
        System.out.print("TESTFILE NAME IS: " + fileName + "\n");

        for (int i = 0; i < numBlocks; i++) {
            System.out.print("BLOCK NUMBER IS: " + (i + 1) + "\n");
            for (int j = 0; j < NUM_RECS; j++) {
                pID = (long)(100000000 + random(900000000)); // to make it a 9
                                                             // digit number
                aScore = (double)(random(10000)); // Just something random,
                                                  // don't know if there are
                                                  // restrictions on Ascore.
                System.out.print("PID IS: " + pID + "\n");
                file.writeLong(pID);
                System.out.print("AScore IS: " + aScore + "\n");
                file.writeDouble(aScore);
            }
        }
        System.out.print("FINAL HALF BLOCK: \n");
        for (int i = 0; i < NUM_RECS / 2; i++) {
            pID = (long)(100000000 + random(900000000)); // to make it a 9
            // digit number
            aScore = (double)(random(10000)); // Just something random,
            // don't know if there are
            // restrictions on Ascore.
            System.out.print("PID IS: " + pID + "\n");
            file.writeLong(pID);
            System.out.print("AScore IS: " + aScore + "\n");
            file.writeDouble(aScore);
        }
        file.flush();
        file.close();
    }


    // ---------------------------------------------------------------------------
    /**
     * This method generates three binary files, one unsorted, one reverse
     * sorted (min to max), and one sorted (max to min). The number of blocks is
     * passed through the args.
     * 
     * @param args
     * @throws IOException
     */
    public void generateSortedFiles(String[] args) throws IOException {
        long pID;
        double aScore;
        fileName = args[0];
        String unsortedFilename = "Unsorted " + fileName;
        String reverseSortedFilename = "Reverse Sorted " + fileName;
        String sortedFilename = "Sorted " + fileName;
        int fileSize = Integer.parseInt(args[1]); // Size of file in blocks
        @SuppressWarnings("unchecked")
        Node<Double, Long>[] recordsArray =
            (Node<Double, Long>[])new Node[fileSize * NUM_RECS];
        // ===============================================================================
        DataOutputStream unsortedFile = new DataOutputStream(
            new BufferedOutputStream(new FileOutputStream(unsortedFilename)));
        DataOutputStream reverseSortedFile = new DataOutputStream(
            new BufferedOutputStream(new FileOutputStream(
                reverseSortedFilename)));
        DataOutputStream sortedFile = new DataOutputStream(
            new BufferedOutputStream(new FileOutputStream(sortedFilename)));
        // ===============================================================================

// System.out.print("TESTFILE NAME IS: " + unsortedFilename + "\n");
        for (int i = 0; i < fileSize; i++) {
// System.out.print("BLOCK NUMBER IS: " + (i + 1) + "\n");
            for (int j = i * NUM_RECS; j < (i + 1) * NUM_RECS; j++) {
                pID = (long)(100000000 + random(900000000)); // to make it a 9
                                                             // digit number
                aScore = (double)(random(10000)); // Just something random,
                                                  // don't know if there are
                                                  // restrictions on Ascore.
// System.out.print("Key: " + aScore + " | Value: " + pID + "\n");
                unsortedFile.writeLong(pID);
                unsortedFile.writeDouble(aScore);
                recordsArray[j] = new Node<Double, Long>(aScore, pID);
            }
        }
        unsortedFile.flush();
        unsortedFile.close();
        // =============================================================================================
        Arrays.sort(recordsArray, null);

// System.out.print("TESTFILE NAME IS: " + reverseSortedFilename + "\n");
        for (int i = 0; i < recordsArray.length; i++) {
// System.out.print("Key: " + recordsArray[i].getKey() + " | Value: "
// + recordsArray[i].getValue() + "\n");
            reverseSortedFile.writeLong(recordsArray[i].getValue());
            reverseSortedFile.writeDouble(recordsArray[i].getKey());
        }
        reverseSortedFile.flush();
        reverseSortedFile.close();

        // =============================================================================================
        for (int i = 0; i < recordsArray.length / 2; i++) {
            Node<Double, Long> temp = recordsArray[i];
            recordsArray[i] = recordsArray[recordsArray.length - i - 1];
            recordsArray[recordsArray.length - i - 1] = temp;
        }

// System.out.print("TESTFILE NAME IS: " + sortedFilename + "\n");
        for (int i = 0; i < recordsArray.length; i++) {
// System.out.print("Key: " + recordsArray[i].getKey() + " | Value: "
// + recordsArray[i].getValue() + "\n");
            sortedFile.writeLong(recordsArray[i].getValue());
            sortedFile.writeDouble(recordsArray[i].getKey());
        }
        sortedFile.flush();
        sortedFile.close();
    }


    // ---------------------------------------------------------------------------
    /**
     * This method generates a binary file with full blocks, the number of
     * blocks passed through args.
     *
     * @param args
     *            an array of arguments
     * @throws IOException
     *             an exception
     */
    public DLList<Run> generateRunFile(String[] args) throws IOException {
        long pID;
        double aScore = (double)(random(10000)) * 10000;
        double temp;
        int runNumber = 0;
        ByteBuffer byteBuffer;
        fileName = args[0];
        int fileSize = Integer.parseInt(args[1]); // Size of file in blocks
        byte[] byteArr = new byte[8 * BLOCK_SIZE];
        byteBuffer = ByteBuffer.wrap(byteArr);
        r = new RandomAccessFile(new File(fileName), "rw");
        long fileSize2 = r.length();
        long start = r.getFilePointer();
        for (int i = 0; i < fileSize; i++) {

            for (int j = 0; j < NUM_RECS; j++) {
                pID = (long)(100000000 + random(900000000)); // to make it a
                                                             // 9
                                                             // digit number
                temp = aScore - 100;
                byteBuffer.putLong(pID);
                byteBuffer.putDouble(temp);
                aScore = temp;

            }
            if ((i + 1) % 8 == 0) {
                runNumber++;
                r.write(byteArr);
                runsTracker.add(new Run(runNumber, (int)start, (int)(r
                    .getFilePointer() - 1)));
                start = r.getFilePointer();
                byteArr = new byte[8 * BLOCK_SIZE];
                byteBuffer = ByteBuffer.wrap(byteArr);
                aScore = (double)(random(10000)) * 10000;
            }
        }
        System.out.print(r.length() + "\n");
        r.close();
        return runsTracker;
    }


    // ---------------------------------------------------------------------------
//    /**
//     * This method generates a binary file ending with partial blocks, the
//     * number of whole blocks passed through args.
//     *
//     * @param args
//     *            an array of arguments
//     * @throws IOException
//     *             an exception
//     */
//    public DLList<Run> generatePartialRunFiles(String[] args)
//        throws IOException {
//        long pID;
//        double aScore = (double)(random(10000)) * 10000;
//        double temp;
//        int runNumber = 0;
//        ByteBuffer byteBuffer;
//        fileName = args[0];
//        int fileSize = Integer.parseInt(args[1]); // Size of file in blocks
//        byte[] byteArr = new byte[8 * BLOCK_SIZE];
//        byteBuffer = ByteBuffer.wrap(byteArr);
//        r = new RandomAccessFile(new File(fileName), "rw");
//        long fileSize2 = r.length();
//        long start = r.getFilePointer();
//        for (int i = 0; i < fileSize; i++) {
//
//            for (int j = 0; j < NUM_RECS + random(1000); j++) {
//                pID = (long)(100000000 + random(900000000)); // to make it a
//                                                             // 9
//                                                             // digit number
//                temp = aScore - 100;
//                byteBuffer.putLong(pID);
//                byteBuffer.putDouble(temp);
//                aScore = temp;
//
//            }
//            if ((i + 1) % 8 == 0) {
//                runNumber++;
//                r.write(byteArr);
//                runsTracker.add(new Run(runNumber, (int)start, (int)(r
//                    .getFilePointer() - 1)));
//                start = r.getFilePointer();
//                byteArr = new byte[8 * BLOCK_SIZE];
//                byteBuffer = ByteBuffer.wrap(byteArr);
//                aScore = (double)(random(10000)) * 10000;
//            }
//        }
//        System.out.print(r.length() + "\n");
//        r.close();
//        return runsTracker;
//    }

}
