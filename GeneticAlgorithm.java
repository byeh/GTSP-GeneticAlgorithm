import java.lang.*;
import java.io.File;
import java.util.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Map;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;

public class GeneticAlgorithm {

  // settings
  private boolean verboseOutput = false;

  // some constants
  private static final int NUMBER_STATES = 49;
  private static final int TOUR_SIZE = 49;
  private static final int NUMBER_CITIES = 115475;
  private static final String[] STATE_LIST = {
    "AL", "AZ", "AR", "CA", "CO", "CT", "DC",
    "DE", "FL", "GA", "ID", "IL", "IN", 
    "IA", "KS", "KY", "LA", "ME", "MD", 
    "MA", "MI", "MN", "MS", "MO", "MT", 
    "NE", "NV", "NH", "NJ", "NM", "NY", 
    "NC", "ND", "OH", "OK", "OR", "PA", 
    "RI", "SC", "SD", "TN", "TX", "UT", 
    "VT", "VA", "WA", "WV", "WI", "WY"};

  // A set tours for our initial population and our parameters
  private int POPULATION_SIZE = 1;
  private int GENERATION_SURVIVAL_RATE = 1; // top 20 will survive
  private int TOTAL_GENERATIONS = 2;
  private int currentGeneration = 1;
  private HashMap <Integer, Tour> population;

  // Google Maps related Constants
  private static final String GOOGLE_MAPS_API_KEY = "&key=" + System.getenv("GOOGLE_MAPS_API_KEY");
  private static final String GOOGLE_MAPS_URL_HEAD = "https://maps.googleapis.com/maps/api/staticmap?center=39.833333,-98.583333&zoom=4&size=640x640&maptype=roadmap&path=color:0xff0000ff|weight:3|";

  // list of all states and cities contained in that state
  private HashMap <String, ArrayList<City>> states;

  // lets run this cool genetic algorithm
  public GeneticAlgorithm() throws FileNotFoundException { 
    states = new HashMap<String, ArrayList<City>>();
    population = new HashMap<Integer, Tour>();   

    run();
  }

  private void run() throws FileNotFoundException {
    long startTime = System.currentTimeMillis();
    setParameters();
    importData();
    auditImport();
    runCycleOfLifeAndDeath(TOTAL_GENERATIONS);
    findBest();
 
    showMetrics(startTime);
  }

  private void runCycleOfLifeAndDeath(int cycles) {

    for(int i = 1; i < TOTAL_GENERATIONS; i++) {
      System.out.println("Currently on generation: " + currentGeneration + " of " + TOTAL_GENERATIONS);
      generatePopulation(POPULATION_SIZE);
      simulateNaturalSelection();
      currentGeneration++;
      findBest();   
    }
  }

  private void setParameters() {
    // show verbose output
    verboseOutput = true;

    System.out.println("Running Experiment with population size of:" + POPULATION_SIZE);
  }

  private void findBest() {
    int bestLength = -1;
    for(int length : population.keySet()) {
      if(bestLength == -1) {
        bestLength = length;
      }
      else if(length < bestLength){
        bestLength = length;
      }
    }
    if(currentGeneration == TOTAL_GENERATIONS) {
      System.out.print(GOOGLE_MAPS_URL_HEAD);
      population.get(bestLength).printTour();
      System.out.print(GOOGLE_MAPS_API_KEY + '\n');
      System.out.println("Best Tour Length: " + bestLength);      
    }
    else {
      System.out.println("Best Tour of Generation " + currentGeneration + " is " + bestLength);
    }
  }

