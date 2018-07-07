 package deque;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author titan
 */
import java.util.Iterator;
import java.util.NoSuchElementException;

import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] s;
    private int head;
    private int tail;
    private int n;

    public RandomizedQueue() {
        this.s = (Item[]) new Object[2];
        this.head = 0;
        this.tail = 0;
        this.n = 0;
    }

    public boolean isEmpty() {
        return this.n == 0;
    }

    public int size() {
        return this.n;
    }

    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        if (this.n == this.s.length) {
            this.resize(2 * this.s.length);
        }
        this.s[this.tail++] = item;
        this.n++;
    }

    private void resize(int capacity) {
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < this.tail; i++) {
            temp[i] = this.s[i];
        }
        this.s = temp;
        this.head = 0;
        this.tail = n;
    }

    public Item dequeue() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Queue is empty!");
        }
        int random_int = StdRandom.uniform(this.n);
        Item item = this.s[random_int];
        this.s[random_int] = this.s[this.tail-1];
        this.s[this.tail-1] = null;
        this.n--;
        this.tail--;
        if (this.n > 0 && n == this.s.length / 4) {
            this.resize(this.s.length / 2);
        }
        return item;
    }

    public Item sample() {
        if (this.isEmpty()) {
            throw new NoSuchElementException("Queue is empty!");
        }
        return this.s[StdRandom.uniform(this.n)];
    }
    
    @Override
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {

        private int current = head;
        final private Item[] copy_s;

        public RandomizedQueueIterator() {
            copy_s = s.clone();
            StdRandom.shuffle(copy_s, head, tail);
        }

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
            if (!this.hasNext()) {
                throw new NoSuchElementException("No element");
            }
            return copy_s[this.current++];
        }
    }

    public static void main(String[] args) {
        RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();

        q.enqueue(2);

        q.enqueue(3);

        q.enqueue(1);

        q.enqueue(0);

        q.enqueue(4);

        q.enqueue(5);
        for(int i=0;i<6;i++){
            System.out.println(q.dequeue());
        }

//        Iterator<Integer> it = q.iterator();
//
//        Iterator<Integer> it2 = q.iterator();
//        while (it.hasNext()) {
//            System.out.println("Iterator it " + it.next());
//        }
//
//        while (it2.hasNext()) {
//            System.out.println("Iterator it2 " + it2.next());
//        }

    }
}
