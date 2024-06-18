package org.example.wizone.SVGLoader.svg.element.shape.path;

import org.example.wizone.SVGLoader.svg.element.Element;

public class CubicTo extends PathOp {
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private double x3;
    private double y3;

    protected String label = "C";
    protected boolean absolute;

    public CubicTo(String label) {
        super(label);
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

    public double getX3() {
        return x3;
    }

    public void setX3(double x3) {
        this.x3 = x3;
    }

    public double getY3() {
        return y3;
    }

    public void setY3(double y3) {
        this.y3 = y3;
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

    @Override
    public Element newInstance() {
        return null;
    }

    public boolean load(String expr) {
        if (expr.contains("C")) {
            int start1 = expr.indexOf("Q") + 1;
            int end1 = start1+expr.substring(start1).indexOf(" ");
            int middle1 = start1+expr.substring(start1, end1).indexOf(",");
            int start2 = end1 + 1;
            int end2 = start2+expr.substring(start2).indexOf(" ");
            int middle2 = start2+expr.substring(start2, end2).indexOf(",");
            int start3 = end2+1;
            int end3 = start3+expr.substring(start3).indexOf(" ");
            int middle3 = start3+expr.substring(start3).indexOf(",");
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
            final Double result5 = Double.valueOf(expr.substring(start3, middle3));
            if (result5 != null) {
                x3 = result3;
            }
            final Double result6 = Double.valueOf(expr.substring(middle3 + 1, end3));
            if (result6 != null) {
                y3 = result6;
            }
        }
        return false;
    }
    public String toString(){
        return label + ": [ x1 =" + x1 + ", y1=" + y1 + ", x2=" + x2 + ", y2 =" + y2 + ", x3 =" + x3 + ", y3 =" +y3+ " ]";
    }
}
