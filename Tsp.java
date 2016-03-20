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


  Tsp() throws FileNotFoundException { 
    states = new HashMap<String,ArrayList<City>>();
    states = new ImportData().importData();

    GeneticAlgorithm data = new GeneticAlgorithm(states);
    }

  // entry point for the application
  public static void main(String[] args) throws FileNotFoundException{
    Tsp tsp = new Tsp();
  }
}