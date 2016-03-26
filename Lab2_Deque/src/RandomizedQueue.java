import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] array = (Item[]) new Object[2];
    private int N;

    // constructs an iterator over items in order from front to end
    private class RandomIterator implements Iterator<Item> {
        private int pointer = 0;
        private final Item[] shuffledArray;

        private RandomIterator() {
            shuffledArray = (Item[]) new Object[N];
            System.arraycopy(array, 0, shuffledArray, 0, N);
            StdRandom.shuffle(shuffledArray);
        }

        public boolean hasNext()  {
            return pointer < shuffledArray.length;
        }

        public void remove()      {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            return shuffledArray[pointer++];
        }
    }


    // construct an empty randomized queue
    public RandomizedQueue() {

    }

    // resize the underlying array holding the elements
    private void resize(int capacity) {
        assert capacity >= N;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < N; i++) {
            temp[i] = array[i];
        }
        array = temp;
    }

    // is the queue empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the queue
    public int size() {
        return N;
    }

    // add the item
    public void enqueue(Item item) {
        Objects.requireNonNull(item);
        if (N == array.length) {
            resize(2 * array.length);
        }
        array[N++] = item;
    }

    private void verifyNotEmpty() {
        if (isEmpty()) throw new NoSuchElementException();
    }

    // remove and return a random item
    public Item dequeue() {
        verifyNotEmpty();
        int randomId = StdRandom.uniform(N);
        Item randomItem = array[randomId];
        Item last = array[N-1];
        array[randomId] = last;
        array[N-1] = null;
        N--;

        // shrink size of array if necessary
        if (N > 0 && N == array.length / 4) resize(array.length / 2);
        return randomItem;
    }

    // return (but do not remove) a random item
    public Item sample() {
        verifyNotEmpty();
        int randomId = StdRandom.uniform(N);
        return array[randomId];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomIterator();
    }


    public static void main(String[] args) {
        final RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue("AA");
        randomizedQueue.enqueue("BB");
        randomizedQueue.enqueue("BB");
        randomizedQueue.enqueue("BB");
        randomizedQueue.enqueue("BB");
        randomizedQueue.enqueue("BB");
        randomizedQueue.enqueue("CC");
        randomizedQueue.enqueue("CC");

        for (int i = 0; i < 8; ++i) {
            System.out.println(randomizedQueue.dequeue());
        }
    }
}