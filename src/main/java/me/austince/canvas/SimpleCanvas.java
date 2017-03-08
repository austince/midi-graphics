package me.austince.canvas;
//
//  canvas.SimpleCanvas.java
//
//  Created by Joe Geigel on 1/21/10.
//  Copyright 2010 Rochester institute of Technology. All rights reserved.
//

///
// This is a simple class to allow pixel level drawing in
// java without using OpenGL
//
// techniques for pixel drawing taken from:
//
//   http://www.cap-lore.com/code/java/JavaPix.java
//
//  with my thanks.
///

import java.awt.*;
import java.awt.image.BufferedImage;

public class SimpleCanvas extends Canvas {
    protected static int DEFAULT_WIDTH = 900;
    protected static int DEFAULT_HEIGHT = 600;


    private BufferedImage image;
    private int width;
    private int height;
    private Color clearColor;
    private Color curColor;

    public SimpleCanvas() {
        this(SimpleCanvas.DEFAULT_WIDTH, SimpleCanvas.DEFAULT_HEIGHT);
    }


    public SimpleCanvas(int w, int h) {
        this.width = w;
        this.height = h;
        this.image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        this.curColor = new Color(0.0f, 0.0f, 0.0f);
        this.clearColor = new Color(0);
        this.setSize(w, h);
    }

    public void clear() {
        for (int i=0; i < this.getWidth(); i++)
            for (int j=0; j < this.getHeight(); j++)
                this.image.setRGB(i, j, this.clearColor.getRGB());
    }

    @Override
    public int getHeight() {
        return height;
    }

    public Color getCurColor() {
        return curColor;
    }

    public void setColor (float r, float g, float b) {
        this.curColor = new Color(r,g, b);
    }

    public void setPixel (int x, int y) {
        this.image.setRGB(x, (height - y - 1), this.curColor.getRGB());
    }

    public void paint(Graphics g) {
        // draw pixels
        g.drawImage(this.image, 0, 0, Color.red, null);
    }

}
