package me.austince.polyshapes;

import java.awt.*;

/**
 * Created by austin on 3/7/17.
 */
public class PolyRectangle extends PolyShape {

    public PolyRectangle(int width, int height) {
        super(new Point[] {
                new Point(0, 0),
                new Point(width, 0),
                new Point(width, height),
                new Point(0, height)
        });
    }

    public int getMaxX() {
        return this.getVerticies()[1].x;
    }

    public int getMinX() {
        return this.getVerticies()[0].x;
    }

    public int getMaxY() {
        return this.getVerticies()[2].y;
    }

    public int getMinY() {
        return this.getVerticies()[0].y;
    }
}
