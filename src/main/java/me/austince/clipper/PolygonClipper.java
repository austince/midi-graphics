/**
 * @file clipper.java
 * @author: Srinivas
 * @contributor: Austin Cawley-Edwards <acawleye@stevens.edu>
 * @since Feb 17, 2017
 */
package me.austince.clipper;

import me.austince.polyshapes.PolyShape;

import java.awt.*;
import java.util.ArrayList;

/**
 * Object to perform clipping
 */
public class PolygonClipper {

    /**
     * Stores the upper corner of the clip window
     */
    public int urx, ury;

    /**
     * Stores the lower corner of the clip window
     */
    public int llx, lly;

    /**
     * To determine which side of the clipping window is currently being clipped against
     * An enum helps if the way the clip window is represented changes
     */
    enum EdgeSide {
        RIGHT,
        LEFT,
        TOP,
        BOTTOM
    }

    /**
     * Empty constructor
     */
    public PolygonClipper() {
        this(0, 0, 0, 0);
    }

    /**
     * @param llx lower left x
     * @param lly lower left y
     * @param urx upper right x
     * @param ury upper right y
     */
    public PolygonClipper(int llx, int lly, int urx, int ury) {
        this.setBounds(llx, lly, urx, ury);
    }

    /**
     * @param llx lower left x
     * @param lly lower left y
     * @param urx upper right x
     * @param ury upper right y
     */
    public void setBounds(int llx, int lly, int urx, int ury) {
        this.llx = llx;
        this.lly = lly;
        this.ury = ury;
        this.urx = urx;
    }

    public Rectangle getBounds() {
        return new Rectangle(llx, lly, urx, ury);
    }

    public Point[] getPointBounds() {
        return new Point[]{
                new Point(llx, lly),
                new Point(urx, ury)
        };
    }

    /**
     * Assumes COUNTER CLOCKWISE edge iteration
     * Uses the clip window corners to determine
     *
     * @param edgeSide Side of the clipping window
     * @param point    point
     * @return whether its in or out
     */
    private boolean isInside(EdgeSide edgeSide, Point point) {
        // Use the assumption that the clip window is always rectangular
        // check, counter clockwise, which way the clip
        switch (edgeSide) {
            case LEFT:
                return point.getX() > llx;
            case RIGHT:
                return point.getX() < urx;
            case TOP:
                return point.getY() < ury;
            case BOTTOM:
                return point.getY() > lly;
            default:
                throw new Error("Edge side not found.");
        }
    }

    /**
     * Uses a modified similar triangle method
     *
     * @param segStart  segment start point
     * @param segEnd    segment end point
     * @param clipStart clip edge start point
     * @param clipEnd   clip edge end point
     * @return a point that intersects
     */
    private Point findIntersection(Point segStart, Point segEnd, Point clipStart, Point clipEnd) {
        // For both the segment and the clip edge
        // Get the ratio of dy dx along with the offsets x and y
        double segDx = segStart.getX() - segEnd.getX();
        double segDy = segEnd.getY() - segStart.getY();
        double segOffset = segDy * segStart.getX() + segDx * segStart.getY();

        double clipDx = clipStart.getX() - clipEnd.getX();
        double clipDy = clipEnd.getY() - clipStart.getY();
        double clipOffset = clipDy * clipStart.getX() + clipDx * clipStart.getY();

        // Uphold the ratio for x and y of the intersection
        double combinedOffset = segDy * clipDx - segDx * clipDy;
        double iX = (clipDx * segOffset - segDx * clipOffset) / combinedOffset;
        double iY = (segDy * clipOffset - clipDy * segOffset) / combinedOffset;

        return new Point((int) iX, (int) iY);
    }

