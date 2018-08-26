package io.phobotic.label;

import java.awt.print.PageFormat;

/**
 * Created by Jonathan Nelson on 2/18/16.
 */

public class DymoSmallHorizontalLabel implements PrintableLabel {
    private int mOrientation = PageFormat.PORTRAIT;
    private double mLabelWidthMM = 120;
    private double mLabelHeightMM = 31.75;
    private double mHorizontalMarginMM = 5.0;
    private double mVerticalMarginMM = 5.0;

    public double getLabelHeightMarginMM() {
        return mVerticalMarginMM;
    }

    public double getLabelWidthMM() {
        return mLabelWidthMM;
    }

    public double getLabelHeightMM() {
        return mLabelHeightMM;
    }

    public double getLabelWidthMarginMM() {
        return mHorizontalMarginMM;
    }

    public double getLabelWidthPx() {
        return toPPI(mLabelWidthMM);
    }

    private double toPPI(double mm) {
        return (mm / 25.4) * 72;
    }

    public double getLabelHeightPx() {
        return toPPI(mLabelHeightMM);
    }

    public double getLabelVerticalMarginPx() {
        return toPPI(mVerticalMarginMM);
    }


    public double getLabelHorizontalMarginPx() {
        return toPPI(mHorizontalMarginMM);
    }

    public int getOrientation() {
        return mOrientation;
    }
}