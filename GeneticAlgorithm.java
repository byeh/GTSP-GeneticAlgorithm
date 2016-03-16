import java.lang.*;
import java.io.File;
import java.util.*;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;

public class GeneticAlgorithm {

  // some constants
  private static final int NUMBER_STATES = 49;
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

  private static final int TOUR_SIZE = 49;

  // list of all states and cities contained in that state
  HashMap <String, ArrayList<City>> states;

  // A tour
  Tour tour;

  public GeneticAlgorithm() throws FileNotFoundException { 
    states = new HashMap<String, ArrayList<City>>();

    importData();
    audit();
    tour = generateRandomTour();
    
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
      addCity(city);
    }
    scanner.close();
  }

  // this method generates a random tour of 49 states
  public Tour generateRandomTour() {
    Tour tour = new Tour(NUMBER_STATES);
    return tour;
  }

  // subroutine for ImportData() to parse a line
  public void parseCity(String s, City city) {
    StringTokenizer st = new StringTokenizer(s, ";");
    city.updateLatitude(Double.parseDouble(st.nextToken()));
    city.updateLongitude(Double.parseDouble(st.nextToken()));
    city.updateCity(st.nextToken());
    city.updateCounty(st.nextToken());
    city.updateState(st.nextToken());
  }

  // Subroutine for Importdata(). 
  // Adds city to a hashmap of states, which contains an 
  // array list of cities in that state
  public void addCity(City city) {

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
  private void audit() {

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
  }


}