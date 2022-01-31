/**
 * Stores run#, start location, and end location of a run
 * 
 * @author Taimoor Qamar
 * @version 10-23-19
 *
 */
public class Run {
    private int number;
    private int start;
    private int end;


    // unsure unit used for offset in file
    /**
     * Makes a new Run with the given run#, start location, and end location
     * (e.g. s = 0 and e = 1 means run is 1 BYTE long)
     * 
     * @param n
     *            run#: e.g. 1st run -> 1
     * @param s
     *            start location: offset in the run file where the run begins
     * @param e
     *            end location: offset in the run file where the run ends
     */
    public Run(int n, int s, int e) {
        number = n;
        start = s;
        end = e;
    }


    /**
     * Getter for run number
     * 
     * @return run number
     */
    public int getNumber() {
        return number;
    }


    /**
     * Getter for start
     * 
     * @return start
     */
    public int getStart() {
        return start;
    }


    /**
     * Getter for end
     * 
     * @return end
     */
    public int getEnd() {
        return end;
    }


    /**
     * Setter for run number
     * 
     * @param n
     *            the number to which to set the run number
     */
    public void setNumber(int n) {
        number = n;
    }


    /**
     * Setter for start
     * 
     * @param s
     *            the number to which to set the start
     */
    public void setStart(int s) {
        start = s;
    }


    /**
     * Setter for end
     * 
     * @param e
     *            the number to which to set the end
     */
    public void setEnd(int e) {
        end = e;
    }


    /**
     * Returns true if run complete, false otherwise.
     * 
     * @return run completion.
     */
    public boolean isComplete() {
        return start >= end;
    }


    /**
     * Prints the number, start, and end for this run
     */
    public String toString() {
        return "#: " + number + " Start: " + start + " End: " + end;
    }

}
