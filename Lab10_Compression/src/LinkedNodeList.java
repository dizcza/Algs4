import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Created by dizcza on 13.05.16.
 */
public class LinkedNodeList<E> {

    private Node<E> first, last;
    private int N;

    public class Node<E> {
        public E item;
        private Node<E> next, prev;

        private Node(final E item, final Node<E> prev, final Node<E> next) {
            this.item = item;
            this.prev = prev;
            this.next = next;
        }
    }

    // constructs an iterator over items in order from front to end
    private class AscendingIterator implements Iterator<Node<E>> {
        private Node<E> next = first;
        public boolean hasNext()  { return next != null; }

        public Node<E> next() {
            if (!hasNext()) throw new NoSuchElementException();
            Node<E> curr = next;
            next = next.next;
            return curr;
        }
    }

    // construct an empty linked list
    public LinkedNodeList() {
    }

    // is the list empty?
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
    public void addFirst(E item) {
        Objects.requireNonNull(item);
        Node<E> addingNode = new Node<>(item, null, first);
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
    public void addLast(E item) {
        Objects.requireNonNull(item);
        Node<E> addingNode = new Node<>(item, last, null);
        if (isEmpty()) {
            first = addingNode;
        }
        else {
            last.next = addingNode;
        }
        last = addingNode;
        ++N;
    }

    public E remove(Node<E> node) {
        Objects.requireNonNull(node);
        E item = node.item;
        if (node.prev != null) {
            node.prev.next = node.next;
        }
        if (node.next != null) {
            node.next.prev = node.prev;
        }
        if (node == first) {
            first = node.next;
        }
        if (node == last) {
            last = node.prev;
        }
        --N;
        return item;
    }

    public E remove(int index) {
        Node<E> curr = first;
        for (int i = 0; i < index; ++i) {
            curr = curr.next;
        }
        return remove(curr);
    }

    // return an iterator over items in order from front to end
    public Iterator<Node<E>> iterator() {
        return new AscendingIterator();
    }

}
