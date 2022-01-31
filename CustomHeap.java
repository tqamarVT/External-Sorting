import java.nio.ByteBuffer;

/**
 * Heap that stores data as bytes. Note that Heaps can store duplicate key-value
 * pairs. Can be min Heap or max Heap. By default, it is a max Heap. Use
 * Heap.min in constructor for a min Heap.
 * 
 * @author Taimoor Qamar
 * @version 10-23-19
 *
 */
public class CustomHeap {

    /**
     * Pass Heap.min into the Constructor to get a min Heap (a Heap that can
     * find
     * the minimum in constant time, O(1))
     */
    public static final boolean min = true;
    /**
     * Pass Heap.max into the Constructor to get a max Heap (a Heap that can
     * find
     * the maximum in constant time, O(1))
     */
    public static final boolean max = false;
    private static final boolean defaultOrder = Heap.max;
    private static int defaultCapacity = 8;
    private static final int BLOCK_SIZE = 16384;
    private static final int RECORD_SIZE = 16;
    private static final int HEAP_SIZE = BLOCK_SIZE * defaultCapacity;

    private byte[][] tree;
    private int signUsedForOrder; // sort order (aka min Heap or max Heap)
    private int size; // how full the tree is


    /**
     * Makes a new Heap with the given sortOrder. sortOrder
     * should be either Heap.min (for a min Heap) or Heap.max (for a max
     * Heap).
     * 
     * @param sortOrder
     *            whether min Heap or max Heap. Use Heap.min or Heap.max.
     */
    @SuppressWarnings("unchecked")
    public CustomHeap(boolean sortOrder) {
        tree = new byte[defaultCapacity][RECORD_SIZE];
        //tree = new byte[HEAP_SIZE / RECORD_SIZE][RECORD_SIZE];
        if (sortOrder) {
            signUsedForOrder = 1; // min
        }
        else {
            signUsedForOrder = -1; // max
        }
        size = 0;
    }


    /**
     * Makes a new max Heap
     */
    public CustomHeap() {
        this(defaultOrder);
    }


    /**
     * Inserts a new record into this Heap with bytes record[]. The second 8
     * bytes of record will be treated as a Double and used for comparison.
     * Please make record.length equal to 16.
     * 
     * @param record
     *            the record in bytes to be inserted
     * @throws ArrayIndexOutOfBoundsException
     *             if you insert too many records
     */
    public void insert(byte[] record) {
// if (size == tree.length) {
// expandCapacity();
// }
        int currentNode = size;
        tree[size++] = record;
        int parent = parent(currentNode);
        // sift up:
        while (parent != -1 && compareBytes(tree[currentNode],
            tree[parent]) <= 0) {
            swap(currentNode, parent);
            currentNode = parent;
            parent = parent(currentNode);
        }
    }

    /**
     * Removes and the min value of this Heap (if min Heap) or max value of
     * this Heap (if max Heap).
     * 
     * @return the min or max depending on the type of Heap (shallow copy)
     * @throws NullPointerException
     *             if Heap is empty
     */
    public byte[] remove() {
        byte[] result = tree[0];
        swap(0, --size);
        siftDown(0);
        return result;
    }

    /**
     * Makes a new Heap in O(n) time as opposed to O(log(n)) of insert. This
     * changes the Heap so that its contents will be exactly what was in
     * rawData. In other words, it deletes the heap and then adds the data
     * from
     * rawData. Please do not pass null array elements.
     * 
     * @param rawData
     *            the array used for buildHeap algorithm
     */
    public void buildHeap(byte[][] rawData) {
        tree = rawData;
        size = tree.length;
        for (int i = size / 2 - 1; i >= 0; i--) {
            siftDown(i);
        }
    }

    /**
     * This method only works with this project. First, output the root to
     * the
     * buffer. Second, take next input from buffer and store in root. Third,
     * check if input is too large for this run. If yes, swap with end,
     * decrement size, and sift down. If not, just sift down.
     */
    public void replace(InputBuffer in, OutputBuffer out) {
        byte[] lastNodeOutput = tree[0];
        ByteBuffer bb = ByteBuffer.wrap(tree[0]);
        bb.getLong();
        // System.out.print("NEXT RECORD TO BE INPUT: " + bb.getDouble() +
        // "\n");
        out.insertBinaryRecord(tree[0]); // unsure if Buffer.output() will
                                         // change
        // result

        tree[0] = in.nextBinaryInput();
        // if next record is too big for this run
        if (compareBytes(tree[0], lastNodeOutput) < 0) {
            swap(0, --size);
        }
        siftDown(0);

    }

