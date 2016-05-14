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
  private int INITIAL_POPULATION_SIZE = 10;
  private int NUMBER_OF_ISOLATED_POPULATIONS = 2;
  private int TOTAL_GENERATIONS = 25;
  private int REPLICATON_SIZE = (int) (INITIAL_POPULATION_SIZE * 0.4);
  private int REPRODUCTION_RATE = (int) (INITIAL_POPULATION_SIZE * 0.6);
  private int TERMINATION_CONDITION = 25;

  private int currentGeneration = 1;
  private static final double DEATH_POW = 0.375;
  private static final double REPROD_POW = 0.375;
  private static final double MUTATION_POW = 0.05;

  //private ArrayList <Tour> population;
  private ArrayList<ArrayList<Tour>> isolatedPopulation;
  // list of all states and cities contained in that state
  public HashMap <String, ArrayList<City>> states;
  private Random randomGenerator = new Random();
  private long startTime;
  private boolean importData = false;
  private int maxStagnancy = 0;

  // lets run this cool genetic algorithm
  public GeneticAlgorithm(HashMap <String, ArrayList<City>> s, boolean i) throws FileNotFoundException { 
    states = s;
    importData = i;
    isolatedPopulation = new ArrayList<ArrayList<Tour>>();   
    startTime = System.currentTimeMillis();
    run();
  }

  private void run() throws FileNotFoundException {
    // setup experiment
    setParameters();

    for(int i = 0; i < NUMBER_OF_ISOLATED_POPULATIONS; i++) {
      ArrayList<Tour> temp = new ArrayList<Tour>();
      isolatedPopulation.add(temp);
    }
    // run experiment
    runGeneticAlgorithm(TOTAL_GENERATIONS);
  }

  private void run2() {
    for(int i = 0; i <= NUMBER_OF_ISOLATED_POPULATIONS; i++) {
      ArrayList<Tour> temp = new ArrayList<Tour>();
      isolatedPopulation.add(temp);
    }
    Tour t = new RandomTour().generateRandomTour(states);
    isolatedPopulation.get(NUMBER_OF_ISOLATED_POPULATIONS).add(t);
  }

  private void setParameters() {
    System.out.println("Running Experiment with the following parameters");
    System.out.println("Initial Isolated Population Size: " + INITIAL_POPULATION_SIZE);
    System.out.println("Number of Isolated Populations: " + NUMBER_OF_ISOLATED_POPULATIONS);
    System.out.println("Number of generations for Isolated populations: " + TOTAL_GENERATIONS);
    System.out.println("Replication Size: " + REPLICATON_SIZE);
    System.out.println("Reproduction Rate: " + REPRODUCTION_RATE);
    System.out.println("Termination Generation for merged population: " + TERMINATION_CONDITION);
  }  

  private void runGeneticAlgorithm(int cycles) throws FileNotFoundException{

    for(int i = 0; i < TOTAL_GENERATIONS; i++) {
      System.out.println("Currently on generation: " + (i+1) + " of " + TOTAL_GENERATIONS);
      for(int j = 0; j < NUMBER_OF_ISOLATED_POPULATIONS; j++) {
        ArrayList<Tour> population = isolatedPopulation.get(j);
        generatePopulation(Math.max(0,INITIAL_POPULATION_SIZE - population.size()), j);
        simulateNaturalSelection(j);
        reproduction(j);   
      }   
      currentGeneration++;
      showMetrics(startTime);
    }
    mergePopulation();
    System.out.println("Max Generations Stagnant: " + maxStagnancy);
  }

  // this method generates an initial population of random tours and runs 2-opt on it
  private void generatePopulation(int populationSize, int location) throws FileNotFoundException{
    //System.out.println("GENERATING: " + populationSize + " tours");
    ArrayList<Tour> population = isolatedPopulation.get(location);
    if(location == 0 && currentGeneration == 1 && importData) {
      ArrayList<Tour> t = new ImportData().importTours();
      for(Tour importTour : t) {
        population.add(importTour);
      }
      for(int i = 0; i < populationSize - population.size(); i++) {
        Tour temp = new RandomTour().generateRandomTour(states);
        temp = new TwoOptAlgorithm().runTwoOpt(temp);
        population.add(temp);
      }      
    }
    else {
      for(int i = 0; i < populationSize; i++) {
        Tour temp = new RandomTour().generateRandomTour(states);
        temp = new TwoOptAlgorithm().runTwoOpt(temp);
        population.add(temp);
      }      
    }
  }

  // Reproduction Phase of the algorith,
  private void reproduction(int location) {
    ArrayList<Tour> population = isolatedPopulation.get(location);
    //System.out.println("Producing " + REPRODUCTION_RATE + " new tours");
    int reprodNum;
    if(location < NUMBER_OF_ISOLATED_POPULATIONS) {
      reprodNum = REPRODUCTION_RATE;
    }
    else {
      reprodNum = (int) (INITIAL_POPULATION_SIZE * NUMBER_OF_ISOLATED_POPULATIONS * 0.6);
    }

    for(int i = 0; i < reprodNum; i++) {
      Tour parentA = population.get(randomGenerator.nextInt(population.size()));
      Tour parentB = population.get(randomGenerator.nextInt(population.size()));
      Tour child;
      if(location == NUMBER_OF_ISOLATED_POPULATIONS) {
        child = new OrderedCrossover().mrOrderedCrossover(parentA, parentB,  states);
        child = new TwoOptAlgorithm().runTwoOpt(child);
      }
      else {
        child = new OrderedCrossover().rOrderedCrossover(parentA, parentB,  states);
      }
      population.add(child);
    }
  }

  private void simulateNaturalSelection(int location) {
    ArrayList<Tour> population = isolatedPopulation.get(location);
    //System.out.println("Reducing population to best : " + REPLICATON_SIZE + " tours");
    HashMap<Integer,Tour> temp = new HashMap<Integer,Tour>();
    for(Tour t : population) {
      temp.put(t.getTourLength(), t);
    }
    int replicateNum;
    if(location == NUMBER_OF_ISOLATED_POPULATIONS) {
      replicateNum = (int)( INITIAL_POPULATION_SIZE * NUMBER_OF_ISOLATED_POPULATIONS * 0.4);
    }
    else {
      replicateNum = REPLICATON_SIZE;
    }

    ArrayList<Integer> orderedPopulation = new ArrayList(temp.keySet());
    Collections.sort(orderedPopulation);
    population.clear();
    for(int i = 0; i < Math.min(replicateNum, orderedPopulation.size()); i++) {
      if(i < 10) {
        Tour t = temp.get(orderedPopulation.get(i));
        t = new TwoOptAlgorithm().runTwoOpt(t);
      }
      population.add(temp.get(orderedPopulation.get(i)));
    }
  }

  // merge the isolated populations
  public void mergePopulation() {
    ArrayList<Tour> combinedPopulation = new ArrayList<Tour>();
    for(ArrayList<Tour> tours : isolatedPopulation) {
      for(Tour t : tours) {
        Tour localOpt = new TwoOptAlgorithm().runTwoOptSwap(t);
        combinedPopulation.add(localOpt);
      }
    }
    isolatedPopulation.add(combinedPopulation);
    System.out.println("Optimizing Combined Population of: " + combinedPopulation.size());
    int counter = 0;
    int stagnancy = 0;
    Tour bestTour = findBest();
    while(true) {
      System.out.println("On generation: " + counter);
      simulateNaturalSelection(NUMBER_OF_ISOLATED_POPULATIONS);
      reproduction(NUMBER_OF_ISOLATED_POPULATIONS);
      Tour temp = findBest();
      if(temp.getTourLength() < bestTour.getTourLength()) {
        auditData(bestTour);
        bestTour = temp;
        stagnancy = 0;
      }
      else {
        stagnancy++;
        if(maxStagnancy < stagnancy) {
          maxStagnancy = stagnancy;
        }
        System.out.println("Tour length stagnated for: " + stagnancy + " generations.");
        if(counter > TERMINATION_CONDITION && maxStagnancy > 10) {
          break;
        }
      }
      System.out.println("Best Tour Length:" + bestTour.getTourLength());
      showMetrics(startTime);
      counter++;
    }
  }

  public Tour findBest() {
    ArrayList<Tour> population = isolatedPopulation.get(NUMBER_OF_ISOLATED_POPULATIONS);
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


  private void auditData(Tour t) {
    HashMap<String, Integer> test = new HashMap<String,Integer>();
    for(int i = 0; i < TSPConstants.TOUR_SIZE; i++) {
      test.put(t.getCity(i).getState(), 1);
    }

    if(test.size() < TSPConstants.TOUR_SIZE) {
      System.out.println("ERROR: invalid tour");
      System.exit(10);
    }

  }
  public void showMetrics(long startTime) {
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