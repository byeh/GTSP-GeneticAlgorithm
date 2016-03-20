import java.lang.*;
import java.io.File;
import java.util.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Map;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;

// This class handles output of final results
public class DisplayResults {

  // Google Maps related Constants
  private static final String GOOGLE_MAPS_API_KEY = "&key=" + System.getenv("GOOGLE_MAPS_API_KEY");
  private static final String GOOGLE_MAPS_URL_HEAD = "https://maps.googleapis.com/maps/api/staticmap?center=39.833333,-95.583333&zoom=4&size=640x640&maptype=roadmap&path=color:0xff0000ff|weight:3|";

  public DisplayResults() {

  }

  public void print(Tour t) {
      System.out.print(GOOGLE_MAPS_URL_HEAD);
      t.printTour();
      System.out.print(GOOGLE_MAPS_API_KEY + '\n');
      System.out.println("Best Tour Length: " + t.getTourLength());      
  }

  // generates a Google Static Map with the tour
  private void printTour(Tour t) {
    ArrayList <City> tour = t.getTour();
    for(City city : tour) {
      System.out.print(city.getX() + "," + city.getY() + "|");
    }
  }  
}