package org.example.wizone.SVGLoader.svg.element.shape;

import org.example.wizone.SVGLoader.svg.SVGParser;
import org.example.wizone.SVGLoader.svg.element.Element;

import java.util.ArrayList;
import java.util.List;

public class Polygon extends Polyline {
    private final String label = "polygon";
    private final List<Point2D> polygon = new ArrayList<>();

    public Polygon(String label) {
        super(label);
    }

    @Override
    public Element newInstance() {
        return null;
    }

    @Override
    public boolean load(String expr) {
        if (expr.contains(" points=")) {
            final Double[] result = SVGParser.extractDouble(expr, " points=");
            if (result != null) {
                // Check if number of points is valid for a triangle (3 pairs)
                if (result.length % 2 == 0 && result.length >= 6) {
                    for (int i = 0; i < result.length; i += 2) {
                        polygon.add(new Point2D(result[i].doubleValue(), result[i + 1].doubleValue()));
                    }
                } else {
                    System.err.println("Warning: Invalid number of points for polygon in expression: " + expr);
                }
            }
        }
        return true;
    }

    public String toString() {
        String toReturn = label + ": ";
        for (Point2D a : polygon) {
            toReturn += "(" + a.getX() + "," + a.getY() + ") ";
        }
        return toReturn;
    }

    public String getLabel() {
        return label;
    }

    public List<Point2D> getPolygon() {
        return polygon;
    }
}

