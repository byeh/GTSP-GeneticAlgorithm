  public class Location {

    Double x;
    Double y;

    public Location(Double xcord, Double ycord) {
      x = xcord;
      y = ycord;
    }

    public void updateX(double xcord) {
      x = xcord;
    }

    public void updateY(double ycord) {
      y = ycord;
    }

    public Double getX() {
      return x;
    }

    public Double getY() {
      return y;
    }

    public void print() {
      System.out.println("X-Coordinate: " + x + ", Y-Coordinate: " + y);
    }
  }