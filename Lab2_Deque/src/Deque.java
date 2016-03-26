import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class Deque<Item> implements Iterable<Item> {

    private Node first, last;
    private int N;

    private class Node {
        private Item value;
        private Node next, prev;

        private Node(final Item item, final Node prev, final Node next) {
            this.value = item;
            this.prev = prev;
            this.next = next;
        }
    }

    // constructs an iterator over items in order from front to end
    private class DequeIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.value;
            current = current.next;
            return item;
        }
    }


    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return N == 0;
    }

    // return the number of items on the deque
    public int size() {
        return N;
    }


    private void verifyNotEmpty() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
    }

    // add the item to the front
    public void addFirst(Item item) {
        Objects.requireNonNull(item);
        Node addingNode = new Node(item, null, first);
        if (isEmpty()) {
            // first == last == null
            last = addingNode;
        }
        else {
            first.prev = addingNode;
        }
        first = addingNode;
        ++N;
    }

    // add the item to the end
    public void addLast(Item item) {
        Objects.requireNonNull(item);
        Node addingNode = new Node(item, last, null);
        if (isEmpty()) {
            first = addingNode;
        }
        else {
            last.next = addingNode;
        }
        last = addingNode;
        ++N;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        verifyNotEmpty();
        Item firstItem = first.value;
        first = first.next;
        --N;
        if (!isEmpty()) {
            first.prev = null;
        }
        else {
            // first == null already
            last = null;
        }
        return firstItem;
    }

    // remove and return the item from the end
    public Item removeLast() {
        verifyNotEmpty();
        Item lastItem = last.value;
        last = last.prev;
        --N;
        if (!isEmpty()) {
            last.next = null;
        }
        else {
            // last == null already
            first = null;
        }
        return lastItem;
    }

    // return an iterator over items in order from front to end
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }


    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(2);
        deque.addFirst(5);
        deque.addLast(1);
        Iterator<Integer> iterator = deque.iterator();
        while (iterator.hasNext()) {
            System.out.println(iterator.next());
        }
        deque.removeFirst();
        deque.removeFirst();
        deque.removeFirst();
    }
}