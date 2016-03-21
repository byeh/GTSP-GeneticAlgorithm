import java.lang.*;
import java.io.File;
import java.util.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Map;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;

public class RandomTour {

  public RandomTour() {

  } 

  // this method generates a random tour of 49 states
  public Tour generateRandomTour(HashMap <String, ArrayList<City>> states) {
    Tour temp = new Tour(TSPConstants.TOUR_SIZE);
    Random randomGenerator = new Random();
    for(String state: states.keySet()) {
      int randomIndex = randomGenerator.nextInt(states.get(state).size());
      City selectedCity = states.get(state).get(randomIndex);
      temp.addCity(selectedCity);
    }
    return temp;
  }
}