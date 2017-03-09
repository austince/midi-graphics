package me.austince.polyshapes;

import java.awt.*;

/**
 * Created by austin on 3/7/17.
 */
public class PolyShape {
    private Point[] verticies;
    private Point center;
    public Color color;

    public PolyShape() {
        this.center = new Point(0, 0);
        this.verticies = new Point[0];
    }

    public PolyShape(Point[] verticies) {
        this.center = new Point(0, 0);
        setVerticies(verticies);
    }

    public void setVerticies(Point[] verticies) {
        this.verticies = verticies.clone();
    }

    public Point[] getVerticies() {
        return verticies;
    }

    public void setCenter(Point newCenter) {
        this.center = newCenter;
        // Translate vertices around new center
        this.translate(newCenter);
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void translate(Point adjVec) {
        for (Point point : this.verticies) {
            point.translate(adjVec.x, adjVec.y);
        }
    }

    public void scale(Point pScale) {
        for (Point point : this.verticies) {
            point.x *= pScale.x;
            point.y *= pScale.y;
        }
    }

    public void scale(int scalar) {
        scale((double) scalar);
    }

    public void scale(double scalar) {
        for (Point point : this.verticies) {
            point.x *= scalar;
            point.y *= scalar;
        }
    }

    public int[] getXs() {
        int[] xPoints = new int[this.verticies.length];
        for (int i = 0; i < xPoints.length; i++) {
            xPoints[i] = this.verticies[i].x;
        }
        return xPoints;
    }

    public int[] getYs() {
        int[] yPoints = new int[this.verticies.length];
        for (int i = 0; i < yPoints.length; i++) {
            yPoints[i] = this.verticies[i].y;
        }
        return yPoints;
    }

    public Polygon toPolygon() {
        return new Polygon(this.getXs(), this.getYs(), this.verticies.length);
    }
}
