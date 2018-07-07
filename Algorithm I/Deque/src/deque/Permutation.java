/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deque;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

/**
 *
 * @author titan
 */
public class Permutation {

    public static void main(String[] args) {
        int size = Integer.parseInt(args[0]);
        RandomizedQueue<String> q = new RandomizedQueue();
        while(!StdIn.isEmpty()){
            String word = StdIn.readString();
            q.enqueue(word);
        }
        for(int i=0;i<size;i++){
           StdOut.println(q.dequeue());
        }

    }
}
