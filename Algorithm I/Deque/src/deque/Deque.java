/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author titan
 */
public class Deque<Item> implements Iterable<Item> {

    /**
     * @param args the command line arguments
     */
    private class Node {

        public Item value;
        public Node next;
        public Node previous;

    }
    private Node head = new Node();
    private Node tail = new Node();
    private int size = 0;

    public Deque() {
        this.head.next = this.tail;
        this.head.previous = null;
        this.tail.next = null;
        this.tail.previous = this.head;
    }

    public boolean isEmpty() {
        return this.size == 0;
    }

    public int size() {
        return this.size;
    }

    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item must not be null");
        }
        Node current = new Node();
        current.value = item;
        // link
        current.next = this.head.next;
        current.previous = this.head;
        current.next.previous = current;
        this.head.next = current;
        this.size++;
    }

    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Item must not be null");
        }
        Node current = new Node();
        current.value = item;
        // link
        current.previous = this.tail.previous;
        current.next = this.tail;
        current.previous.next = current;
        this.tail.previous = current;
        this.size++;
    }

    public Item removeFirst() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("No element");
        }
        Node first = this.head.next;
        first.next.previous = this.head;
        this.head.next = first.next;
        this.size--;
        return first.value;
    }

    public Item removeLast() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("No element");
        }
        Node last = this.tail.previous;
        last.previous.next = this.tail;
        this.tail.previous = last.previous;
        this.size--;
        return last.value;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = head.next;

        @Override
        public boolean hasNext() {
            return this.current != tail;
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException("Unsupported method");
        }

        @Override
        public Item next() {
            if(!this.hasNext()){
                throw new NoSuchElementException("No element");
            }
            Item item = this.current.value;
            this.current = this.current.next;
            return item;
        }
    }

    public static void main(String[] args) {
        // TODO code application logic here

        Deque<Integer> deque = new Deque();
        deque.addFirst(1);
        deque.addFirst(2);
        deque.addFirst(3);
        deque.addLast(4);
        deque.removeFirst();
        deque.removeLast();
        for (int value : deque) {
            System.out.println(value);
        }
    }

}
