package org.example.wizone.SVGLoader.svg.element.shape;

import org.example.wizone.SVGLoader.svg.SVGParser;
import org.example.wizone.SVGLoader.svg.element.Element;

    public class Rect extends Shapes {
        private double x;
        private double y;
        private double rx;
        private double ry;
        private double width;
        private double height;
        private final String label = "rect";

    public Rect(String label) {
        super(label);
    }
        public Rect(double x, double y, double width, double height) {
            super("rect"); // Call the Shapes constructor with the label
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }

        @Override
    public Element newInstance() {
        return null;
    }

        public double getX() {
            return x;
        }

        public void setX(double x) {
            this.x = x;
        }

        public double getY() {
            return y;
        }

        public void setY(double y) {
            this.y = y;
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

        public double getWidth() {
            return width;
        }

        public void setWidth(double width) {
            this.width = width;
        }

        public double getHeight() {
            return height;
        }

        public void setHeight(double height) {
            this.height = height;
        }

        public String getLabel() {
            return label;
        }

        @Override
    public boolean load(String expr) {
        if (expr.contains(" x=")) {
            final Double result = SVGParser.extractDouble(expr, " x=")[0];
            if (result != null)
                x = result.doubleValue();
        }
        if (expr.contains(" y=")) {
            final Double result = SVGParser.extractDouble(expr, " y=")[0];
            if (result != null)
                y = result.doubleValue();
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
        if (expr.contains(" width=")) {
            final Double result = SVGParser.extractDouble(expr, " width=")[0];
            if (result != null)
                width = result.doubleValue();
        }
        if (expr.contains(" height=")) {
            final Double result = SVGParser.extractDouble(expr, " height=")[0];
            if (result != null)
                height = result.doubleValue();
        }
        return true;
    }
    public String toString(){
        return label+": x="+x+", y="+y+", rx="+rx+", ry="+ry+", width="+width+", height="+height;
    }
}
