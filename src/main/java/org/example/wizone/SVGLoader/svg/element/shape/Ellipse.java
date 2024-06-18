package org.example.wizone.SVGLoader.svg.element.shape;

import org.example.wizone.SVGLoader.svg.SVGParser;
import org.example.wizone.SVGLoader.svg.element.Element;

public class Ellipse extends Shapes {
    private double cx;
    private double cy;
    private double rx;
    private double ry;
    private final String label = "ellipse";

    public Ellipse(String label) {
        super(label);
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
        if (expr.contains(" rx=")) {
            final Double result = SVGParser.extractDouble(expr, " rx=")[0];
            if (result != null)
                rx = result.doubleValue();
        }
        if (expr.contains(" ry=")) {
            final Double result = SVGParser.extractDouble(expr, " ry=")[0];
            if (result != null)
                ry = result.doubleValue();
        }
        return true;
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

    public double getRx() {
        return rx;
    }

    public void setRx(double rx) {
        this.rx = rx;
    }

    public double getRy() {
        return ry;
    }

    public void setRy(double ry) {
        this.ry = ry;
    }

    public String getLabel() {
        return label;
    }

    public String toString(){
        return label+": cx="+cx+", cy="+cy+", rx="+rx+", ry="+ry;
    }
}