    /**
     * @param clipStart - x coord of lower left of clipping rectangle.
     * @param clipEnd   - y coord of lower left of clipping rectangle.
     * @return a set of vertices that should be included in the output
     */
    public ArrayList<Point> clipPolygonAgainstEdge(EdgeSide edgeSide, Point clipStart, Point clipEnd, ArrayList<Point> inputPolygon) {
        // Cycle through polygon segments, clipping each one
        ArrayList<Point> outputs = new ArrayList<>();

        // If all points have been identified as outside, input size will be 0, thus return nothing
        int inputSize = inputPolygon.size();
        if (inputSize == 0) {
            return inputPolygon;
        }

        Point pred = inputPolygon.get(inputSize - 1);
        for (Point vertex : inputPolygon) {

            if (isInside(edgeSide, vertex)) {
                if (isInside(edgeSide, pred)) {
                    // Case 1 -> both points are inside == Entire line segment is inside
                    // output just vertex, will get to the pred later
                    outputs.add(vertex);
                } else {
                    // Case 4 -> just the start is inside
                    // Output the vertex and the clipped findIntersection
                    Point intersectPoint = findIntersection(pred, vertex, clipStart, clipEnd);

                    outputs.add(intersectPoint);
                    outputs.add(vertex);
                }
            } else if (isInside(edgeSide, pred)) {
                // Case 2 -> just the vertex is out
                // Output just the clipped findIntersection
                Point intersectPoint = findIntersection(pred, vertex, clipStart, clipEnd);

                outputs.add(intersectPoint);
            } // Case 3 has no output

            // Let the last vertex be the next predecessor
            pred = vertex;
        }

        return outputs;
    }

    public Point[] clipPolygon(PolyShape poly) {
        return clipPolygon(poly.getXs(), poly.getYs());
    }

    /**
     * clipPolygon
     * <p>
     * Clip the polygon with vertex count in and vertices inx/iny
     * against the rectangular clipping region specified by lower-left corner
     * (llx,lly) and upper-right corner (urx,ury). The resulting vertices are
     * placed in outx/outy.
     * <p>
     * The routine should return the the vertex count of the polygon
     * resulting from the clipping.
     *
     * @param inxs - x coords of vertices of polygon to be clipped.
     * @param inys - y coords of vertices of polygon to be clipped.
     * @return number of vertices in the polygon resulting after clipping
     */
    public Point[] clipPolygon(int inxs[], int inys[]) {
        int in = inxs.length;
        ArrayList<Point> inputPolygon = new ArrayList<>(in);
        for (int i = 0; i < in; i++) {
            inputPolygon.add(i, new Point(inxs[i], inys[i]));
        }
        ArrayList<Point> outputs = new ArrayList<>(inputPolygon);

        // Construct a bounding box

        // Counter Clockwise
        // Traverse order:
        // 1 -> left
        // 2 -> top
        // 3 -> right
        // 4 -> bottom
        Point[] clipEdgeVerts = {
                new Point(this.llx, this.ury),
                new Point(this.urx, this.ury),
                new Point(this.urx, this.lly),
                new Point(this.llx, this.lly),
        };

        // Cycle through all edges of that bounding box
        Point edgeStart = clipEdgeVerts[clipEdgeVerts.length - 1];
        for (int edgeIndex = 0; edgeIndex < clipEdgeVerts.length; edgeIndex++) {
            Point edgeEnd = clipEdgeVerts[edgeIndex];

            // Assign the proper enum as we cycle through the clip edges
            EdgeSide side;
            switch (edgeIndex) {
                case 0:
                    side = EdgeSide.LEFT;
                    break;
                case 1:
                    side = EdgeSide.TOP;
                    break;
                case 2:
                    side = EdgeSide.RIGHT;
                    break;
                case 3:
                    side = EdgeSide.BOTTOM;
                    break;
                default:
                    throw new Error("Edges out of bounds.");
            }

            // Constantly update the outputs by using the clipped outputs as inputs for next clip
            outputs = clipPolygonAgainstEdge(side, edgeStart, edgeEnd, outputs);

            if (outputs.size() == 0) {
                // Break early if we have found the entirety of the polygon is outside the clip window
                break;
            }

            edgeStart = edgeEnd;
        }


        Point[] outArr = new Point[outputs.size()];
        for (int i = 0; i < outArr.length; i++) {
            outArr[i] = outputs.get(i);
        }

        return outArr;
    }

}
