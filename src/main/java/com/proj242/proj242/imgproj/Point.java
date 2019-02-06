package com.proj242.proj242.imgproj;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.StringTokenizer;

class Point implements Comparable<Point> {
    int x, y;

    public int compareTo(Point p) {
        if (this.x == p.x) {
            return this.y - p.y;
        } else {
            return this.x - p.x;
        }
    }

    public String toString() {
        return "("+x + "," + y+")";
    }

}