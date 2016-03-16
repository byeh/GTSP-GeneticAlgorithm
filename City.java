import java.lang.*;

public class City {

  Location coord;
  String city;
  String county;
  String state;
  City() { 
    coord = new Location(0.0, 0.0);
    city = "";
    county = "";
    state = "";
  }

  public Double getX() {
    return coord.getX();
  }

  public Double getY() {
    return coord.getY();
  }

  public String getCity() {
    return city;
  }

  public String getCounty() {
    return county;
  }

  public String getState() {
    return state;
  }

  public void updateX(double y) {
    coord.updateX(y);
  }

  public void updateY(double y) {
    coord.updateY(y);
  }

  public void updateCity(String c) {
    city = c;
  }

  public void updateCounty(String c) {
    county = c;
  }

  public void updateState(String s) {
    state = s;
  }


  public void print() {
    coord.print();
    System.out.println(city);
    System.out.println(county);
    System.out.println(state);
  }
} 