  // this method generates a population of random tours and runs 2-opt on it
  private void generatePopulation(int pop) {
    for(int i = 0; i < pop; i++) {
      Tour temp = generateRandomTour();
      temp = runTwoOpt(temp););
      //temp = nearestNeighbours(temp);
      //temp = runTwoOpt(temp);
      population.put(temp.getTourLength(),temp);
    }
  }

  // purge everything but the top number of options based on natural selection value
  // then regenerate more random solutions
  private void simulateNaturalSelection() {
    ArrayList keys = new ArrayList(population.keySet());
    Collections.sort(keys);
    for(int i = GENERATION_SURVIVAL_RATE; i < keys.size(); i++){
      population.remove(keys.get(i));
    }
    generatePopulation(POPULATION_SIZE - GENERATION_SURVIVAL_RATE);

  }

  // this method generates a random tour of 49 states
  private Tour generateRandomTour() {
    Tour temp = new Tour(TOUR_SIZE);
    for(String state: states.keySet()) {
      Random randomGenerator = new Random();
      int randomIndex = randomGenerator.nextInt(states.get(state).size());
      City selectedCity = states.get(state).get(randomIndex);
      temp.addCity(selectedCity);
    }
    return temp;
  }

  // since we have predefined clusters, after running 2-opt we know that
  // there are no crossovers, so for each vertex v in a cluster, we can search
  // through all the clusters and see if we can find a smaller distance between
  // its neighbour.
  private Tour nearestNeighbours(Tour t) {
    Tour bestTour =  t;

    for(int i = 0; i < TOUR_SIZE - 1; i++) {
      int node = TOUR_SIZE + i; // dont worry about wrapovers
      City currentCity = t.getCity(node%TOUR_SIZE);
      City pastCity = t.getCity((node-1)%TOUR_SIZE);
      City nextCity = t.getCity((node+1)%TOUR_SIZE);
      //System.out.println((node-1)%TOUR_SIZE + ", " + (node)%TOUR_SIZE + ", " + (node+1)%TOUR_SIZE);

      ArrayList<City> cityStates = states.get(currentCity.getState());

      double pastToCurrent = t.computeEdge((node-1)%TOUR_SIZE, node%TOUR_SIZE);
      double currentToNext = t.computeEdge(node%TOUR_SIZE, (node+1)%TOUR_SIZE);
      double distance = pastToCurrent + currentToNext;

      for(City c: cityStates) {
        double newPastToCurrent = t.computePair(pastCity.getX(), c.getX(), pastCity.getY(), c.getY());
        double newCurrentToNext = t.computePair(c.getX(), nextCity.getX(), c.getY(), nextCity.getY());
        double newDistance = newPastToCurrent + newCurrentToNext;

        if(newDistance < distance) {
          Tour temp = new Tour(TOUR_SIZE);
          for(int j = 0; j < i; j++) {
            temp.addCity(bestTour.getCity(j));
          }
          temp.addCity(c);
          for(int j = i+1; j < TOUR_SIZE; j++) {
            temp.addCity(bestTour.getCity(j));
          }

          bestTour = temp;

        }
      }
    }

    return bestTour;
  }
  private Tour runTwoOpt(Tour t) {
    Tour bestTour = t;
    // for any this edge
    for(int i = 0; i < TOUR_SIZE; i++) {
      for(int k = 0; k < Math.min((TOUR_SIZE- (i+2)), TOUR_SIZE - 3); k++) {
        // vertex indicies of the 2 selected edges
        int a = i;
        int b = (i+1)%TOUR_SIZE;
        int c = (k+i+2)%TOUR_SIZE;
        int d = (k+i+3)%TOUR_SIZE;

        double distanceAB = bestTour.computeEdge(a,b);
        double distanceCD = bestTour.computeEdge(c,d);
        double distanceAC = bestTour.computeEdge(a,c);
        double distanceBD = bestTour.computeEdge(b,d);

         // use the damn triangle inequality
        if((distanceAB + distanceCD) > (distanceAC + distanceBD)) {
          Tour newTour = new Tour(TOUR_SIZE);

          // end of first subtour
          for(int j = 0; j < a; j++) {
            newTour.addCity(bestTour.getCity(j));
          }

          // new edge AC
          newTour.addCity(bestTour.getCity(a));
          newTour.addCity(bestTour.getCity(c));

          for(int j = c - 1; j > b; j--) {
            newTour.addCity(bestTour.getCity(j));
          }
          // add edge BD
          newTour.addCity(bestTour.getCity(b));
          newTour.addCity(bestTour.getCity(d));

          // continue on
          for(int j = d+1; j < TOUR_SIZE; j++) {
            newTour.addCity(bestTour.getCity(j));
          }
          newTour.computeTourLength();
          //System.out.println(newTour.getTourLength());
          bestTour = newTour;
          i = 0;
          k = 0;
        }
      }
    }
    bestTour.computeTourLength();
    return bestTour;
  }

  // Method to import csv data for all the cities
  public void importData() throws FileNotFoundException {
    Scanner scanner = new Scanner(new File("cities.csv"));
    scanner.useDelimiter(" ; ");
    
    // read each line in CSV
    while (scanner.hasNextLine()) {
      City city = new City();
      String temp = scanner.nextLine();
      parseCity(temp, city);
      addCityState(city);
    }
    scanner.close();
  }

  // generates a Google Static Map with the tour
  private void printTour(Tour t) {
    ArrayList <City> tour = t.getTour();
    for(City city : tour) {
      System.out.print(city.getX() + "," + city.getY() + "|");
    }
  }

  // subroutine for ImportData() to parse a line
  private void parseCity(String s, City city) {
    StringTokenizer st = new StringTokenizer(s, ";");
    city.updateX(Double.parseDouble(st.nextToken()));
    city.updateY(Double.parseDouble(st.nextToken()));
    city.updateCity(st.nextToken());
    city.updateCounty(st.nextToken());
    city.updateState(st.nextToken());
  }

  // Subroutine for Importdata(). 
  // Adds city to a hashmap of states, which contains an 
  // array list of cities in that state
  private void addCityState(City city) {

    if(!states.containsKey(city.getState())) {
      ArrayList<City> temp = new ArrayList<City>();
      temp.add(city);
      states.put(city.getState(), temp);
    }
    else {
      ArrayList<City> temp = states.get(city.getState());
      temp.add(city);
    }
  }

  // method to verify integrity of processed data against initial data
  private void auditImport() {

    int numCities = 0;

    // verify that number of states is correct
    if(states.size() != NUMBER_STATES) {
      System.out.println("ERROR: Number of States is: " + states.size());
      System.exit(1);
    }

    // verify that state abbrievations are correct:
    for(String stateAbbr : STATE_LIST) {
      if(!states.containsKey(stateAbbr)) {
        System.out.println("ERROR: " + stateAbbr + " not in list");
        System.exit(2);
      }
    }
    
    // verify that number of cities is correct
    for(String state : states.keySet()) {
      numCities = numCities + states.get(state).size();
    }

    // verity that no cities magically disappeared
    if(numCities != NUMBER_CITIES) {
      System.out.println("ERROR: number of cities is: " + numCities);
      System.out.println(states.keySet());
      for(String state : states.keySet()) {
        System.out.println(state + ": " + states.get(state).size());
      }
      System.exit(3);
    }

    //System.out.println(GOOGLE_MAPS_API_KEY);
  }

  private void auditData() {
        // verify that tour generated is valid size
    // if(tour.getTourSize() != NUMBER_STATES) {
    //   System.out.println("ERROR: Number of States in tour is: " + tour.getTourSize());
    //   System.exit(4);      
    // }
  }

  // print some metrics
  private void showMetrics(long startTime) {
    long stopTime = System.currentTimeMillis();
    long millis = stopTime - startTime;
    long second = (millis / 1000) % 60;
    long minute = (millis / (1000 * 60)) % 60;
    long hour = (millis / (1000 * 60 * 60)) % 24;
    millis = millis % 1000;
    String time = String.format("Elapsed time: " + "%d" + "h " + "%d" + "m " + "%d" + "s " + "%d" + "ms", hour, minute, second, millis);
    System.out.println(time);
  }
}