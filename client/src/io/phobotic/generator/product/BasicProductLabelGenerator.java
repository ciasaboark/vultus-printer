package io.phobotic.generator.product;

import com.sun.istack.internal.NotNull;
import io.phobotic.generator.LabelGenerator;
import io.phobotic.message.PrintRequest;
import io.phobotic.label.Printable;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.print.PageFormat;
import java.io.File;
import java.io.IOException;

/**
 * Created by Jonathan Nelson on 2/19/16.
 */
public class BasicProductLabelGenerator implements LabelGenerator {
    private static final int ICON_H_OFFSET = 10; //initial horizontal offset for icon row
    private static final int ICON_V_OFFSET = 40; //vertical offset for icon row (assumes 24px images + 10px padding)
    private static final int ICON_H_PADD = 0;

    private Font mDetailsFont;
    private Font mLocationFont;

    private PrintRequest printRequest;
    private int curIconX = ICON_H_OFFSET;
    private int curIconY;
    private Graphics2D mGraphics2D;
    private PageFormat mPageFormat;

    public BasicProductLabelGenerator() {
        mDetailsFont = new Font("Monaco", Font.TRUETYPE_FONT, 8);
        mLocationFont = new Font("Monaco", Font.TRUETYPE_FONT | Font.BOLD, 12);
    }

    public Font getFont() {
        return mDetailsFont;
    }

    public void setFont(@NotNull Font font) {
        mDetailsFont = font;
    }

    @Override
    public Graphics2D generate(Graphics2D g2d, PageFormat pageFormat, Printable printable) {
        if (!(printable instanceof PrintRequest)) {
            throw new ClassCastException("Unable to cast printable to " + PrintRequest.class.getName());
        }

        printRequest = (PrintRequest) printable;
        mGraphics2D = g2d;
        mPageFormat = pageFormat;

//        log.trace("Label generator for product id: " + printRequest.getProductId());

        Color oldColor = mGraphics2D.getColor();
        mGraphics2D.setColor(Color.white);

        //per the java doc for printing we should be translating to imageableX and imageableY
        // but this starts too far down when running on Windows.
        double imageableX = pageFormat.getImageableX();
        double imageableY = pageFormat.getImageableY();
        double imageableWidth = pageFormat.getImageableWidth();
        double imageableHeight = pageFormat.getImageableHeight();
//        log.trace("label imageable X: " + imageableX + " imageable Y: " + imageableY);
//        log.trace("label imageable width: " + imageableWidth + " imageable height: " + imageableHeight);
        double translateX = imageableX;
        double translateY = imageableY + 60;
//        log.trace("Translating to x:" + translateX + " y:" + translateY);
        mGraphics2D.translate(imageableX, imageableY);

        int labelWidth = (int) mGraphics2D.getClipBounds().getWidth();
        int labelHeight = (int) mGraphics2D.getClipBounds().getHeight();
//        log.trace("label graphics width: " + labelWidth + " graphics height: " + labelHeight);

        mGraphics2D.setColor(Color.black);
        double horizontalOffset = 5;    //TODO this should be coming from the label

        Font oldFont = mGraphics2D.getFont();
        mGraphics2D.setFont(mDetailsFont);
        mGraphics2D.setRenderingHint(
                RenderingHints.KEY_TEXT_ANTIALIASING,
                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        int padding = 6;   //padding between lines in px


        int detailsFontSize = mDetailsFont.getSize();
        int locationFontSize = mLocationFont.getSize();
//        log.trace("small text height: " + detailsFontSize);
//        log.trace("large text height: " + locationFontSize);

        //two detail lines plus location plus padding between the three
        int textareaHeight = (detailsFontSize * 2) + locationFontSize + (2 * padding);
//        log.trace("font area size calculated at: " + textareaHeight);

        int descriptionOffset = 6 + detailsFontSize;
        int productIdOffset = descriptionOffset + detailsFontSize + padding;
        int locationOffset = productIdOffset + detailsFontSize + padding;
//        log.trace("description vertical offset: " + descriptionOffset);
//        log.trace("product id vertical offset: " + productIdOffset);
//        log.trace("location vertical offset: " + locationOffset);

        draw(horizontalOffset, descriptionOffset, printRequest.getDescription());
        draw(horizontalOffset, productIdOffset, printRequest.getProductId());

        mGraphics2D.setFont(mLocationFont);
        draw(horizontalOffset, locationOffset, printRequest.getLocation());

        drawIcons();

        mGraphics2D.setFont(oldFont);
        mGraphics2D.setColor(oldColor);

        return mGraphics2D;
    }

    private void draw(double x, double y, String text) {
//        log.trace("writing text '" + text + "' at x:" + x + " y:" + y);
        mGraphics2D.translate((float) x, (float) y);
        mGraphics2D.drawString(text, 0, 0);
        mGraphics2D.translate(-(float) x, -(float) y);
    }

    private void drawIcons() {
        curIconX = ICON_H_OFFSET;
        double labelHeight = mPageFormat.getImageableHeight();
        curIconY = (int) labelHeight - ICON_V_OFFSET;

        try {
            //if the return location is more than 6 feet in the air then a forklift will be used for returns
            if (printRequest.isForkRequired()) {
                drawForkIcon();
            }

            if (printRequest.isFreezer()) {
                drawFreezerIcon();
            }

            //lumping chill and fresh meat together for now
            if (printRequest.isChill()) {
                drawChillIcon();
            }

            if (printRequest.isDollarGeneral()) {
                drawDGIcon();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("unable to add icons to label: " + e.getMessage());
        }
    }

    private void drawForkIcon() throws Exception {
        File forkFile = new File(getClass().getResource("/forklift_24.png").toURI());
        drawIcon(forkFile);
    }

    private void drawFreezerIcon() throws Exception {
        File freezerFile = new File(getClass().getResource("/snowflake_24.png").toURI());
        drawIcon(freezerFile);
    }

    private void drawChillIcon() throws Exception {
        File chillFile = new File(getClass().getResource("/thermometer-lines_24.png").toURI());
        drawIcon(chillFile);
    }

    private void drawDGIcon() throws Exception {
        File dgFile = new File(getClass().getResource("/dg_24.png").toURI());
        drawIcon(dgFile);
    }

    private void drawIcon(File iconFile) {
        try {
            BufferedImage iconImage = ImageIO.read(iconFile);
            int imgWidth = iconImage.getWidth();
            mGraphics2D.drawImage(iconImage, curIconX, curIconY, null);
            curIconX += imgWidth + ICON_H_PADD;
        } catch (IOException e) {
            System.err.printf("Unable to open file '%s'", iconFile.toString());
        }
    }
}