    /**
     * After the InputBuffer has reached end of file, we cannot replace anymore.
     * We need to make sure that we send everything that is currently in memory
     * to the output file exactly once. If it is currently in the Heap, then it
     * goes to the current Run. If it is in memory but not in the Heap, then it
     * goes to the next and final run. This method also builds the heap for the
     * final run.
     * 
     * @param out
     *            the buffer used to write records to the output file
     *            efficiently
     * @return the number of records that still need to be removed from the heap
     *         for the next run
     */
    public int finishRun(OutputBuffer out) {
        int numRecordsToPutInNextRun = HEAP_SIZE/RECORD_SIZE;
        while (size > 0) {
            out.insertBinaryRecord(tree[0]);
            numRecordsToPutInNextRun--;
            swap(0, --size);
            siftDown(0);
        }
        // buildheap with recordsToPutInNextRun:
        // clear all other bytes (they will move to the bottom of the heap after
        // bulding because NEGATIVE_INFINITY is the lowest double):
        for (int i = 0; i < HEAP_SIZE/RECORD_SIZE - numRecordsToPutInNextRun; i++) {
            // make ascore Double.NEGATIVE_INFINITY (0xfff0000000000000)
            tree[i][8] = -1; // 0xff
            tree[i][9] = -16; // 0xf0
            tree[i][10] = 0x00;
            tree[i][11] = 0x00;
            tree[i][12] = 0x00;
            tree[i][13] = 0x00;
            tree[i][14] = 0x00;
            tree[i][15] = 0x00;
        }
        buildHeap(tree);
        return numRecordsToPutInNextRun;
    }

    /**
     * Returns the number of records currently in this Heap
     * 
     * @return the size
     */
    public int getSize() {
        return size;
    }

    /**
     * Returns a shallow copy of the physical array used for the Heap.
     * 
     * @return an array of the bytes in this Heap
     */
    public byte[][] toPhysicalArray() {
        return tree;
    }


    /**
     * helper method to simplify comparison that varies if Heap is min or
     * max. I
     * just pretend that I am making a min Heap, and this method makes it
     * work
     * for max Heap too.
     * 
     * @param first
     *            is like this in this.compareTo(other)
     * @param second
     *            is like other in this.compareTo(other)
     * @return If this key is greater than the other key, return a positive
     *         int.
     *         If this key is less than the other key, return a negative
     *         int. If
     *         the keys are equal, return 0.
     */
    protected int compareBytes(byte[] first, byte[] second) {
        ByteBuffer b1 = ByteBuffer.wrap(first);
        b1.getLong();
        ByteBuffer b2 = ByteBuffer.wrap(second);
        b2.getLong();
        double one = b1.getDouble();
        double two = b2.getDouble();
        if (one > two)
            return signUsedForOrder;
        if (one < two)
            return -signUsedForOrder;
        return 0;
    }


    /**
     * helper method to find the parent of a given Node that is in this
     * Heap (could be optimized)
     * 
     * @param position
     *            the position of the child whose parent you want to find
     * @return parent parent Node or -1 if not parent not found or child not
     *         found in Heap
     */
    protected int parent(int position) {
        if (position <= 0 || position >= size) {
            return -1;
        }
        return (position - 1) / 2;
    }


    /**
     * helper method to find the left child of a given Node that is in this
     * Heap (could be optimized)
     * 
     * @param position
     *            the position of the parent whose left child you want to
     *            find
     * @return child child Node or -1 if not parent not found or child not
     *         found in Heap
     */
    protected int leftChild(int position) {
        if (position < 0 || 2 * position + 1 + 1 > size) {
            return -1;
        }
        return 2 * position + 1;
    }


    /**
     * helper method to find the left child of a given Node that is in this
     * Heap (could be optimized)
     * 
     * @param position
     *            the position of the parent whose right child you want to
     *            find
     * @return child child Node or -1 if not parent not found or child not
     *         found in Heap
     */
    protected int rightChild(int position) {
        if (position < 0 || 2 * (position + 1) + 1 > size) {
            return -1;
        }
        return 2 * (position + 1);
    }


    /**
     * helper method to swap values in an array (could throw
     * IndexOutOfBoundsException) (could be optimized)
     * 
     * @param first
     *            the index of the element that goes to position second
     * @param second
     *            the index of the element that goes to position first
     */
    protected void swap(int first, int second) {
        byte[] temp = tree[first];
        tree[first] = tree[second];
        tree[second] = temp;
    }

    /**
     * Moves tree[index] down until its children are lower priority than it.
     * 
     * @param index
     *            the index in tree of the element to sift down
     */
    protected void siftDown(int index) {
        int parent = index;
        int leftChild = leftChild(parent);
        int rightChild = rightChild(parent);
        if (leftChild == -1) {
            return;
        }
        if (rightChild == -1) { // below line is should swap
            if (compareBytes(tree[parent], tree[leftChild]) > 0) {
                swap(parent, leftChild);
            }
            return;
        }
        boolean goLeft = compareBytes(tree[leftChild], tree[rightChild]) <= 0;
        boolean shouldSwap = goLeft
            ? compareBytes(tree[parent], tree[leftChild]) > 0
            : compareBytes(tree[parent], tree[rightChild]) > 0;
        while (shouldSwap) {
            if (goLeft) {
                swap(parent, leftChild);
                parent = leftChild;
            }
            else {
                swap(parent, rightChild);
                parent = rightChild;
            }
            // move down:
            leftChild = leftChild(parent);
            rightChild = rightChild(parent);
            if (leftChild == -1) {
                return;
            }
            if (rightChild == -1) { // below line is should swap
                if (compareBytes(tree[parent], tree[leftChild]) > 0) {
                    swap(parent, leftChild);
                }
                return;
            }
            goLeft = compareBytes(tree[leftChild], tree[rightChild]) <= 0;
            shouldSwap = goLeft
                ? compareBytes(tree[parent], tree[leftChild]) > 0
                : compareBytes(tree[parent], tree[rightChild]) > 0;
        }
    }
}
