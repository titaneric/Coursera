/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package point;

/**
 *
 * @author titan
 */
import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.In;
import java.util.ListIterator;

public class BruteCollinearPoints {

    private class EndPoints {

        public Point p;
        public Point q;

        public EndPoints(Point x, Point y) {
            this.p = x;
            this.q = y;
        }
    }

    final private Point[] points;
    private LineSegment[] segment_array;

    public BruteCollinearPoints(Point[] point_array) {
        if (point_array == null) {
            throw new IllegalArgumentException();
        }
        this.points = point_array.clone();
        if (this.checkPointsNull() || this.checkPointsDuplicate()) {
            throw new IllegalArgumentException();
        }
        this.findSegments();
    }

    private boolean checkPointsNull() {
        for (Point p : this.points) {
            if (p == null) {
                return true;
            }
        }
        return false;
    }

    private boolean checkPointsDuplicate() {
        Arrays.sort(this.points);
        for (int i = 0; i < this.points.length - 1; i++) {
            Point cur_p = this.points[i];
            Point next_p = this.points[i + 1];
            if (cur_p.compareTo(next_p) == 0) {
                return true;
            }
        }
        return false;
    }

    public int numberOfSegments() {
        return this.segment_array.length;
    }

    private void findSegments() {
        ArrayList<EndPoints> segment_list = new ArrayList<EndPoints>();
        ArrayList<Point[]> comb_points = this.getCombination(this.points, 4);
        for (Object[] sub_points : comb_points) {
            Arrays.sort(sub_points);
            // four points: p, q, r, s
            Point p = (Point) sub_points[0];
            Point q = (Point) sub_points[1];
            Point r = (Point) sub_points[2];

            double pq_slope = p.slopeTo(q);
            double pr_slope = p.slopeTo(r);

            if (pq_slope == pr_slope) {
                Point s = (Point) sub_points[3];
                double ps_slope = p.slopeTo(s);
                if (pr_slope == ps_slope) {
                    segment_list = this.updateSegmentSet(segment_list, p, s);
                }
            }
        }
        this.segment_array = new LineSegment[segment_list.size()];
        ListIterator<EndPoints> it = segment_list.listIterator();
        while (it.hasNext()) {
            int i = it.nextIndex();
            EndPoints point = it.next();
            this.segment_array[i] = new LineSegment(point.p, point.q);
        }
    }

    public LineSegment[] segments() {
        return this.segment_array.clone();
    }

    private ArrayList<EndPoints> updateSegmentSet(ArrayList<EndPoints> segment_list, Point p, Point q) {
        for (EndPoints segment : new ArrayList<EndPoints>(segment_list)) {
            Point[] p_array;
            Point left_p = segment.p;
            Point right_p = segment.q;

            // find the point that collinear with end_p
            if (left_p.slopeTo(q) == left_p.slopeTo(right_p)) {
                if (left_p.slopeTo(p) == left_p.slopeTo(q)) {
                    p_array = new Point[]{left_p, right_p, p, q};
                    Arrays.sort(p_array);
                    segment_list.remove(segment);
                    segment_list.add(new EndPoints(p_array[0], p_array[p_array.length - 1]));
                    return segment_list;
                } else if (left_p == p) {
                    return segment_list;
                }
            }

        }
        segment_list.add(new EndPoints(p, q));
        return segment_list;
    }


    /*Revise and reference from Kodin at 
        https://stackoverflow.com/questions/29910312/algorithm-to-get-all-the-combinations-of-size-n-from-an-array-java*/
    private <T> ArrayList<T[]> getCombination(T[] list, int k) {
        ArrayList<T[]> segment_list = new ArrayList<T[]>();
        int[] s = new int[k];
        if (k <= list.length) {
            // first index sequence: 0, 1, 2, ...
            for (int i = 0; (s[i] = i) < k - 1; i++);
            segment_list.add(this.getSubset(list, s));
            for (;;) {
                int i;
                // find position of item that can be incremented
                for (i = k - 1; i >= 0 && s[i] == list.length - k + i; i--);
                if (i < 0) {
                    break;
                }
                s[i]++;                    // increment this item
                for (++i; i < k; i++) {    // fill up remaining items
                    s[i] = s[i - 1] + 1;
                }
                segment_list.add(this.getSubset(list, s));
            }
        }

        return segment_list;
    }

    private <T> T[] getSubset(T[] list, int[] s) {
        // generate actual subset by index sequence
        T[] result = (T[]) new Object[s.length];
        for (int i = 0; i < s.length; i++) {
            result[i] = list[s[i]];
        }
        return result;

    }

    public static void main(String[] args) {

        // read the n points from a file
        String f = "C:\\Users\\titan\\Documents\\Coursera\\Algorithm I\\CollinearPoints\\src\\point\\collinear\\input10.txt";
        In in = new In(f);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            try {
                int x = in.readInt();
                int y = in.readInt();
                points[i] = new Point(x, y);
            } catch (java.util.InputMismatchException e) {
                points[i] = null;
            }
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
//        for (Point p : points) {
//            p.draw();
//        }
//        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
