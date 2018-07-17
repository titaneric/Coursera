/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;
import java.util.Collections;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

/**
 *
 * @author titan
 */
public class FastCollinearPoints {

    private class EndPoints {

        public Point p;
        public Point q;

        public EndPoints(Point x, Point y) {
            this.p = x;
            this.q = y;
        }
    }

    /* Revise and reference from Zack Zatkin-Gold at
        https://stackoverflow.com/questions/23587314/how-to-sort-an-array-and-keep-track-of-the-index-in-java*/
    private class Pair implements Comparable<Pair> {

        public final int index;
        public final double value;

        public Pair(int index, double value) {
            this.index = index;
            this.value = value;
        }

        @Override
        public int compareTo(Pair other) {
            if (this.value < other.value) {
                return -1;
            } else if (this.value > other.value) {
                return 1;
            } else {
                return 0;
            }
        }
    }

    private class SegmentInfo {

        public ArrayList<Point> points;
        public double slope;

        public SegmentInfo(ArrayList<Point> points, double slope) {
            this.points = points;
            this.slope = slope;
        }
    }

    final private Point[] points;
    private LineSegment[] segment_array;

    public FastCollinearPoints(Point[] point_array) {
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

    public int numberOfSegments() { // the number of line segments
        return this.segment_array.length;
    }

    private void findSegments() {
        ArrayList<Point> reference_list = new ArrayList<Point>(Arrays.asList(this.points));
        ArrayList<SegmentInfo> points_list = new ArrayList<SegmentInfo>();

        while (!reference_list.isEmpty()) {
            ArrayList<Point> other_p_list = new ArrayList<Point>(reference_list);
            ArrayList<Point> filtered_p_list = new ArrayList<Point>();
            reference_list.remove(0);
            Point p = other_p_list.get(0);
            other_p_list.remove(0);

            boolean points_list_not_empty = !points_list.isEmpty();

            ArrayList<Pair> slope_list = new ArrayList<Pair>();
            while (!other_p_list.isEmpty()) {
                int i = filtered_p_list.size();
                Point other_p = other_p_list.get(0);
                other_p_list.remove(0);
                double p_q_slope = p.slopeTo(other_p);
                boolean collinear_state = false;
                if (points_list_not_empty) {
                    // find segment that collinear with p
                    for (SegmentInfo segment : points_list) {
                        // remove the collinear points if found
                        if (this.checkPointsCollinear(segment, p, other_p, p_q_slope)) {
                            for (Point q : segment.points) {
                                other_p_list.remove(q);
                            }
                            collinear_state = true;
                            break;
                        }
                    }
                }
                // add point if didn't find collinear segment or points_list is empty
                if (!collinear_state) {
                    slope_list.add(new Pair(i, p_q_slope));
                    filtered_p_list.add(other_p);
                }
            }
            Collections.sort(slope_list);
            int stride = 1;
            for (int i = 0; i < slope_list.size(); i += stride) {
                stride = 1;
                ArrayList<Point> p_list = new ArrayList<Point>();
                double q_slope = slope_list.get(i).value;
                p_list.add(p);
                p_list.add(filtered_p_list.get(slope_list.get(i).index));

                while (i + stride < filtered_p_list.size()) {
                    Pair p_pair = slope_list.get(i + stride);
                    double r_slope = p_pair.value;
                    if (q_slope == r_slope) {
                        p_list.add(filtered_p_list.get(p_pair.index));
                        stride++;
                    } else {
                        break;
                    }
                }
                /* add to points_list if size of p_list >= 4
                    it is guaranteed that 
                */
                if (p_list.size() >= 4) {
                    Collections.sort(p_list);
                    points_list.add(new SegmentInfo(p_list, q_slope));
                }
            }

        }
        // copy result to segment_array
        this.segment_array = new LineSegment[points_list.size()];
        ListIterator<SegmentInfo> it = points_list.listIterator();
        while (it.hasNext()) {
            int i = it.nextIndex();
            SegmentInfo segment = it.next();
            ArrayList<Point> tmp_points = segment.points;
            this.segment_array[i] = new LineSegment(tmp_points.get(0), tmp_points.get(tmp_points.size()-1));
        }
    }

    private boolean checkPointsCollinear(SegmentInfo segment, Point p, Point q, double p_q_slope) {
        double end_points_slope = segment.slope;
        // check the slope
        if (p_q_slope == end_points_slope) {
            // check p or q existing in points
            for (Point r : segment.points) {
                if (r == p || r == q) {
                    return true;
                }
            }
        }
        return false;
    }

    public LineSegment[] segments() { // the line segments
        return this.segment_array.clone();
    }

    public static void main(String[] args) {
        // read the n points from a file
        String f = "C:\\Users\\titan\\Documents\\Coursera\\Algorithm I\\CollinearPoints\\src\\point\\collinear\\rs1423.txt";
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();

    }
}
