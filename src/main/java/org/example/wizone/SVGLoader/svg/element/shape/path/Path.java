package org.example.wizone.SVGLoader.svg.element.shape.path;

import org.example.wizone.SVGLoader.svg.element.Element;
import org.example.wizone.SVGLoader.svg.element.shape.Shapes;

import java.util.ArrayList;
import java.util.List;

public class Path extends Shapes {
    private List<PathOp> pathOp = new ArrayList<>();
    private String label = "path";

    public List<PathOp> getPathOp() {
        return pathOp;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setPathOp(List<PathOp> pathOp) {
        this.pathOp = pathOp;
    }

    public Path(String label) {
        super(label);
    }

    @Override
    public Element newInstance() {
        return null;
    }

    public String getLabel() {
        return this.label;
    }

    @Override
    public boolean load(String expr) {
        if (expr.contains("M")) {
            MoveTo move = new MoveTo("M");
            move.load(expr);
            pathOp.add(move);
        }
        if (expr.contains("Q")) {
            QuadTo quad = new QuadTo("Q");
            quad.load(expr);
            pathOp.add(quad);
        }
        if (expr.contains("L")) {
            LineTo line = new LineTo("L");
            line.load(expr);
            pathOp.add(line);
        }
        if (expr.contains("C")) {
            CubicTo cubic = new CubicTo("C");
            cubic.load(expr);
            pathOp.add(cubic);
        }
        if (expr.contains("Z")) {
            Close close = new Close("Z");
            pathOp.add(close);
        }
        return true;
    }

    public String toString() {
        String output = "path: ";
        for (int i = 0; i < pathOp.size(); i++) {
            output = output + pathOp.get(i).toString();
        }
        return output;
    }
}