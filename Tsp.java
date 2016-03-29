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

  Tsp(boolean b) throws FileNotFoundException { 
    states = new ImportData().importData();
    long startTime = System.currentTimeMillis();
    data = new GeneticAlgorithm(states, b);
    bestTour = data.findBest();
    bestTour.printTourLine();
    new DisplayResults().print(bestTour);
    showMetrics(startTime);
    }

  // entry point for the application
  public static void main(String[] args) throws FileNotFoundException{
    if(args.length == 1 && args[0].equals("import")) {
      Tsp tsp = new Tsp(true);
    }
    else {
      Tsp tsp = new Tsp(false);
    }
  }

  // print some metrics
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