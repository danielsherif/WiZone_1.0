package org.example.wizone.SVGLoader.svg.element.shape.path;

import org.example.wizone.SVGLoader.svg.element.Element;

public class Close extends PathOp {
    protected String label = "Z";
    protected boolean absolute;
    public Close(String label) {
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
        return false;
    }
    public String toString(){
        return "[Z]";
    }
}