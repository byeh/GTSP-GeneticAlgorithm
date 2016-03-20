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

  // A set tours for our initial population and our parameters
  private int POPULATION_SIZE = 1;
  private int GENERATION_SURVIVAL_RATE = Math.max(1, (int) (POPULATION_SIZE * 0.25));
  private int TOTAL_GENERATIONS = 2;
  private int currentGeneration = 1;
  private HashMap <Integer, Tour> population;

  // list of all states and cities contained in that state
  private HashMap <String, ArrayList<City>> states;

  // lets run this cool genetic algorithm
  public GeneticAlgorithm(HashMap <String, ArrayList<City>> s) throws FileNotFoundException { 
    states = s;
    population = new HashMap<Integer, Tour>();   

    run();
  }

  private void run() throws FileNotFoundException {
    long startTime = System.currentTimeMillis();

    // setup experiment
    setParameters();

    // run experiment
    runGeneticAlgorithm(TOTAL_GENERATIONS);

    // get results
    showMetrics(startTime);
  }

  private void runGeneticAlgorithm(int cycles) {

    for(int i = 1; i < TOTAL_GENERATIONS; i++) {
      System.out.println("Currently on generation: " + currentGeneration + " of " + TOTAL_GENERATIONS);
      generatePopulation(POPULATION_SIZE);
      reproduction();
      simulateNaturalSelection();
      currentGeneration++;
      findBest();   
    }
  }

  private void setParameters() {
    System.out.println("Running Experiment with population size of:" + POPULATION_SIZE); 
  }  

  // this method generates a population of random tours and runs 2-opt on it
  private void generatePopulation(int pop) {
    for(int i = 0; i < pop; i++) {
      Tour temp = new RandomTour().generateRandomTour(states);
      temp = new TwoOptAlgorithm().runTwoOpt(temp);
      //temp = nearestNeighbours(temp);
      //temp = runTwoOpt(temp);
      population.put(temp.getTourLength(),temp);
    }
  }

  // merge a few tours together to generate a new, better tour
  private void reproduction() {
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

  // returns the best tour found by this genetic algorithm
  public Tour findBest() {
    int bestLength = -1;
    for(int length : population.keySet()) {
      if(bestLength == -1) {
        bestLength = length;
      }
      else if(length < bestLength){
        bestLength = length;
      }
    }
    return population.get(bestLength);
  }


  private void auditData() {
        // verify that tour generated is valid size
    // if(tour.getTourSize() != TSPConstants.NUMBER_STATES) {
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