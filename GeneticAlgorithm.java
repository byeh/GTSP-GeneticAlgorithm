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
  private int INITIAL_POPULATION_SIZE = 50;
  private int NUMBER_OF_ISOLATED_POPULATIONS = 10;
  private int TERMINATION_CONDITION = 150;
  private int REPLICATON_SIZE = (int) (INITIAL_POPULATION_SIZE * 0.4);
  private int REPRODUCTION_RATE = (int) (INITIAL_POPULATION_SIZE * 0.6);
  private int TOTAL_GENERATIONS = 10;
  private int currentGeneration = 1;
  private static final double DEATH_POW = 0.375;
  private static final double REPROD_POW = 0.375;
  private static final double MUTATION_POW = 0.05;

  //private ArrayList <Tour> population;
  private ArrayList<ArrayList<Tour>> isolatedPopulation;
  // list of all states and cities contained in that state
  public HashMap <String, ArrayList<City>> states;
  private Random randomGenerator = new Random();

  // lets run this cool genetic algorithm
  public GeneticAlgorithm(HashMap <String, ArrayList<City>> s) throws FileNotFoundException { 
    states = s;
    isolatedPopulation = new ArrayList<ArrayList<Tour>>();   

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

    // get results
    
  }

  private void setParameters() {
    System.out.println("Running Experiment with population size of:" +INITIAL_POPULATION_SIZE); 
  }  

  private void runGeneticAlgorithm(int cycles) {

    for(int i = 0; i < TOTAL_GENERATIONS; i++) {
      System.out.println("Currently on generation: " + (i+1) + " of " + TOTAL_GENERATIONS);
      for(int j = 0; j < NUMBER_OF_ISOLATED_POPULATIONS; j++) {
        ArrayList<Tour> population = isolatedPopulation.get(j);
        generatePopulation(Math.max(0,INITIAL_POPULATION_SIZE - population.size()), j);
        simulateNaturalSelectionB(j);
        reproduction(j);   
      }   
    }
    mergePopulation();
  }

  // this method generates an initial population of random tours and runs 2-opt on it
  private void generatePopulation(int populationSize, int location) {
    //System.out.println("GENERATING: " + populationSize + " tours");
    ArrayList<Tour> population = isolatedPopulation.get(location);
    for(int i = 0; i < populationSize; i++) {
      Tour temp = new RandomTour().generateRandomTour(states);
      temp = new TwoOptAlgorithm().runTwoOpt(temp);
      population.add(temp);
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
      //System.out.println("Reproduction: " + i);

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
      //child = new NearestNeighbour(states).nearestNeighbours(child);
      //child = new TwoOptAlgorithm().runTwoOpt(child);
      population.add(child);
    }

    //System.out.println("New Population Size: " + population.size());
  }

  // purge everything but the top number of options based on natural selection value
  // then regenerate more random solutions
  private void simulateNaturalSelectionA(int location) {
    ArrayList<Tour> population = isolatedPopulation.get(location);
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
    //System.out.println("Reducing population size to: " + REPLICATON_SIZE);
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
          totalDeathAffinity = totalDeathAffinity - popDeathAffinity.get(index);
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
  }

  private void simulateNaturalSelectionB(int location) {
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
    for(int i = 0; i < replicateNum; i++) {
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
    Tour bestTour = findBest();
    while(true) {
      System.out.println("On generation: " + counter);
      System.out.println("Population size: " + isolatedPopulation.get(NUMBER_OF_ISOLATED_POPULATIONS).size());
      simulateNaturalSelectionB(NUMBER_OF_ISOLATED_POPULATIONS);
      reproduction(NUMBER_OF_ISOLATED_POPULATIONS);
      Tour temp = findBest();
      if(temp.getTourLength() < bestTour.getTourLength()) {
        bestTour = temp;
      }
      else {
        if(counter > TERMINATION_CONDITION) {
          break;
        }
      }
      new DisplayResults().print(bestTour);
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


  private void auditData() {
        // verify that tour generated is valid size
    // if(tour.getTourSize() != TSPConstants.NUMBER_STATES) {
    //   System.out.println("ERROR: Number of States in tour is: " + tour.getTourSize());
    //   System.exit(4);      
    // }
  }


}