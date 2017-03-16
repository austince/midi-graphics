package me.austince.polyshapes;

import Jama.Matrix;
import org.openimaj.math.geometry.transforms.TransformUtilities;

import java.awt.*;

/**
 * Created by austin on 3/7/17.
 */
public class PolyShape {
    private Matrix vertices;
    public Color color;

    public PolyShape() {

    }

    public PolyShape(Point[] vertices) {
        setVertices(vertices);
    }

    /**
     * Sets vertices by a set of Points
     * @param vertices
     */
    public void setVertices(Point[] vertices) {
        double[][] vertMatrix = new double[vertices.length][2];
        for (int i = 0; i < vertices.length; i++) {
            vertMatrix[i][0] = vertices[i].getX();
            vertMatrix[i][1] = vertices[i].getY();
        }
        this.vertices = new Matrix(vertMatrix);
    }

    /**
     *
     * @return the vertices represented as points
     */
    public Point[] getPointVerticies() {
        double[][] vertMatrix = this.vertices.getArray();
        Point[] vertices = new Point[vertMatrix.length];
        for (int v = 0; v < vertMatrix.length; v++) {
            vertices[v] = new Point((int) vertMatrix[v][0], (int) vertMatrix[v][1]);
        }
        return vertices;
    }

    public void setCenter(Point newCenter) {
        // TODO
        // Translate vertices around new center
        translate(newCenter);
    }

    public Point getCenter() {
        Rectangle bounds = getBounds();
        return new Point(bounds.x + bounds.width / 2, bounds.y + bounds.height / 2);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public Point vectorFromCenter(Point point) {
        return new Point(this.getCenter().x - point.x, this.getCenter().y - point.y);
    }

    public void translate(Point adjVec) {
        for (double[] vert : this.vertices.getArray()) {
            vert[0] += adjVec.getX();
            vert[1] += adjVec.getY();
        }
    }

    public void rotate(double rad) {
        Matrix rotMatrix = TransformUtilities.rotationMatrix(rad);
        this.vertices.arrayTimesEquals(rotMatrix);
    }

    public void rotateAboutCenter(double rad) {
        Point center = this.getCenter();
        Matrix rotMatrix = TransformUtilities.rotationMatrixAboutPoint(rad, center.x, center.y);
        this.vertices.arrayTimesEquals(rotMatrix);
    }

    public void scale(int scalar) {
        scale((double) scalar);
    }

    public void scale(double scalar) {
        this.vertices.timesEquals(scalar);
    }

    public void scaleAboutCenter(double scalar) {
        Point oldCenter = getCenter();
        this.vertices.timesEquals(scalar);
        setCenter(oldCenter);
    }

    public int[] getXs() {
        int[] xPoints = new int[this.vertices.getArray().length];
        for (int i = 0; i < this.vertices.getArray().length; i++) {
            xPoints[i] = (int) this.vertices.getArray()[i][0];
        }
        return xPoints;
    }

    public int[] getYs() {
        int[] yPoints = new int[this.vertices.getArray().length];
        for (int i = 0; i < this.vertices.getArray().length; i++) {
            yPoints[i] = (int) this.vertices.getArray()[i][1];
        }
        return yPoints;
    }

    public Rectangle getBounds() {
        return this.toPolygon().getBounds();
    }

    public Polygon toPolygon() {
        return new Polygon(this.getXs(), this.getYs(), this.vertices.getArray().length);
    }
}
