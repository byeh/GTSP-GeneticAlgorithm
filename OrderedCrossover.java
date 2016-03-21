import java.lang.*;
import java.io.File;
import java.util.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Map;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;

// generates tour based on ordered crossover technique
public class OrderedCrossover {

  public OrderedCrossover() {

  }

  public Tour orderedCrossover(Tour a, Tour b) {
    Random randomGenerator = new Random();
    double rand = Math.random();

    // generating cut points
    int firstCut;
    int secondCut;

    firstCut = randomGenerator.nextInt(TSPConstants.TOUR_SIZE);
    if(firstCut > TSPConstants.TOUR_SIZE / 2) {
      secondCut = (int)Math.floor((rand*rand)*(TSPConstants.TOUR_SIZE / 2)) + 1;
    }
    else {
      secondCut = TSPConstants.TOUR_SIZE - (int)Math.floor((rand*rand)*(TSPConstants.TOUR_SIZE / 2));
    }
    //System.out.println(firstCut + ", " + secondCut);

    /* generating first component of the child using tour a with index between the first cut and the second cut. Passing over the cities within the cutpoints maintained at the same position as the parent. The cities that are included are between the smaller index (inclusive) and the larger index (noninclusive)
    */

    Tour childA = generateChild(a,b,firstCut,secondCut);
    //validateTour(childA);;
    Tour childB = generateChild(b,a,firstCut,secondCut);
    //validateTour(childB);
    if(childA.getTourLength() < childB.getTourLength()) {
      return childA;
    }
    else {
      return childB;
    }
  }  

  private Tour generateChild(Tour a, Tour b, int firstCut, int secondCut){

    Tour child = new Tour(TSPConstants.TOUR_SIZE);

    //a.printTourLine();
    for(int i = Math.min(firstCut,secondCut); i < Math.max(firstCut,secondCut); i++) {
      child.addCity(a.getCity(i));
      //System.out.println(i + " " + a.getCity(i).getState());
    }
    validateTour(child);
    /* Generating the remaining component of the child using tour b with index after the second cut warpping around to the first cut. Ensuring that there is no overlap
    */
    //System.out.println(child.getTourSize()); 
    for(int i = 0; i < TSPConstants.TOUR_SIZE; i++) {
      int index = (Math.max(firstCut,secondCut) + i)%TSPConstants.TOUR_SIZE;
      City c = b.getCity(index);

      if(child.getTourSize() < TSPConstants.TOUR_SIZE) {
        if(!child.containState(c)) {
          //System.out.println("ADDING:" + index + " " + c.getState());
          child.addCity(c);
        }
      } 
    }

    //System.out.println(child.getTourSize());
    validateTour(child);
    return child;
    //System.out.println(TSPConstants.TOUR_SIZE - Math.abs(firstCut - secondCut));
  }

  private void validateTour(Tour t) {

    HashMap <String,String> state  = new HashMap<String,String>();
    for(int i = 0; i < t.getTourSize(); i++) {
      City c = t.getCity(i);
      if(state.containsKey(c.getState())) {
        System.out.println("ERROR: DUPLICATE STATES:" + c.getState() + " at: " + i);

        System.exit(6);
      }
      else {
        state.put(c.getState(), c.getCity());
      }
    }
  }

}