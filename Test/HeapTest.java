import student.TestCase;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Tests the Heap Class
 * 
 * @author Taimoor Qamar
 * @version 10-23-19
 *
 */
public class HeapTest extends TestCase {
    private Heap<Integer, String> basic;
    private Heap<Integer, String> empty;
    private Heap<Integer, String> basic2;
    private Heap<Integer, String> diffBasic;


    /**
     * sets up fields before each test
     */
    public void setUp() {
        basic = new Heap<>(); // min Heap
        empty = new Heap<>();
        basic2 = new Heap<>();
        diffBasic = new Heap<>();
    }


    /**
     * tests equals method of Heap
     */
    @SuppressWarnings("unlikely-arg-type")
    public void testEquals() {
        Heap<Integer, String> nullHeap = null;
        assertFalse(basic.equals(nullHeap));
        assertTrue(basic.equals(basic));
        Heap<Long, String> diffHeap = new Heap<>();
        assertEquals(basic, diffHeap);

        ArrayList<Integer> i = new ArrayList<>();
        ArrayList<Long> l = new ArrayList<>();
        assertEquals(l, i);

        String diffClass = "sdjbiosebjv";
        assertFalse(basic.equals(diffClass));

        assertTrue(basic.physicallyEqual(empty));
        assertTrue(Arrays.equals(basic.toPhysicalArray(), empty
            .toPhysicalArray()));
        assertTrue(Arrays.equals(basic.toNodeArray(), empty.toNodeArray()));
        assertTrue(Arrays.equals(basic.toKeyArray(new Integer[1]), empty
            .toKeyArray(new Integer[1])));
        assertTrue(Arrays.equals(basic.toValueArray(new String[1]), empty
            .toValueArray(new String[1])));
        assertEquals(basic.toString(), empty.toString());
    }


    /**
     * tests insert method of Heap (basic test)
     */
    public void testInsert() {
        // Super basic:
        assertEquals(basic, empty);
        basic.insert(1, "a");
        assertFalse(basic.equals(empty));

        assertEquals(basic2, empty);
        basic2.insert(1, "a");
        assertFalse(basic2.equals(empty));
        assertEquals(basic, basic2);

        assertEquals(diffBasic, empty);
        diffBasic.insert(2, "a");
        assertFalse(diffBasic.equals(empty));
        assertFalse(diffBasic.equals(basic));
        assertFalse(diffBasic.equals(basic2));

        basic = new Heap<>();
        basic2 = new Heap<>();
        diffBasic = new Heap<>();
        // Basic Ordering:
        basic.insert(1, "a");
        basic.insert(2, "a");

        basic2.insert(2, "a");
        basic2.insert(1, "a");
        assertEquals(basic, basic2);
// test more data
        basic.insert(2, "a");
        basic.insert(3, "a");
        basic.insert(3, "a");
        basic.insert(3, "a");
        basic.insert(3, "a");
        basic.insert(4, "a");
        basic.insert(4, "a");
        basic.insert(4, "a");
        basic.insert(4, "a");
        basic.insert(4, "a");
        basic.insert(4, "a");
        basic.insert(4, "a");
        basic.insert(4, "a");

        basic2.insert(4, "a");
        basic2.insert(3, "a");
        basic2.insert(4, "a");
        basic2.insert(2, "a");
        basic2.insert(3, "a");
        basic2.insert(4, "a");
        basic2.insert(4, "a");
        basic2.insert(3, "a");
        basic2.insert(4, "a");
        basic2.insert(4, "a");
        basic2.insert(3, "a");
        basic2.insert(4, "a");
        basic2.insert(4, "a");

        assertEquals(basic, basic2);
        assertTrue(basic.physicallyEqual(basic2));
        assertTrue(Arrays.equals(basic.toPhysicalArray(), basic2
            .toPhysicalArray()));
        assertTrue(Arrays.equals(basic.toNodeArray(), basic2.toNodeArray()));
        assertTrue(Arrays.equals(basic.toKeyArray(new Integer[1]), basic2
            .toKeyArray(new Integer[1])));
        assertTrue(Arrays.equals(basic.toValueArray(new String[1]), basic2
            .toValueArray(new String[1])));
        assertEquals(basic.toString(), basic2.toString());

        basic.insert(5, "a");
        basic.insert(4, "a");
        basic2.insert(4, "a");
        basic2.insert(5, "a");
        assertFalse(basic.equals(basic2));
        assertFalse(Arrays.equals(basic.toPhysicalArray(), basic2
            .toPhysicalArray()));
        assertFalse(Arrays.equals(basic.toNodeArray(), basic2.toNodeArray()));
        assertFalse(Arrays.equals(basic.toKeyArray(new Integer[1]), basic2
            .toKeyArray(new Integer[1])));
        assertTrue(Arrays.equals(basic.toValueArray(new String[1]), basic2
            .toValueArray(new String[1])));
        assertFalse(basic.toString().equals(basic2.toString()));

    }


