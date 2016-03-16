import java.lang.*;
import java.util.*;
import java.lang.Math;
// A tour is an array list of Cities and can be modified
public class Tour {

  private ArrayList <City> tour;
  private int tourSize;
  private double tourLength;
  private int maxSize;
  private boolean isLengthComputed;

  public Tour(int max) {
    tour = new ArrayList<City>();
    tourSize = 0;
    maxSize = max;
    tourLength = -1.0;
    isLengthComputed = false;
  }

  public void addCity(City city) {
    if(tourLength < maxSize) {
      tour.add(city);
      tourSize = tourSize + 1;
    }
  }

  public ArrayList<City> getTour() {
    return tour;
  }

  public int getTourSize() {
    return tourSize;
  }

  public int getTourLength() {
    return (int) tourLength;
  }

  public int getMaxSize() {
    return maxSize;
  }

  public void computeTourLength() {

    if((tourSize == maxSize) && !isLengthComputed) {
        double dx;
        double dy;
        double distance;
      for(int i = 0; i < tourSize; i++) {
        if(i == tourSize - 1) {
          dx = tour.get(i).getX() - tour.get(0).getX();
          dy = tour.get(i).getY() - tour.get(0).getY();
          distance = Math.sqrt((dx*dx) + (dy*dy));
          tourLength = tourLength + distance;
          //System.out.println((i+1) + ": " + "Distance:" + distance + " " + tour.get(i).getCity() + ", " + tour.get(0).getCity());
        }
        else {
          dx = tour.get(i).getX() - tour.get(i+1).getX();
          dy = tour.get(i).getY() - tour.get(i+1).getY();
          distance = Math.sqrt((dx*dx) + (dy*dy));
          tourLength = tourLength + distance;
          //System.out.println((i+1) + ": " + "Distance:" + distance + " " + tour.get(i).getCity() + ", " + tour.get(i+1).getCity());
        }
      }

      System.out.println("Total Distance:" + (int)tourLength);
      isLengthComputed = true;
    }
  }

  public void printTour() {
    for(City city : tour) {
      System.out.print(city.getCity() + " " + city.getState() + ",");
    }
  }
}