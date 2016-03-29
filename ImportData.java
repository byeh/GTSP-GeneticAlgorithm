import java.lang.*;
import java.io.File;
import java.util.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Map;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;

public class ImportData {

  private HashMap <String, ArrayList<City>> states;

  public ImportData() throws FileNotFoundException{
    states = new HashMap<String, ArrayList<City>>();
  }

  public HashMap<String, ArrayList<City>> importData() throws FileNotFoundException {
    Scanner scanner = new Scanner(new File("cities.csv"));
    scanner.useDelimiter(" ; ");
    
    // read each line in CSV
    while (scanner.hasNextLine()) {
      City city = new City();
      String temp = scanner.nextLine();
      parseCity(temp, city, ";");
      addCityState(city);
    }
    scanner.close();
    auditImport();
    return states;
  }

  public ArrayList<Tour> importTours() throws FileNotFoundException {
    ArrayList<Tour> importedTours = new ArrayList<Tour>();
    try {
      Scanner scanner = new Scanner(new File("bestTours.txt"));
      scanner.useDelimiter("|");
      
      Tour t = new Tour(TSPConstants.TOUR_SIZE);
      // read each line in CSV
      while (scanner.hasNextLine()) {
        if(t.getTourSize() < TSPConstants.TOUR_SIZE) {
          //System.out.println(t.getTourSize());
          City city = new City();
          String temp = scanner.nextLine();
          parseCity(temp, city, "|");;
          t.addCity(city);
          if(t.getTourSize() == TSPConstants.TOUR_SIZE) {
            t.computeTourLength();
            System.out.println("Imported tour with length: " + t.getTourLength());
            importedTours.add(t);
            t = new Tour(TSPConstants.TOUR_SIZE);
          }
        }
      }
      scanner.close();
      //auditImport();
      return importedTours;      
    }
    catch(Exception e) {
      System.out.println(e);
    }
    return importedTours;
  }

// subroutine for ImportData() to parse a line
  private void parseCity(String s, City city, String token) {
    StringTokenizer st = new StringTokenizer(s, token);
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
    if(states.size() != TSPConstants.NUMBER_STATES) {
      System.out.println("ERROR: Number of States is: " + states.size());
      System.exit(1);
    }

    // verify that state abbrievations are correct:
    for(String stateAbbr : TSPConstants.STATE_LIST) {
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
    if(numCities != TSPConstants.NUMBER_CITIES) {
      System.out.println("ERROR: number of cities is: " + numCities);
      System.out.println(states.keySet());
      for(String state : states.keySet()) {
        System.out.println(state + ": " + states.get(state).size());
      }
      System.exit(3);
    }
  }

}