    /**
     * tests the remove method of Heap
     */
    @SuppressWarnings("unchecked")
    public void testRemove() {
        boolean found = false;
        try {
            empty.remove();
        }
        catch (NullPointerException e) {
            found = true;
        }
        assertTrue(found);
        basic2.insert(4, "a");
        basic2.insert(3, "a");
        basic2.insert(4, "a");
        basic2.insert(2, "a");
        basic2.insert(3, "a");
        basic2.insert(4, "a");
        basic2.insert(4, "a");
        basic2.insert(3, "a");
        basic2.insert(4, "a");
        basic2.insert(4, "a");
        basic2.insert(3, "a");
        basic2.insert(4, "a");
        basic2.insert(4, "a");
        assertEquals(basic2.remove(), new Node<>(2, "a"));
        assertEquals(basic2.remove(), new Node<>(3, "a"));
        assertEquals(basic2.remove(), new Node<>(3, "a"));
        assertEquals(basic2.remove(), new Node<>(3, "a"));
        assertEquals(basic2.remove(), new Node<>(3, "a"));
        assertEquals(basic2.remove(), new Node<>(4, "a"));
        assertEquals(basic2.remove(), new Node<>(4, "a"));
        assertEquals(basic2.remove(), new Node<>(4, "a"));
        assertEquals(basic2.remove(), new Node<>(4, "a"));
        assertEquals(basic2.remove(), new Node<>(4, "a"));
        assertEquals(basic2.remove(), new Node<>(4, "a"));
        assertEquals(basic2.remove(), new Node<>(4, "a"));
        assertEquals(basic2.remove(), new Node<>(4, "a"));
        assertEquals(basic2.getSize(), 0);

        String value = "a";
        int size = 100;
        int[] randomData = new int[size];
        int[] randomData2 = new int[size];
        Node<Integer, String>[] rawNodes = new Node[size];
        for (int i = 0; i < size; i++) {
            randomData[i] = (int)(Math.random() * size);
            randomData2[i] = randomData[i];
            rawNodes[i] = new Node<>(randomData[i], value);
            basic.insert(randomData[i], value);
        }
        int[] result = new int[size];
        for (int i = 0; i < size; i++) {
            result[i] = basic.remove().getKey();
        }
        Arrays.sort(randomData);
        assertTrue(Arrays.equals(randomData, result));
    }


    /**
     * tests the buildHeap() method of Heap with random data
     */
    @SuppressWarnings("unchecked")
    public void testBuildHeap() {
        String value = "a";
        for (int z = 0; z < 20; z++) {
            int size = 100 + (int)(Math.random() * 15);
            int[] randomData = new int[size];
            int[] randomData2 = new int[size];
            Node<Integer, String>[] rawNodes = new Node[size];
            for (int i = 0; i < size; i++) {
                randomData[i] = (int)(Math.random() * size);
                randomData2[i] = -1 * randomData[i];
                rawNodes[i] = new Node<>(randomData[i], value);
                basic2.insert(randomData[i], value);
            }
            basic.buildHeap(rawNodes);
            // for test coverage:
            basic.find();
            basic.getSize();
            basic.physicallyEqual(basic);
            basic.toValueArray(new String[1]);
            basic.toKeyArray(new Integer[1]);
            basic.toPhysicalArray();
            basic.toNodeArray();
            basic.toString();
            int[] result = new int[size];
            for (int i = 0; i < size; i++) {
                int root = basic.find().getKey();
                result[i] = basic.remove().getKey();
                assertEquals(result[i], root);
                assertEquals(basic.getSize(), size - (i + 1));
            }
            Arrays.sort(randomData);
            Arrays.sort(randomData2);
            int[] correctLength = new int[size];
            for (int i = 0; i < size; i++) {
                correctLength[i] = -1 * randomData2[i];
            }
            Node<Integer, String>[] phys = basic.toPhysicalArray();
            int[] physInt = new int[phys.length];
            for (int i = 0; i < phys.length; i++) {
                physInt[i] = phys[i].getKey();
            }
            assertTrue(Arrays.equals(physInt, correctLength));
            // assertTrue(Arrays.equals(randomData, randomData2)); QuickSort is
            // not
            // stable

            assertTrue(Arrays.equals(randomData, result));
        }
    }

}
