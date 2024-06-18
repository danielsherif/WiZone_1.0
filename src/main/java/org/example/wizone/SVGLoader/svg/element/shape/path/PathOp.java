package org.example.wizone.SVGLoader.svg.element.shape.path;

import org.example.wizone.SVGLoader.svg.element.shape.Shapes;


public abstract class PathOp extends Shapes {
    protected char label;
    protected boolean absolute;

    public PathOp(String label) {
        super(label);
    }
}