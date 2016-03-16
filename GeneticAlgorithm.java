import java.lang.*;
import java.io.File;
import java.util.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Map;
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

  // Google Maps related Constants
  private static final String GOOGLE_MAPS_API_KEY = System.getenv("GOOGLE_MAPS_API_KEY");
  private static final String GOOGLE_MAPS_URL_HEAD = "https://maps.googleapis.com/maps/api/staticmap?center=39.833333, -98.583333&zoom=4&size=640x640&maptype=roadmap&path=color:0xff0000ff|weight:5|";

  

  // list of all states and cities contained in that state
  HashMap <String, ArrayList<City>> states;

  // A set tours for our initial population and our parameters
  private int populationSize = 50;
  private ArrayList <Tour> population;

  // lets run this cool genetic algorithm
  public GeneticAlgorithm() throws FileNotFoundException { 
    states = new HashMap<String, ArrayList<City>>();
    population = new ArrayList<Tour>();   

    run();
  }

  private void run() throws FileNotFoundException {
    long startTime = System.currentTimeMillis();
    setParameters();
    importData();
    auditImport();
    generateInitialPopulation();
    findBest();
 
    showMetrics(startTime);
  }

  private void setParameters() {
    // show verbose output
    verboseOutput = true;
    populationSize = 50000;
    System.out.println("Running Experiment with population size of:" + populationSize);
  }

  private void findBest() {
    int best = population.get(0).getTourLength();
    for(Tour t : population) {
      if(t.getTourLength() < best) {
        best = t.getTourLength();
      }
    }
    System.out.println("Best tour for 2-opt is:" + best);
  }

  // this method generates a population of random tours and runs 2-opt on it
  private void generateInitialPopulation() {
    for(int i = 0; i < populationSize; i++) {
      Tour temp = generateRandomTour();
      //System.out.print(temp.getTourLength() + " ");
      temp = runTwoOpt(temp);
      population.add(temp);
    }
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

  public Tour runTwoOpt(Tour t) {

    Tour bestTour = t;

    for(int i = 0; i < t.getTourSize() - 2; i++) {
      for(int j = i + 2; j < t.getTourSize(); j++) {
        Tour temp = new Tour(TOUR_SIZE);
        temp.replaceTour(t.getTour());
        City a = t.getTour().get(i);
        City b = t.getTour().get(j);

        // swap the two cities in the tour
        temp.replaceCity(i,b);
        temp.replaceCity(j,a);
        temp.computeTourLength();

        if(temp.getTourLength() < bestTour.getTourLength()) {
          bestTour = temp;
          //System.out.println(bestTour.getTourLength() + " " + temp.getTourLength());
        }
        else {
          //System.out.println(bestTour.getTourLength() + " " + temp.getTourLength());
        }
      }
    }
    //System.out.println(count);
    //System.out.println(bestTour.getTourLength());
    return bestTour;
  }

  // Method to import csv data for all the cities
  public void importData() throws FileNotFoundException {
    System.out.println("Importing Data");
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
    System.out.println("Done Importing Data");
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