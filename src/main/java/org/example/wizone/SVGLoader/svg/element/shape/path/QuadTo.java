package org.example.wizone.SVGLoader.svg.element.shape.path;

import org.example.wizone.SVGLoader.svg.element.Element;

public class QuadTo extends PathOp {
    private double x1;
    private double y1;
    private double x2;
    private double y2;

    protected String label = "Q";
    protected boolean absolute;

    public QuadTo(String label) {
        super(label);
    }

    @Override
    public Element newInstance() {
        return null;
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

    public double getX2() {
        return x2;
    }

    public void setX2(double x2) {
        this.x2 = x2;
    }

    public double getY2() {
        return y2;
    }

    public void setY2(double y2) {
        this.y2 = y2;
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

    public boolean load(String expr) {
        if (expr.contains("Q")) {
            int start1 = expr.indexOf("Q") + 1;
            int end1 = start1+expr.substring(start1).indexOf(" ");
            int middle1 = start1+expr.substring(start1, end1).indexOf(",");
            int start2 = end1 + 1;
            int end2 = start2+expr.substring(start2).indexOf(" ");
            int middle2 = start2+expr.substring(start2, end2).indexOf(",");
            final Double result1 = Double.valueOf(expr.substring(start1, middle1));
            if (result1 != null)
                x1 = result1;
            final Double result2 = Double.valueOf(expr.substring(middle1 + 1, end1));
            if (result2 != null) {
                y1 = result2;
            }
            final Double result3 = Double.valueOf(expr.substring(start2, middle2));
            if (result3 != null) {
                x2 = result3;
            }
            final Double result4 = Double.valueOf(expr.substring(middle2 + 1, end2));
            if (result4 != null) {
                y2 = result4;
            }
        }
        return false;
    }
    public String toString(){
        return label + ": [ x1 =" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2 =" + y2 + " ]";
    }

}