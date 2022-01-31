/**
 * This class holds a student's pid, ascore, and corresponding "national"
 * ranking
 * 
 * @author Taimoor Qamar
 * @version 10-23-19
 *
 */
public class StudRank {
    private long pid;
    private long rank;
    private double ascore;


    /**
     * Makes a new StudRank with the given pid and rank
     * 
     * @param personalID
     *            the personal identification number of this student
     * @param ranking
     *            a cardinal number representing this student's ascore relative
     *            to other students in the entire "country"
     * @param ascr
     *            the ascore (described in specs)
     */
    public StudRank(long ranking, long personalID, double ascr) {
        pid = personalID;
        rank = ranking;
        ascore = ascr;
    }


    /**
     * Getter for pid
     * 
     * @return pid
     */
    public long getPID() {
        return pid;
    }


    /**
     * Setter for pid
     * 
     * @param newPID
     *            the new PID
     */
    public void setPID(long newPID) {
        pid = newPID;
    }


    /**
     * Getter for rank
     * 
     * @return rank
     */
    public long getRank() {
        return rank;
    }


    /**
     * Setter for rank
     * 
     * @param newRank
     *            the new rank
     */
    public void setRank(long newRank) {
        rank = newRank;
    }


    /**
     * Getter for ascore
     * 
     * @return ascore
     */
    public double getA() {
        return ascore;
    }


    /**
     * Setter for ascore
     * 
     * @param newA
     *            the new ascore
     */
    public void setA(double newA) {
        ascore = newA;
    }
}
