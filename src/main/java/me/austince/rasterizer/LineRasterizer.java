package me.austince.rasterizer;
//
//  rasterizer.LineRasterizer.java
//

//  Created by Joe Geigel on 1/21/10.
//  Copyright 2010 Rochester Institute of Technology. All rights reserved.
//
//  Contributor:  Austin Cawley-Edwards
//

import me.austince.canvas.SimpleCanvas;

/**
 * A simple class for performing rasterization algorithms for lines.
 */
public class LineRasterizer {

    ///
    // number of scanlines
    ///
    private int nScanlines;

    /**
     *
     * @param numScanlines number of scanlines
     */
    public LineRasterizer(int numScanlines) {
        this.nScanlines = numScanlines;
    }

    /**
     *
     * @param x constant x
     * @param y0 start y
     * @param y1 end y
     * @param canvas canvas to draw on
     */
    private void drawVertLine(int x, int y0, int y1, SimpleCanvas canvas) {
        // Always draw from bottom to top
        if (y0 > y1) {
            int temp = y0;
            y0 = y1;
            y1 = temp;
        }

        for (int y = y0; y <= y1; y++) {
            canvas.setPixel(x, y);
        }
    }

    /**
     * A straightforward way to draw horizontal lines
     * @param x0 start x
     * @param x1 end x
     * @param y constant y
     * @param canvas
     */
    private void drawHorizLine(int x0, int x1, int y, SimpleCanvas canvas) {
        // Always draw from left to right
        if (x0 > x1) {
            int temp = x0;
            x0 = x1;
            x1 = temp;
        }

        for (int x = x0; x <= x1; x++) {
            canvas.setPixel(x, y);
        }
    }

    /**
     * For lines with abs slope of 1 only! You will cause an infinite loop otherwise.
     * @param x0 start x
     * @param y0 start y
     * @param x1 end x
     * @param y1 end y
     * @param canvas canvas to draw on
     */
    private void drawDiagLine(int x0, int y0, int x1, int y1, SimpleCanvas canvas) {
        // Increment x and y by the sign of their change direction
        int xInc = (x1 - x0) < 0 ? -1 : 1;
        int yInc = (y1 - y0) < 0 ? -1 : 1;

        int x = x0;
        int y = y0;
        // Doesn't matter which way we draw, they'll end up here eventually
        while (x != x1 && y != y1) {
            canvas.setPixel(x, y);
            x += xInc;
            y += yInc;
        }
    }

    /**
     * Mirrors a point to all 8 octants for a circle drawing
     * Translates to a point on the canvas using a center x and center y
     * @param x first octant x
     * @param y first octant y
     * @param cx the center x of the circle
     * @param cy the center y of the circle
     * @param canvas
     */
    private void drawAllEightPoints(int x, int y, int cx, int cy, SimpleCanvas canvas) {
        // First quadrant
        canvas.setPixel(x + cx, y + cy);
        canvas.setPixel(y + cx, x  + cy);
        // Second
        canvas.setPixel(y + cx, -x + cy);
        canvas.setPixel(x + cx, -y + cy);
        // Third
        canvas.setPixel(-x + cx, -y + cy);
        canvas.setPixel(-y + cx, -x + cy);
        // Fourth
        canvas.setPixel(-x + cx, y + cy);
        canvas.setPixel(-y + cx, x + cy);
    }


    /**
     * Draw a circle with center (cx, cy) and radius r
     * WILL CRASH IF POINTS ARE OUTSIDE OF CANVAS
     * @param cx center x
     * @param cy center y
     * @param r radius
     * @param canvas canvas to draw on
     */
    public void drawCircle(int cx, int cy, int r, SimpleCanvas canvas) {
        // Calculates with the Second Order Integer Implementation
        //  of the Bresenham Midpoint Algorithm
        int x = 0;
        int y = r;
        int h = 1 - r;
        int dE = 3;
        int dSE = 5 - (r * 2);
        this.drawAllEightPoints(x, y, cx, cy, canvas);
        while (y > x) {
            if (h < 0) {
                h += dE;
                dSE += 2;
                x += 1;
            } else {
                h += dSE;
                dSE += 4;
                x += 1;
                y -= 1;
            }
            dE += 2;
            this.drawAllEightPoints(x, y, cx, cy, canvas);
        }
    }

