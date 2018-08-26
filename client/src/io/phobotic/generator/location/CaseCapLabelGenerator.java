package io.phobotic.generator.location;


import com.sun.istack.internal.NotNull;
import io.phobotic.generator.LabelGenerator;
import io.phobotic.label.Printable;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.io.File;
import java.io.IOException;

/**
 * Created by Jonathan Nelson on 3/7/16.
 */
public class CaseCapLabelGenerator implements LabelGenerator {
    double labelWidth;
    double labelHeight;
    private Font mFont;
    private Graphics2D g2d;
//    private CappedProduct product;

    public CaseCapLabelGenerator() {
        mFont = new Font("Monaco", Font.TRUETYPE_FONT, 8);

    }

    public Font getFont() {
        return mFont;
    }

    public void setFont(@NotNull Font font) {
        mFont = font;
    }

    public Graphics2D generate(Graphics2D g2d, PageFormat pageFormat, Printable printable) {
//        if (!(printable instanceof CappedProduct)) {
//            throw new ClassCastException("Unable to cast printable to " + CappedProduct.class.getName());
//        }
//        this.product = (CappedProduct) printable;

        this.g2d = g2d;
        Color oldColor = g2d.getColor();
        labelHeight = g2d.getClipBounds().height;
        labelWidth = g2d.getClipBounds().width;

        Font oldFont = g2d.getFont();
        g2d.setFont(mFont);
        g2d.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        int fontHeight = mFont.getSize();
        g2d.setColor(Color.white);
        g2d.fillRect(0, 0, (int) labelWidth, (int) labelHeight);
        g2d.setColor(Color.red);
        writeImage("0");
        drawLocation();
        writeImage("1");
        drawStackHeight();
        writeImage("2");
        drawStackWidth();
        writeImage("3");
        drawStackDepth();
        writeImage("4");

        g2d.setFont(oldFont);

        return g2d;
    }

    private void writeImage(String name) {
        BufferedImage img = new BufferedImage((int) labelWidth, (int) labelHeight, BufferedImage.TYPE_INT_RGB);
        g2d.drawImage(img, null, 0, 0);
        try {
            ImageIO.write(img, "JPEG", new File("/tmp/" + name + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void drawLocation() {
        g2d.translate(10, 10);
//        g2d.drawString(product.getLocation(), 0, 0);
    }

    private void drawStackHeight() {
        g2d.translate(labelWidth - 20, 20);
//        g2d.drawString(String.valueOf(product.getNewCaseCap().getStackHeight()), 0, 0);
        int startX = (int) labelWidth - 5;
        int startY = (int) labelHeight - 5;
        int endX = (int) labelWidth - 5;
        int endY = 10;
        g2d.drawLine(startX, startY, endX, endY);
        //todo draw line arrow
    }

    private void drawStackWidth() {
        g2d.translate(20, labelHeight - 20);
//        g2d.drawString(String.valueOf(product.getNewCaseCap().getStackWidth()), 0, 0);
        int startX = (int) labelWidth - 5;
        int startY = (int) labelHeight - 5;
        int endX = 20;
        int endY = (int) labelHeight - 5;
        g2d.drawLine(startX, startY, endX, endY);
        //todo draw line arrow
    }

    private void drawStackDepth() {
        g2d.translate(labelWidth / 2, labelHeight / 2);
//        g2d.drawString(String.valueOf(product.getNewCaseCap().getStackDepth()), 0, 0);
        int distanceUp = ((int) labelHeight - 5 - 5) / 2;
        int distanceOver = distanceUp;
        int startX = (int) labelWidth - 5;
        int startY = (int) labelHeight - 5;
        int endX = startX - distanceUp;
        int endY = startY - distanceOver;
        Stroke oldStroke = g2d.getStroke();
        float[] dash = {10.0f};
        BasicStroke dashedStroke = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f);
        g2d.setStroke(dashedStroke);
        g2d.drawLine(startX, startY, endX, endY);
        g2d.setStroke(oldStroke);
        //todo draw line arrow
    }
}
