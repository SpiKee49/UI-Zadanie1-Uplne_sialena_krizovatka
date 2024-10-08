package Classes;

public class Car implements Cloneable {
    private String color;
    private int length;
    private int x;
    private int y;
    private boolean isVertical;

    public Car (String color, int length, int x, int y, boolean isVertical){
        this.color = color;
        this.length= length;
        this.x = x;
        this.y = y;
        this.isVertical = isVertical;
    }

    @Override
    protected Car clone() throws CloneNotSupportedException {
        return (Car) super.clone();
    }


    public String getColor() {
        return color;
    }
    public int getLength() {
        return length;
    }
    public int getRow() {
        return x;
    }
    public int getColumn() {
        return y;
    }
    public boolean isVertical() {
        return isVertical;
    }
}
