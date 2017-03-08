/**
 * @file rasterizer.PolyRasterizer
 * @author Srinivas Sridharan
 * @author Austin Cawley-Edwards <acawleye@stevens.edu>
 * @since 2/10/17
 * @copyright 2017 Stevens Institute of Technology. All rights reserved.
 */
package me.austince.rasterizer;

import me.austince.canvas.SimpleCanvas;

import java.awt.*;
import java.util.ArrayList;

/**
 * This is a class that performs rasterization algorithms for polygons.
 */
public class PolyRasterizer {

    /**
     * number of scanlines
     */
    private int nScanlines;

    /**
     *
     * @param numScanlines number of scanlines
     */
    public PolyRasterizer (int numScanlines) {
        this.nScanlines = numScanlines;
    }

    /**
     *  Draw a filled polygon in the simpleCanvas C.
     *  // The polygon has n distinct vertices. The
     // coordinates of the vertices making up the polygon are stored in the
     // x and y arrays.  The ith vertex will have coordinate  (x[i], y[i])
     // You are to add the implementation here using only calls
     // to C.setPixel()
     * @param vertices a list of verticies
     * @param canvas canvas to draw on
     */
    public void drawPolygon(Point[] vertices, SimpleCanvas canvas) {

        // ET
        ArrayList<EdgeBucket>[] edgeTable = this.buildEdgeTable(vertices);
        // AEL
        ArrayList<EdgeBucket> activeEdgeList = new ArrayList<>();

        // Count up from 0 to each scanline
        // Could be made more efficient by iterating from bounding yMin to yMax
        for (int y = 0; y < this.nScanlines; y++) {

            // Discard AEL entries where y = yMax
            for (int i = 0; i < activeEdgeList.size(); i++) {
                EdgeBucket bucket = activeEdgeList.get(i);
                if (y == bucket.yMax) {
                    activeEdgeList.remove(bucket);
                    i -= 1; // Decrement since the size has changed
                }
            }

            // Move from ET[y] to AEL when yMin = y
            ArrayList<EdgeBucket> scanBucketList = edgeTable[y];
            if (scanBucketList != null) {
                // yMin = y, so add to AEL
                activeEdgeList.addAll(scanBucketList);
            }

            // Sort within AEL
            // Primary sort by x
            // Secondary sort by 1/m
            activeEdgeList.sort((EdgeBucket b1, EdgeBucket b2) -> {
                if (b1.x == b2.x) {
                    // if m is large, 1 / m is small
                    return (b1.getSlope() > b2.getSlope()) ? 1 : 0;
                }

                return (b1.x < b2.x) ? 1 : 0;
            });

            // Fill pixels on scan line y using pairs of x coords from AEL
            for (int i = 0; i < activeEdgeList.size() - 1; i++) {
                drawHorizontalLine(
                        activeEdgeList.get(i).getX(),
                        activeEdgeList.get(i + 1).getX(),
                        y,
                        canvas
                );
            }


            // For each non-vertical edge in AEL
            for(EdgeBucket bucket : activeEdgeList) {
                if (!bucket.isVertical()) {
                    bucket.increment();
                }
            }

        }
    }

    private static void drawHorizontalLine(int x1, int x2, int y, SimpleCanvas canvas) {
        // Always draw left to right
        if (x2 < x1) {
            int temp = x2;
            x2 = x1;
            x1 = temp;
        }

        for (int x = x1; x < x2; x++) {
            canvas.setPixel(x, y);
        }
    }

    private ArrayList<EdgeBucket>[] buildEdgeTable(Point[] vertices) {
        // Should only allocate enough space for yMax - yMin, but that'll be for later
        ArrayList<EdgeBucket>[] edgeTable = new ArrayList[this.nScanlines];
        int numVerts = vertices.length;
        // Build from lowest y to highest
        for (int i = 0; i < numVerts; i++) {
//            edgeTable.add(new EdgeBucket(xs[i], ));
            Point startV = vertices[i];
            Point endV;

            if (i == numVerts - 1) {
                // Wrap around for second vertex if we've reached the end
                endV = vertices[0];
            } else {
                endV = vertices[i+1];
            }

            // Always position vertices from left to right
            if (endV.getX() < startV.getX()) {
                // swap
                Point temp = startV;
                startV = endV;
                endV = temp;
            }

            // Calculate the bucket contents
            int dy = endV.y - startV.y;
            // Ignore horizontal edges
            if (dy == 0) {
                continue;
            }

            int dx = endV.x - startV.x;
            // x is the x of the yMin
            int yMax, yMin, x;
            if (startV.y >= endV.y) {
                yMin = endV.y;
                yMax = startV.y;
                x = endV.x;
            } else {
                yMin = startV.y;
                yMax = endV.y;
                x = startV.x;
            }

            EdgeBucket newBucket = new EdgeBucket(yMax, x, dx, dy);
            ArrayList<EdgeBucket> scanBucketList = edgeTable[yMin];

            // Create a new scanline list if none exists
            if (scanBucketList != null) {
                scanBucketList.add(newBucket);
            } else {
                edgeTable[yMin] = new ArrayList<>();
                edgeTable[yMin].add(newBucket);
            }
        }

        return edgeTable;
    }


    /**
     * Custom class to represent the contents of a bucket
     */
    private class EdgeBucket {
        private int yMax;
        private int x;
        private int dx;
        private int dy;
        private int sum;
        private double slope;
        // No 'next' necessary, handling with an ArrayList

        private EdgeBucket(int yMax, int x, int dx, int dy) {
            this.yMax = yMax;
            this.x = x;
            this.dx = dx;
            this.dy = dy;
            this.sum = 0;
            this.slope = dx == 0 ? 0.0 : (double) this.dy / (double) this.dx;
            // More efficient to just store the slope
        }

        private double getSlope() {
            return this.slope;
        }

        private int getX() {
            return this.x;
        }

        private boolean isVertical() {
            return this.dx == 0;
        }

        private void increment() {
            //      increment sum
            //      if sum >= dy, adjust x and sum
            this.sum += this.dx;

            while (this.sum >= Math.abs(this.dy)) {
                this.x += (this.getSlope() < 0) ? -1 : 1;
                this.sum -= Math.abs(this.dy);
            }
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof EdgeBucket)) {
                return false;
            }
            EdgeBucket otherBucket = (EdgeBucket) obj;
            return this.yMax == otherBucket.yMax &&
                    this.sum == otherBucket.sum &&
                    this.dx == otherBucket.dx &&
                    this.dy == otherBucket.dy &&
                    this.x == otherBucket.x;
        }
    }
}
