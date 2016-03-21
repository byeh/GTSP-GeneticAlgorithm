import java.lang.*;
import java.io.File;
import java.util.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Map;
import java.lang.Math;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;

public class GeneticAlgorithm {

  // A set tours for our initial population and our parameters
  private int INITIAL_POPULATION_SIZE = 2000;
  private int REPLICATON_SIZE = 800;
  private int REPRODUCTION_RATE = 1200;
  private int TOTAL_GENERATIONS = 25;
  private int currentGeneration = 1;
  private static final double DEATH_POW = 0.375;
  private static final double REPROD_POW = 0.375;
  private static final double MUTATION_POW = 0.05;

  private ArrayList <Tour> population;

  // list of all states and cities contained in that state
  private HashMap <String, ArrayList<City>> states;
  private Random randomGenerator = new Random();

  // lets run this cool genetic algorithm
  public GeneticAlgorithm(HashMap <String, ArrayList<City>> s) throws FileNotFoundException { 
    states = s;
    population = new ArrayList<Tour>();   

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

  private void setParameters() {
    System.out.println("Running Experiment with population size of:" +INITIAL_POPULATION_SIZE); 
  }  

  private void runGeneticAlgorithm(int cycles) {

    for(int i = 1; i < TOTAL_GENERATIONS; i++) {
      System.out.println("Currently on generation: " + currentGeneration + " of " + TOTAL_GENERATIONS);
      generatePopulation(Math.max(0,INITIAL_POPULATION_SIZE - population.size()));
      simulateNaturalSelection();
      reproduction();
      currentGeneration++;
      findBest();   
    }
  }

  // this method generates an initial population of random tours and runs 2-opt on it
  private void generatePopulation(int pop) {
    System.out.println("GENERATING: " + pop + " tours");
    for(int i = 0; i < pop; i++) {
      Tour temp = new RandomTour().generateRandomTour(states);
      temp = new TwoOptAlgorithm().runTwoOptSwap(temp);
      //temp = new NearestNeighbour(states).nearestNeighbours(temp);
      //temp = runTwoOpt(temp);

      population.add(temp);
    }
  }

  // Reproduction Phase of the algorith,
  private void reproduction() {

    System.out.println("Producing " + REPRODUCTION_RATE + " new tours");
    for(int i = 0; i < REPRODUCTION_RATE; i++) {
      //System.out.println("Reproduction: " + i);

      Tour parentA = population.get(randomGenerator.nextInt(population.size()));
      Tour parentB = population.get(randomGenerator.nextInt(population.size()));
      Tour child = new OrderedCrossover().orderedCrossover(parentA, parentB);
      //child = new NearestNeighbour(states).nearestNeighbours(child);
      child = new TwoOptAlgorithm().runTwoOptSwap(child);
      population.add(child);
    }

    System.out.println("New Population Size: " + population.size());
  }

  // purge everything but the top number of options based on natural selection value
  // then regenerate more random solutions
  private void simulateNaturalSelection() {
    int bestLength = -1;
    int totalDeathAffinity = 0;

    for(Tour t : population) {
      if(bestLength == -1) {
        bestLength = t.getTourLength();
      }
      if(t.getTourLength() < bestLength) {
        bestLength = t.getTourLength();
      }
    }

    ArrayList <Integer> popDeathAffinity = new ArrayList<Integer>();
    ArrayList <Integer> cumulativeDeathAffinity = new ArrayList<Integer>();
    for(int i = 0; i < population.size(); i++) {
      Tour t = population.get(i);
      //System.out.println(t.getTourLength() - bestLength);
      int deathAffinity = (int)Math.pow(t.getTourLength() - bestLength,DEATH_POW);
      totalDeathAffinity = totalDeathAffinity + deathAffinity;
      popDeathAffinity.add(deathAffinity);
      cumulativeDeathAffinity.add(totalDeathAffinity);
      //System.out.println(totalDeathAffinity);
       
      //System.out.println(deathAffinity + " " + totalDeathAffinity);
    }
    System.out.println("Reducing population size to: " + REPLICATON_SIZE);
    while(population.size() > REPLICATON_SIZE) {
      int spinner = randomGenerator.nextInt(totalDeathAffinity);
      int index = 0;
      boolean winner = false;
      for(int i = 0; i < popDeathAffinity.size() - 1; i++) {

        // spinner greater than cumlitative prob, so nope you lose
        if(spinner > cumulativeDeathAffinity.get(i)) {
        }
        // no winner, so you win, yay time to die
        if(spinner <= cumulativeDeathAffinity.get(i) && !winner) {
          winner = true;
          index = i;
          //System.out.println(index);
        }
        // reduce cumulative probabilities
        if(spinner <= cumulativeDeathAffinity.get(i) && winner){
          int oldAffinity = cumulativeDeathAffinity.get(i);
          cumulativeDeathAffinity.set(i, oldAffinity - popDeathAffinity.get(i));
        }
      }

      population.remove(index);
      popDeathAffinity.remove(index);
      cumulativeDeathAffinity.remove(index);
    }
    //System.out.println(deathAffinity/totalDeathAffinity);



    //generatePopulation(POPULATION_SIZE - GENERATION_SURVIVAL_RATE);

  }

  // returns the best tour found by this genetic algorithm
  public Tour findBest() {
    int bestLength = -1;
    Tour bestTour = new Tour(TSPConstants.TOUR_SIZE);
    for(Tour t : population) {
      if(bestLength == -1) {
        bestLength = t.getTourLength();
        bestTour = t;
      }
      else if(t.getTourLength() < bestLength){
        bestLength = t.getTourLength();
        bestTour = t;
      }
    }
    return bestTour;
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