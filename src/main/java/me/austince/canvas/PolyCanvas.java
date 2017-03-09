package me.austince.canvas;
//  extendedCanvas.java
//
//  Created by Srinivas Sridharan on 2/17/2017.
//  Copyright 2017 Stevens Institute of Technology. All rights reserved.
//
//  This file should not be modified by students.
//

import me.austince.clipper.PolygonClipper;
import me.austince.rasterizer.LineRasterizer;
import me.austince.rasterizer.PolyRasterizer;
import me.austince.polyshapes.PolyShape;

import java.awt.*;
import java.util.LinkedList;

/**
 * This is a special subclass of simpleCanvas with functionality
 * for testing out the clipping assignment.
 */
public class PolyCanvas extends SimpleCanvas {
    private LinkedList<PolyShape> polyFillList;
    private final PolygonClipper polyClipper;
    private final PolyRasterizer polyRasterizer;
    private final LineRasterizer lineRasterizer;
    private Color clipWindowColor;
    private boolean clipWindowShowing = false;

    public PolyCanvas() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public PolyCanvas(int w, int h) {
        super(w, h);

        this.clipWindowColor = Color.WHITE;
        this.polyRasterizer = new PolyRasterizer(this.getHeight());
        this.lineRasterizer = new LineRasterizer(this.getHeight());
        this.polyClipper = new PolygonClipper(0, 0, this.getWidth() - 1, this.getHeight() - 1);
        this.polyFillList = new LinkedList<>();
    }

    public void setClipperBounds(int llx, int lly, int urx, int ury) {
        this.polyClipper.setBounds(llx, lly, urx, ury);
    }

    public void setClipperBounds(Rectangle rect) {
        this.setClipperBounds(rect.x, rect.y, rect.width, rect.height);
    }

    public boolean isClipWindowShowing() {
        return this.clipWindowShowing;
    }

    public void setClipWindowShowing(boolean show) {
        this.clipWindowShowing = show;
    }

    public void addPolyShape(PolyShape poly) {
        this.polyFillList.add(poly);
    }

    public boolean removePolyShape(PolyShape poly) {
        return this.polyFillList.remove(poly);
    }

    @Override
    public void paint(Graphics g) {
        // Pipeline: polygons -> clipper -> rasterizer
        // Update the image with the polygons and clip window

        // draw polys
        for (PolyShape shape : this.polyFillList) {
            this.setCurColor(shape.color);
            Point[] clippedPoints = this.polyClipper.clipPolygon(shape);
            this.polyRasterizer.drawPolygon(clippedPoints, this);
        }

        if (this.clipWindowShowing) {
            this.setCurColor(this.clipWindowColor);
            // Top
            this.lineRasterizer.drawLine(
                    polyClipper.llx, polyClipper.ury,
                    polyClipper.urx, polyClipper.ury,
                    this
            );
            // Right
            this.lineRasterizer.drawLine(
                    polyClipper.urx, polyClipper.ury,
                    polyClipper.urx, polyClipper.lly,
                    this
            );
            // Bottom
            this.lineRasterizer.drawLine(
                    polyClipper.llx, polyClipper.lly,
                    polyClipper.urx, polyClipper.lly,
                    this
                    );
            // Left
            this.lineRasterizer.drawLine(
                    polyClipper.llx, polyClipper.lly,
                    polyClipper.llx, polyClipper.ury,
                    this
            );
        }

        // then do normal painting
        super.paint(g);
    }

}
