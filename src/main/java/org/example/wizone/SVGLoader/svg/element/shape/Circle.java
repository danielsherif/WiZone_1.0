package org.example.wizone.SVGLoader.svg.element.shape;

import org.example.wizone.SVGLoader.svg.SVGParser;
import org.example.wizone.SVGLoader.svg.element.Element;

public class Circle extends Shapes {
    private double cx;
    private double cy;
    private double r;
    private String label = "circle";

    public Circle(String label) {
        super(label);
    }

    public double getCx() {
        return cx;
    }

    public void setCx(double cx) {
        this.cx = cx;
    }

    public double getCy() {
        return cy;
    }

    public void setCy(double cy) {
        this.cy = cy;
    }

    public double getR() {
        return r;
    }

    public void setR(double r) {
        this.r = r;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public Element newInstance() {
        return null;
    }

    @Override
    public boolean load(String expr) {
        if (expr.contains(" cx=")) {
            final Double result = SVGParser.extractDouble(expr, " cx=")[0];
            if (result != null)
                cx = result.doubleValue();
        }
        if (expr.contains(" cy=")) {
            final Double result = SVGParser.extractDouble(expr, " cy=")[0];
            if (result != null)
                cy = result.doubleValue();
        }
        if (expr.contains(" r=")) {
            final Double result = SVGParser.extractDouble(expr, " r=")[0];
            if (result != null)
                r = result.doubleValue();
        }
        return false;
    }
    public String toString(){
        return label+": cx="+cx+", cy="+cy+", r="+r;
    }
}
