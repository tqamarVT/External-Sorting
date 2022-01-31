import java.lang.reflect.Array;

/**
 * Generic Key-Value pair Heap. Note that Heaps can store duplicate key-value
 * pairs. Can be min Heap or max Heap. By default, it is a min Heap. Use
 * Heap.max in constructor for a max Heap.
 * 
 * @author Taimoor Qamar
 * @version 10-23-19
 *
 */
public class TestHeap extends Heap<Double, Long> {
    // use these later for optimization:
    private static final int BLOCK_SIZE = 16384;
    private static final int RECORD_SIZE = 16;
    private static final int HEAP_SIZE = BLOCK_SIZE * 8;

    /**
     * This method only works with this project. First, output the root to the
     * buffer. Second, take next input from buffer and store in root. Third,
     * check if input is too large for this run. If yes, swap with end,
     * decrement size, and sift down. If not, just sift down.
     */
    public void replace(InputBuffer in, OutputBuffer out) {
        Node<Double, Long> lastNodeOutput = (Node<Double, Long>)(new Node<>(
            tree[0].getKey(), tree[0].getValue()));
        out.insertRecord(tree[0]); // unsure if Buffer.output() will change
        // result
        tree[0] = in.nextInput();
        if (tree[0].compareTo(lastNodeOutput) > 0) {
            swap(0, --size);
        }
        siftDown(0);
    }
}
