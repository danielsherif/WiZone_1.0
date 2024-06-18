package org.example.wizone.SVGLoader.svg.element.shape.path;

import org.example.wizone.SVGLoader.svg.element.Element;
public class LineTo extends PathOp {
    private double x1;
    private double y1;

    protected String label = "L";
    protected boolean absolute;

    public LineTo(String label) {
        super(label);
    }

    @Override
    public Element newInstance() {
        return null;
    }

    public boolean load(String expr) {
        if (expr.contains("L")) {
            int start = expr.indexOf("L") + 1;
            int end = expr.substring(start).indexOf(" ")+start;
            int middle = expr.substring(start, end).indexOf(",")+start;
            final Double result = Double.valueOf(expr.substring(start, middle));
            if (result != null)
                x1 = result;
            final Double result1 = Double.valueOf(expr.substring(middle + 1, end));
            if (result1 != null) {
                y1 = result1;
            }
        }
        return false;
    }

    public double getX1() {
        return x1;
    }

    public void setX1(double x1) {
        this.x1 = x1;
    }

    public double getY1() {
        return y1;
    }

    public void setY1(double y1) {
        this.y1 = y1;
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

    public String toString(){
        return label + ": [ x1 =" + x1 + ", y1=" + y1 + "]";
    }
}