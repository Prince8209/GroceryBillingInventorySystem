package ds;

public class MyStack<T> {
    static class Node<T> {
        T data;
        Node<T> next;
        Node(T data) { this.data = data; }
    }

    Node<T> top;

    public void push(T item) {
        Node<T> node = new Node<>(item);
        node.next = top;
        top = node;
    }

    public T pop() {
        if (isEmpty()) return null;
        T data = top.data;
        top = top.next;
        return data;
    }

    public T peek() {
        return isEmpty() ? null : top.data;
    }

    public boolean isEmpty() {
        return top == null;
    }
}
