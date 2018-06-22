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
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private int grid_size;
    private int open_size_num;
    private WeightedQuickUnionUF unionUF;
    private WeightedQuickUnionUF openUF;
    private int upper_dummy;
    private int bottom_dummy;

    public Percolation(int n) { // create n-by-n grid, with all sites blocked
        if (n <= 0) {
            throw new IllegalArgumentException("n must be positive");
        } else {
            this.grid = new boolean[n][n];
            this.grid_size = n;
            this.open_size_num = 0;
            this.unionUF = new WeightedQuickUnionUF(n * n + 2);
            this.openUF = new WeightedQuickUnionUF(n * n + 1);
            this.upper_dummy = 0;
            this.bottom_dummy = this.grid_size * this.grid_size + 1;
            this.connect_dummy();
        }
    }

    public void open(int row, int col) {// open site (row, col) if it is not open already
        this.checkbound(row, col);
        if (!this.isOpen(row, col)) {
            this.grid[row - 1][col - 1] = true;
            this.open_size_num += 1;
            if (this.grid_size > 1) {
                this.locate(row, col);
            }
        }
    }

    private void connect_dummy() {
        for (int i = 1; i <= this.grid_size; i++) {
            // upper side
            this.unionUF.union(this.upper_dummy, this.index_convertor(1, i));
            this.openUF.union(this.upper_dummy, this.index_convertor(1, i));
            // bottom side
            this.unionUF.union(this.bottom_dummy, this.index_convertor(this.grid_size, i));
        }
    }

    private int index_convertor(int row, int col) {
        this.checkbound(row, col);
        return (row - 1) * this.grid_size + col;
    }

    private void open_neighbor(int[][] list, int row, int col) {
        for (int i = 0; i < list.length; i++) {
            int x = list[i][0];
            int y = list[i][1];
            if (this.isOpen(x, y)) {
                this.unionUF.union(this.index_convertor(row, col), this.index_convertor(x, y));
                this.openUF.union(this.index_convertor(row, col), this.index_convertor(x, y));
            }
        }
    }

    private void locate(int row, int col) {
        int[][] list;
        // upper left
        if (row == 1 && col == 1) {
            list = new int[][]{
                {row, col + 1}, {row + 1, col}
            };
        } // upper right
        else if (row == 1 && col == this.grid_size) {
            list = new int[][]{
                {row, col - 1}, {row + 1, col}
            };
        } // bottom left
        else if (row == this.grid_size && col == 1) {
            list = new int[][]{
                {row, col + 1}, {row - 1, col}
            };
        } // bottom right
        else if (row == this.grid_size && col == this.grid_size) {
            list = new int[][]{
                {row, col - 1}, {row - 1, col}
            };
        } // upper side
        else if (row == 1) {
            list = new int[][]{
                {row, col + 1}, {row, col - 1}, {row + 1, col}
            };
        } // left side
        else if (col == 1) {
            list = new int[][]{
                {row + 1, col}, {row - 1, col}, {row, col + 1}
            };
        } // right side
        else if (col == this.grid_size) {
            list = new int[][]{
                {row + 1, col}, {row - 1, col}, {row, col - 1}
            };
        } // bottom side
        else if (row == this.grid_size) {
            list = new int[][]{
                {row, col + 1}, {row, col - 1}, {row - 1, col}
            };
        } // inside
        else {
            list = new int[][]{
                {row, col + 1}, {row, col - 1}, {row - 1, col}, {row + 1, col}
            };
        }
        this.open_neighbor(list, row, col);
    }

    private void checkbound(int row, int col) {
        if (row <= 0 || row > this.grid_size) {
            throw new IllegalArgumentException("row index row out of bounds");
        }
        if (col <= 0 || col > this.grid_size) {
            throw new IllegalArgumentException("column index col out of bounds");
        }
    }

    public boolean isOpen(int row, int col) {  // is site (row, col) open?
        this.checkbound(row, col);
        return this.grid[row - 1][col - 1];
    }

    public boolean isFull(int row, int col) {  // is site (row, col) full?
        this.checkbound(row, col);
        return this.unionUF.connected(this.index_convertor(row, col), this.upper_dummy) && this.isOpen(row, col)
                && this.openUF.connected(this.index_convertor(row, col), this.upper_dummy);
    }

    public int numberOfOpenSites() {       // number of open sites
        return this.open_size_num;
    }

    public boolean percolates() {              // does the system percolate?
        if(this.grid_size == 1)
            return this.unionUF.connected(this.upper_dummy, this.bottom_dummy) && this.isOpen(grid_size, grid_size);
        else
            return this.unionUF.connected(this.upper_dummy, this.bottom_dummy);
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here

        Percolation p = new Percolation(1);
        //p.open(1, 1);
        System.out.println(p.percolates());
    }

}
