package org.example.wizone.SVGLoader.svg.element.shape.path;

import org.example.wizone.SVGLoader.svg.element.Element;


public class MoveTo extends PathOp {
    private double x;
    private double y;

    protected String label = "M";
    protected boolean absolute;

    public MoveTo(String label) {
        super(label);
    }

    @Override
    public Element newInstance() {
        return null;
    }

    public boolean load(String expr) {
        if (expr.contains("M")) {
            int start = expr.indexOf("M") + 1;
            // Check for space immediately after "M"
            if (expr.charAt(start) == ' ') {
                start++; // Move start index past the space
            }
            int end = expr.substring(start).indexOf(" ") + start;
            int middle = expr.substring(start, end).indexOf(",") + start;
            final Double result = Double.valueOf(expr.substring(start, middle));
            if (result != null)
                x = result;
            final Double result1 = Double.valueOf(expr.substring(middle + 1, end));
            if (result1 != null) {
                y = result1;
            }
        }
        return false;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean isAbsolute() {
        return absolute;
    }

    public void setAbsolute(boolean absolute) {
        this.absolute = absolute;
    }

    public String toString() {
        return label + ": [ x =" + x + ", y=" + y + " ]";
    }
}