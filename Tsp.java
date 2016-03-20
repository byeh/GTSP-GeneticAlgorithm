import java.lang.*;
import java.io.File;
import java.util.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Map;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;
class Tsp {

  // input data of cities split into states
  private HashMap <String, ArrayList<City>> states;
  private GeneticAlgorithm data;
  private Tour bestTour;

  Tsp() throws FileNotFoundException { 
    states = new ImportData().importData();
    data = new GeneticAlgorithm(states);
    bestTour = data.findBest();
    new DisplayResults().print(bestTour);
    }

  // entry point for the application
  public static void main(String[] args) throws FileNotFoundException{
    Tsp tsp = new Tsp();
  }
}