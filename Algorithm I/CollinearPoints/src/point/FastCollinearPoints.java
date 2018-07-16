/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ListIterator;

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
    private class Pair implements Comparable<Pair>{
        public final int index;
        public final double value;
        
        public Pair(int index, double value){
            this.index = index;
            this.value = value;
        }
        @Override
        public int compareTo(Pair other){
            if(this.value < other.value){
                return -1;
            }
            else if(this.value > other.value){
                return 1;
            }
            else{
                return 0;
            }
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
        ArrayList<EndPoints> segment_list = new ArrayList<EndPoints>();
        ArrayList<Point> reference_list = new ArrayList<Point>(Arrays.asList(this.points));
        while(!reference_list.isEmpty()) {
            ArrayList<Point> other_p_list = new ArrayList<Point>(reference_list);
            
            reference_list.remove(0);
            Point p = other_p_list.get(0);
            other_p_list.remove(0);
            
            Pair[] slope_array = new Pair[other_p_list.size()];
            for(int i=0;i<slope_array.length;i++){
                slope_array[i] = new Pair(i, p.slopeTo(other_p_list.get(i)));
            }
            Arrays.sort(slope_array);
            int stride = 1;
            for(int i=0;i<slope_array.length;i+=stride){
                stride = 1;
                ArrayList<Point> p_list = new ArrayList<Point>();
                double q_slope = slope_array[i].value;
                p_list.add(p);
                p_list.add(other_p_list.get(slope_array[i].index));
                
                while(i + stride < other_p_list.size()){
                    Pair p_pair = slope_array[i + stride];
                    double r_slope = p_pair.value;
                    if(q_slope == r_slope){
                        p_list.add(other_p_list.get(p_pair.index));
                        stride++;
                    }
                    else{
                        break;
                    }
                }
                
                if (p_list.size() >= 4) {
                    Point[] p_array = new Point[p_list.size()];
                    p_array = p_list.toArray(p_array);
                    Arrays.sort(p_array);
                    segment_list = this.updateSegmentSet(segment_list, p_array[0], p_array[p_array.length - 1]);
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

    public LineSegment[] segments() { // the line segments
        return this.segment_array.clone();
    }

    public static void main(String[] args) {
        // read the n points from a file
        String f = "C:\\Users\\titan\\Documents\\Coursera\\Algorithm I\\CollinearPoints\\src\\point\\collinear\\input8.txt";
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
