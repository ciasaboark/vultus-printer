package io.phobotic.label;

import java.awt.print.PageFormat;

/**
 * Created by Jonathan Nelson on 2/18/16.
 */

public class DymoLongAddressLabel implements PrintableLabel {
    private int mOrientation = PageFormat.PORTRAIT;
    private double mLabelWidthMM = 57.15;
    private double mLabelHeightMM = 31.75;
    private double mLabelWidthMarginMM = 5.0;
    private double mLabelHeightMarginMM = 5.0;


    public double getLabelHeightMarginMM() {
        return mLabelHeightMarginMM;
    }

    public double getLabelWidthMM() {
        return mLabelWidthMM;
    }

    public double getLabelHeightMM() {
        return mLabelHeightMM;
    }

    public double getLabelWidthMarginMM() {
        return mLabelWidthMarginMM;
    }

//    private BitMatrix generateQRCode() throws WriterException {
//        Map<EncodeHintType, Object> hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
//        hints.put(EncodeHintType.CHARACTER_SET, "ISO-8859-1");
//        hints.put(EncodeHintType.MARGIN, 0);
//        StringBuilder sb = new StringBuilder();
//        DateFormat df = new SimpleDateFormat("MM/dd/yyyy KK:mm:ss a Z").getDateTimeInstance();
//        df.setTimeZone(TimeZone.getTimeZone("UTC"));
//        sb.append("UTC Time: " + df.format(System.currentTimeMillis()));
//        String qrString = sb.toString();
//        return new QRCodeWriter().encode(((Long) System.currentTimeMillis()).toString(), BarcodeFormat.QR_CODE, 55, 55, hints);
//    }
//
//
//    private void drawQRCode(BitMatrix qrCode, Graphics2D g2d, PageFormat pageFormat) {
//        double width = pageFormat.getImageableWidth();
//        double height = pageFormat.getImageableHeight();
//        g2d.translate((int) pageFormat.getImageableX(),
//                (int) pageFormat.getImageableY());
//        int matrixWidth = qrCode.getWidth();
//        int matrixHeight = qrCode.getHeight();
//        int labelWidth = (int) g2d.getClipBounds().getWidth();
//
//        int xPos = (labelWidth - matrixWidth) / 2;
//
//        for (int i = 0; i < matrixWidth; i++, xPos++) {
//            for (int j = 0; j < matrixHeight; j++) {
//                if (qrCode.get(i, j)) {
//                    g2d.fillRect(xPos, j + 10, 1, 1);
//                }
//            }
//        }
//    }
//
//    private void drawProductInformation(Graphics2D g2d, Font font, int horizontalOffset) {
//        Font oldFont = g2d.getFont();
//        g2d.setFont(font);
//        g2d.setRenderingHint(
//                RenderingHints.KEY_TEXT_ANTIALIASING,
//                RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
//        int padding = 10;   //padding between lines in px
//        int labelWidth = (int) g2d.getClipBounds().getWidth();
//        int fontHeight = font.getSize();
//        int textareaHeight = fontHeight * 3 + 2 * padding;
//        int initialOffset = (labelWidth - textareaHeight) / 2;
//        int descriptionOffset = labelWidth - initialOffset - fontHeight;
//        int productIdOffset = descriptionOffset - fontHeight - padding;
//        int locationOffset = productIdOffset - fontHeight - padding;
//
//        drawRotate(g2d, descriptionOffset, horizontalOffset, 90, mProduct.getDescription());
//        drawRotate(g2d, productIdOffset, horizontalOffset, 90, mProduct.getProductID());
////        drawRotate(g2d, locationOffset, horizontalOffset, 90, mProduct.getl); //TODO extract primary selection location from product locations
//        drawRotate(g2d, locationOffset, horizontalOffset, 90, "No location yet");
//        g2d.setFont(oldFont);
//    }
//
//    private static void drawRotate(Graphics2D g2d, double x, double y, int angle, String text) {
//        g2d.translate((float) x, (float) y);
//        g2d.rotate(Math.toRadians(angle));
//        g2d.drawString(text, 0, 0);
//        g2d.rotate(-Math.toRadians(angle));
//        g2d.translate(-(float) x, -(float) y);
//    }

    public int getOrientation() {
        return mOrientation;
    }

    public double getLabelWidthPx() {
        return 0;
    }

    public double getLabelHeightPx() {
        return 0;
    }

    public double getLabelHorizontalMarginPx() {
        return 0;
    }

    public double getLabelVerticalMarginPx() {
        return 0;
    }
}