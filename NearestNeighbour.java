import java.lang.*;
import java.io.File;
import java.util.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Map;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;

public class NearestNeighbour {

  private HashMap <String, ArrayList<City>> states;
  public NearestNeighbour(HashMap <String, ArrayList<City>> s) {
    states = s;
  } 
  // since we have predefined clusters, after running 2-opt we know that
  // there are no crossovers, so for each vertex v in a cluster, we can search
  // through all the clusters and see if we can find a smaller distance between
  // its neighbour.
  public Tour nearestNeighbours(Tour t) {
    Tour bestTour =  t;

    for(int i = 0; i < TSPConstants.TOUR_SIZE - 1; i++) {
      int node = TSPConstants.TOUR_SIZE + i; // dont worry about wrapovers
      City currentCity = t.getCity(node%TSPConstants.TOUR_SIZE);
      City pastCity = t.getCity((node-1)%TSPConstants.TOUR_SIZE);
      City nextCity = t.getCity((node+1)%TSPConstants.TOUR_SIZE);
      //System.out.println((node-1)%TSPConstants.TOUR_SIZE + ", " + (node)%TSPConstants.TOUR_SIZE + ", " + (node+1)%TSPConstants.TOUR_SIZE);

      ArrayList<City> cityStates = states.get(currentCity.getState());

      double pastToCurrent = t.computeEdge((node-1)%TSPConstants.TOUR_SIZE, node%TSPConstants.TOUR_SIZE);
      double currentToNext = t.computeEdge(node%TSPConstants.TOUR_SIZE, (node+1)%TSPConstants.TOUR_SIZE);
      double distance = pastToCurrent + currentToNext;

      for(City c: cityStates) {
        double newPastToCurrent = t.computePair(pastCity.getX(), c.getX(), pastCity.getY(), c.getY());
        double newCurrentToNext = t.computePair(c.getX(), nextCity.getX(), c.getY(), nextCity.getY());
        double newDistance = newPastToCurrent + newCurrentToNext;

        if(newDistance < distance) {
          Tour temp = new Tour(TSPConstants.TOUR_SIZE);
          for(int j = 0; j < i; j++) {
            temp.addCity(bestTour.getCity(j));
          }
          temp.addCity(c);
          for(int j = i+1; j < TSPConstants.TOUR_SIZE; j++) {
            temp.addCity(bestTour.getCity(j));
          }

          bestTour = temp;

        }
      }
    }
    return bestTour;
  }

}