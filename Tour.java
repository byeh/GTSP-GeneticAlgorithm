import java.lang.*;
import java.util.*;
import java.lang.Math;
// A tour is an array list of Cities and can be modified
public class Tour {

  private ArrayList <City> tour;
  private int tourSize;
  private double tourLength;
  private int maxSize;

  public Tour(int max) {
    tour = new ArrayList<City>();
    tourSize = 0;
    maxSize = max;
    tourLength = -1.0;
  }

  public void addCity(City city) {
    if(tourLength < maxSize) {
      tour.add(city);
      tourSize = tourSize + 1;
    }
    if(tourSize == maxSize) {
      computeTourLength();
    }
  }

  // replaces tour with new tour
  public void replaceTour(ArrayList<City> t) {
    tour.clear();
    
    if(t.size() != maxSize) {
      System.out.println("ERROR: replaceTour not replacing tour of same size");
      System.exit(5);
    }
    tourSize = t.size();
    for(int i = 0; i < t.size(); i++) {
      tour.add(t.get(i));
    }
    computeTourLength();
  }

  public void replaceCity(int i, City c) {
    tour.set(i,c);
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
    tourLength = -1;
    for(int i = 0; i < tourSize; i++) {
      if(i == tourSize - 1) {
        double distance = computePair(tour.get(i).getX(), tour.get(0).getX(),tour.get(i).getY(),tour.get(0).getY());
        tourLength = tourLength + distance;
      }
      else {
        double distance = computePair(tour.get(i).getX(), tour.get(i+1).getX(),tour.get(i).getY(),tour.get(i+1).getY());
        tourLength = tourLength + distance;
      }
    }
    //System.out.println("Total Distance:" + (int)tourLength);
  }

  private double computePair(double x1, double x2, double y1, double y2) {
    double dx = x1-x2;
    double dy = y1-y2;
    double distance = Math.sqrt((dx*dx) + (dy*dy));
    return distance;
          //System.out.println((i+1) + ": " + "Distance:" + distance + " " + tour.get(i).getCity() + ", " + tour.get(0).getCity())
  }

  public void printTour() {
    for(City city : tour) {
      System.out.print(city.getCity() + " " + city.getState() + ",");
    }
    System.out.print("\n");
  }
}