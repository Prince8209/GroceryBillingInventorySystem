package ds;

public class MyLinkedList<T> {
    static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) { this.data = data; }
    }

    Node<T> head;

    public void add(T item) {
        Node<T> node = new Node<>(item);
        if (head == null) head = node;
        else {
            Node<T> temp = head;
            while (temp.next != null) temp = temp.next;
            temp.next = node;
        }
    }

    public void removeAt(int index) {
        if (index < 0 || head == null) return;
        if (index == 0) { head = head.next; return; }
        Node<T> temp = head;
        for (int i = 0; i < index - 1 && temp.next != null; i++) temp = temp.next;
        if (temp.next != null) temp.next = temp.next.next;
    }

    public T getAt(int index) {
        Node<T> temp = head;
        for (int i = 0; i < index && temp != null; i++) temp = temp.next;
        return (temp != null) ? temp.data : null;
    }

    public int size() {
        int count = 0;
        Node<T> temp = head;
        while (temp != null) { count++; temp = temp.next; }
        return count;
    }

    public boolean isEmpty() { return head == null; }

    public void clear() { head = null; }
}
