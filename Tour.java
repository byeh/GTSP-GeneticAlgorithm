import java.lang.*;
import java.util.*;

// A tour is an array list of Cities and can be modified
public class Tour {

  private ArrayList <City> tour;
  private int tourSize;

  public Tour(int size) {
    tour = new ArrayList<City>();
    tourSize = size;
  }

  public void addCity(City city) {
    tour.add(city);
  }

  public int getTourLength() {
    return tourSize;
  }

  public void printTour() {
    for(City city : tour) {
      System.out.print(city.getCity() + " " + city.getState() + ",");
    }
  }
}