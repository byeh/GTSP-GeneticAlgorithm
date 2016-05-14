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
    tourLength = 0;
  }

  public Tour(Tour t) {
    tour = new ArrayList<City>();
    tourSize = t.getTourSize();
    maxSize = t.getTourSize();
    tourLength = t.getTourLength();
    for(int i = 0; i < t.getTourSize(); i++) {
      tour.add(t.getCity(i));
    }
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

  public City getCity(int index) {
    return tour.get(index);
  }

  // replaces tour with new tour
  public void replaceTour(ArrayList<City> t) {
    tour.clear();
    
    tourSize = t.size();
    for(int i = 0; i < t.size(); i++) {
      tour.add(t.get(i));
    }
    computeTourLength();
  }

  public void replaceCity(int i, City c) {
    tour.set(i,c);
    computeTourLength();
  }

  public Tour swapCity(int a, int b) {
    City cityA = tour.get(a);
    City cityB = tour.get(b);
    tour.set(a, cityB);
    tour.set(b, cityA);
    return this;
  }

  public boolean containState(City c) {
    boolean found = false;
    for(City city : tour) {
      if(city.getState().equals(c.getState())) {
        return true;
      }
    }
    return found;
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
    tourLength = 0;

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
  }

  // takes two vertices in tour and computes distance
  public double computeEdge(int index1, int index2) {
    return computePair(tour.get(index1).getX(), tour.get(index2).getX(), tour.get(index1).getY(), tour.get(index2).getY());
  }

  public double computePair(double x1, double x2, double y1, double y2) {
    double dx = x1-x2;
    double dy = y1-y2;
    double distance = Math.sqrt((dx*dx) + (dy*dy));
    return distance;
  }

  public void printTour() {
    for(int i = 0; i < tour.size(); i++) {
      String cityName = tour.get(i).getCity().replaceAll("\\s", "+");
      String countyName = tour.get(i).getCounty().replaceAll("\\s", "+");
      String stateName = TSPConstants.STATE_NAME.get(tour.get(i).getState());

      if(i == tour.size()-1) {
        System.out.print(cityName + "," + countyName +"," + stateName + "|");
        System.out.print(tour.get(0).getCity().replaceAll("\\s", "+") + "," + tour.get(0).getCounty().replaceAll("\\s", "+") +"," + TSPConstants.STATE_NAME.get(tour.get(0).getState()));
      }
      else {
        System.out.print(cityName + "," + countyName +"," + stateName + "|");
      }
    }
  }

  public void printTourLine() {
    for(int i = 0; i < tour.size(); i++) {
      City c = tour.get(i);
      double latitude = c.getX();
      double longitude = c.getY();
      String cityName = c.getCity();
      String countyName = c.getCounty();
      String stateName = c.getState();

      if(i == tour.size()-1) {
        System.out.print(latitude + "|"  + longitude + "|" + cityName + "|" + countyName +"|" + stateName);
        System.out.println("");
      }
      else {
        System.out.println(latitude + "|"  + longitude + "|" + cityName + "|" + countyName +"|" + stateName);
      }
    }
  }
}