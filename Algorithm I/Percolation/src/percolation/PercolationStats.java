/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package percolation;

/**
 *
 * @author titan
 */
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double fraction[];
    private int trials;
    private double mean;
    private double std;

    public PercolationStats(int n, int trials) {    // perform trials independent experiments on an n-by-n grid
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException("Must be positive!");
        }
        this.trials = trials;
        fraction = new double[this.trials];
        for (int i = 0; i < trials; i++) {
            Percolation p = new Percolation(n);
            while (!p.percolates()) {
                int row = StdRandom.uniform(1, n + 1);
                int col = StdRandom.uniform(1, n + 1);
                p.open(row, col);
            }
            fraction[i] = (p.numberOfOpenSites() / (double) (n * n));
        }
        this.mean = StdStats.mean(this.fraction);
        this.std = StdStats.stddev(this.fraction);
    }

    public double mean() {                          // sample mean of percolation threshold
        return this.mean;
    }

    public double stddev() {                        // sample standard deviation of percolation threshold
        return this.std;
    }

    public double confidenceLo() {                  // low  endpoint of 95% confidence interval
        return this.mean - 1.96 * this.std / Math.sqrt(this.trials);
    }

    public double confidenceHi() {                  // high endpoint of 95% confidence interval
        return this.mean + 1.96 * this.std / Math.sqrt(this.trials);
    }

    public static void main(String[] args) {
        // TODO code application logic here
        //PercolationStats stat = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
        PercolationStats stat = new PercolationStats(100, 200);
        System.out.println("mean = " + stat.mean());
        System.out.println("std = " + stat.stddev());
        System.out.println(stat.confidenceLo() + stat.confidenceHi());
    }
}