    /**
     * Draw a line from (x0,y0) to (x1, y1) on the canvas.SimpleCanvas C.
     *
     * Implementation should be using the Midpoint Method
     *
     * You are to add the implementation here using only calls
     * to canvas.setPixel()
     * @param x0 x coord of first endpoint
     * @param y0 y coord of first endpoint
     * @param x1 x coord of second endpoint
     * @param y1 y coord of second endpoint
     * @param canvas  The canvas on which to apply the draw command.
     */
    public void drawLine(int x0, int y0, int x1, int y1, SimpleCanvas canvas) {
        // NO DIVISION, all formulas are solved using multiplication instead

        // More efficient ways to do some
        // Don't bother with fancy algorithms for vertical, horizontal, or diagonal lines
        if (x0 == x1) {
//            System.out.format("VERTICAL line: (%d, %d) to (%d, %d)\n", x0, y0, x1, y1);
            this.drawVertLine(x0, y0, y1, canvas);
        } else if (y0 == y1) {
//            System.out.format("HORIZONTAL line: (%d, %d) to (%d, %d)\n", x0, y0, x1, y1);
            this.drawHorizLine(x0, x1, y0, canvas);
        } else if (Math.abs(x1 - x0) == Math.abs(y1 - y0)) {
//            System.out.format("DIAGONAL line: (%d, %d) to (%d, %d)\n", x0, y0, x1, y1);
            this.drawDiagLine(x0, y0, x1, y1, canvas);
        } else {
//            System.out.format("Drawing line: (%d, %d) to (%d, %d)\n", x0, y0, x1, y1);
            // Always draw from left to right
            if (x0 > x1) {
                // swap xs
                int temp = x0;
                x0 = x1;
                x1 = temp;

                // swap ys
                temp = y0;
                y0 = y1;
                y1 = temp;
            }

            // Bress midpoint
            // Definitely possible to condense

            // Slope needs to be double, as slopes less than 1 are rounded to 0
            int dx = x1 - x0;
            int dy = y1 - y0;

            double m = (double) dy / (double) dx;
            int sign = m < 0 ? -1 : 1;

            // Make dx and dy positive, use the slope to infer
            dx = Math.abs(dx);
            dy = Math.abs(dy);

            if (Math.abs(m) > 1) {
                // Y PRIMARY
                // Increment up the values of y

                int dN = 2 * dx; // Change North
                int dNE = 2 * (dx - dy); // Change NorthEast or SouthEast if negative
                int decider = dN - dy; // (2 * dx) - dy;
                // If negative, start from the highest x
                // Otherwise, low !
                int x = m > 0 ? Math.min(x0, x1) : Math.max(x0, x1);

                // Always draw from low to high
                if (y1 < y0) {
                    int temp = y1;
                    y1 = y0;
                    y0 = temp;
                }

                for (int y = y0; y <= y1; y++) {
//                    System.out.format("Printed: (%d, %d)\n", x, y);
                    canvas.setPixel(x, y);
                    if (decider <= 0) {
                        // Choose N
                        decider += dN;
                    } else {
                        // Choose NE
                        decider += dNE;
                        x += sign;
                    }
                }
            } else {
                // X PRIMARY
                // Increment values of x

                int dE = 2 * dy;
                int dNE = 2 * (dy - dx); // South East if Negative slope
                int decider = dE - dx;//(2 * dy) - dx;
                int y = y0;

                for (int x = x0; x <= x1; x++) {
                    canvas.setPixel(x, y);
                    if (decider <= 0) {
                        // Choose E
                        // Don't increment y
                        decider += dE;
                    } else {
                        // Choose NE
                        // or SE if sign is negative
                        decider += dNE;
                        y += sign;
                    }
                }
            }
        }
    }
}




