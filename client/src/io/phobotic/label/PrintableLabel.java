package io.phobotic.label;

/**
 * Created by Jonathan Nelson on 2/18/16.
 */
public interface PrintableLabel {
    double getLabelHeightMarginMM();

    double getLabelWidthMM();

    double getLabelHeightMM();

    double getLabelWidthMarginMM();

    int getOrientation();

    double getLabelWidthPx();

    double getLabelHeightPx();

    double getLabelHorizontalMarginPx();

    double getLabelVerticalMarginPx();
}
