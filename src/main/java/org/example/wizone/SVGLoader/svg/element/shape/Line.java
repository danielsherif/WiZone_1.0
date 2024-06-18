package org.example.wizone.SVGLoader.svg.element.shape;

import org.example.wizone.SVGLoader.svg.SVGParser;
import org.example.wizone.SVGLoader.svg.element.Element;

public class Line extends Shapes {
    private double x1;
    private double y1;
    private double x2;
    private double y2;
    private final String label = "line";

    public Line(String label) {
        super(label);
    }

    @Override
    public Element newInstance() {
        return null;
    }

    @Override
    public boolean load(String expr) {
        if (expr.contains(" x1=")) {
            final Double result = SVGParser.extractDouble(expr, " x1=")[0];
            if (result != null)
                x1 = result.doubleValue();
        }
        if (expr.contains(" y1=")) {
            final Double result = SVGParser.extractDouble(expr, " y1=")[0];
            if (result != null)
                y1 = result.doubleValue();
        }
        if (expr.contains(" x2=")) {
            final Double result = SVGParser.extractDouble(expr, " x2=")[0];
            if (result != null)
                x2 = result.doubleValue();
        }
        if (expr.contains(" y2=")) {
            final Double result = SVGParser.extractDouble(expr, " y2=")[0];
            if (result != null)
                y2 = result.doubleValue();
        }
        return true;
    }

    public String toString(){
        return label+": x1="+x1+", y1="+y1+", x2="+x2+", y2="+y2;
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
}
