/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package deque;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 *
 * @author titan
 */
public class Permutation {

    public static void main(String[] args) {
        // implement the reservoir sampling
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> q = new RandomizedQueue<String>();
        double n = 0;
        while (!StdIn.isEmpty()) {
            n++;
            String word = StdIn.readString();
            if (n > k) {
                double r = StdRandom.uniform();
                if (r < k/n) {
                    q.dequeue();
                    q.enqueue(word);
                }
            }
            else{
                q.enqueue(word);
            }
            
        }
        for (int i = 0; i < k; i++) {
            StdOut.println(q.dequeue());
        }
    }
}
