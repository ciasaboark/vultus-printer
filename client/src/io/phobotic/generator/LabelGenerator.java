package io.phobotic.generator;

import io.phobotic.label.Printable;

import javax.print.PrintException;
import java.awt.*;
import java.awt.print.PageFormat;

/**
 * Created by Jonathan Nelson on 3/7/16.
 */
public interface LabelGenerator {
    Graphics2D generate(Graphics2D g2d, PageFormat pageFormat, Printable printable) throws ClassCastException, PrintException;
}
