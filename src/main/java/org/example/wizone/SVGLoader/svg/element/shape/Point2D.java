package org.example.wizone.SVGLoader.svg.element.shape;

public class Point2D {


    private double x;
    private double y;

    Point2D(double a, double b){
        this.x=a;
        this.y=b;
    }

    public void setX(double newX){
        this.x = newX;
    }
    public void setY(double newY){
        this.y = newY;
    }

    public double getX() {
        return x;
    }
    public double getY() {
        return y;
    }


}
