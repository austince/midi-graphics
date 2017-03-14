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
    protected static final int DEFAULT_WIDTH = 900;
    protected static final int DEFAULT_HEIGHT = 600;


    private BufferedImage image;
    private int width;
    private int height;
    private Color bgColor;
    private Color curColor;
    private int curRGB;

    public SimpleCanvas() {
        this(DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    public SimpleCanvas(int w, int h) {
        this.width = w;
        this.height = h;
        this.image = new BufferedImage(this.width, this.height, BufferedImage.TYPE_INT_RGB);
        this.curColor = new Color(0.0f, 0.0f, 0.0f);
        this.bgColor = new Color(0);
        this.setSize(w, h);
    }

    public void clear() {
        for (int x = 0; x < this.width; x++)
            for (int y = 0; y < this.height; y++)
                this.image.setRGB(x, y, this.bgColor.getRGB());

    }

    public Color getCurColor() {
        return this.curColor;
    }

    public void setCurColor(Color color) {
        this.curColor = color;
        this.curRGB = color.getRGB();
    }

    public void setCurColor(float r, float g, float b) {
        this.curColor = new Color(r, g, b);
    }

    public void setPixel(int x, int y) {
        int curPixelRGB = this.image.getRGB(x, (this.getHeight() - y - 1));
        if (this.curRGB != curPixelRGB)
            this.image.setRGB(x, (this.getHeight() - y - 1), this.curRGB);
    }

    @Override
    public void paint(Graphics g) {
        // draw pixels
//        g.drawImage(this.image, 0, 0, null);
//        g.drawImage(this.image, 0, 0, this.bgColor, null);

        Graphics2D g2 = (Graphics2D) g;
        g2.drawImage(this.image, 0, 0, null);
    }
}
