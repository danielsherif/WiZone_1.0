package org.example.wizone.SVGLoader.svg.element.shape;

import org.example.wizone.SVGLoader.svg.element.BaseElement;

public abstract class Shapes extends BaseElement {
    public Shapes(String label) {
        super(label);
    }
    public Shapes() {
        super(""); // Provide a default label or handle it differently
    }
}
