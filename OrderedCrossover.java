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

  public Tour orderedCrossover(Tour a, Tour b, HashMap <String,ArrayList<City>> states) {
    return orderedCrossoverHelper(a,b,0,states);
  }

  public Tour rOrderedCrossover(Tour a, Tour b, HashMap <String,ArrayList<City>> states) {
    return orderedCrossoverHelper(a,b,1,states);
  }

  public Tour mrOrderedCrossover(Tour a, Tour b, HashMap <String,ArrayList<City>> states) {
    return orderedCrossoverHelper(a,b,2,states);
  }

  /* 
  type = 0: standard order crossover OX
  type = 1: include all rotations and reversals of children rOX
  type = 2: rotation of cut points through all cities mrOX
  */
  private Tour orderedCrossoverHelper(Tour a, Tour b, int type, HashMap <String, ArrayList<City>> states) {
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

    /* 
    generating first component of the child using tour a with index between the first cut and the second cut. 
    Passing over the cities within the cutpoints maintained at the same position as the parent. 
    The cities that are included are between the smaller index (inclusive) and the larger index (noninclusive)
    */

    Tour bestTour = generateChild(a,b,firstCut,secondCut,0);
    Tour temp = generateChild(b,a,firstCut,secondCut,0);
    //validateTour(childB);
    if(temp.getTourLength() < bestTour.getTourLength()) {
      bestTour = temp;
    }
    // rOX subroutine
    if(type > 0) {
      for(int i = 1; i < TSPConstants.TOUR_SIZE - Math.abs(firstCut - secondCut); i++) {
        //System.out.println(i);
        temp = generateChild(a,b,firstCut,secondCut,i);

        if(temp.getTourLength() < bestTour.getTourLength()) {
          bestTour = temp;
        }

        temp = generateChild(b,a,firstCut,secondCut,i);
        if(temp.getTourLength() < bestTour.getTourLength()) {
          bestTour = temp;
        }
      }
    }     
    // mrOX subroutine
    if(type > 1) {
      City cityA = bestTour.getCity(firstCut);
      City cityB = bestTour.getCity(secondCut - 1); 
      String stateA = cityA.getState();
      String stateB = cityB.getState();
      ArrayList<City> cityStatesA = states.get(stateA);
      ArrayList<City> cityStatesB = states.get(stateB);

      for(City c : cityStatesA) {
        Tour modifiedA = new Tour(bestTour);
        modifiedA.replaceCity(firstCut, c);
        if(modifiedA.getTourLength() < temp.getTourLength()) {
          bestTour = modifiedA;
        }
      }
      for(City d : cityStatesB) {
        Tour modifiedB = new Tour(bestTour);
        modifiedB.replaceCity(secondCut-1, d);
        if(modifiedB.getTourLength() < temp.getTourLength()) {
          bestTour = modifiedB;
        }
      }
  }
    return bestTour;
  }  

  private Tour generateChild(Tour a, Tour b, int firstCut, int secondCut, int offset){

    Tour forwardChild = new Tour(TSPConstants.TOUR_SIZE);
    Tour reverseChild = new Tour(TSPConstants.TOUR_SIZE);

    for(int i = Math.min(firstCut,secondCut); i < Math.max(firstCut,secondCut); i++) {
      forwardChild.addCity(a.getCity(i));
      reverseChild.addCity(a.getCity(i));
    }
    validateTour(forwardChild);

    /* Generating the remaining component of the child using tour b 
    with index after the second cut warpping around to the first cut. 
    Ensuring that there is no overlap
    */
    for(int i = 0; i < TSPConstants.TOUR_SIZE; i++) {
      int index = (Math.max(firstCut,secondCut) + i + offset)%TSPConstants.TOUR_SIZE;
      City c = b.getCity(index);

      if(forwardChild.getTourSize() < TSPConstants.TOUR_SIZE) {
        if(!forwardChild.containState(c)) {
          forwardChild.addCity(c);
        }
      } 
    }

    for(int i = TSPConstants.TOUR_SIZE; i > 0; i--) {
      int index = (Math.min(firstCut,secondCut) + i + offset)%TSPConstants.TOUR_SIZE;
       City c = b.getCity(index);

      if(reverseChild.getTourSize() < TSPConstants.TOUR_SIZE) {
        if(!reverseChild.containState(c)) {
          reverseChild.addCity(c);
        }
      }      
    }

    forwardChild.computeTourLength();
    reverseChild.computeTourLength();
    validateTour(forwardChild);
    validateTour(reverseChild);
    if(forwardChild.getTourLength() < reverseChild.getTourLength()) {
      return forwardChild;
    }
    else {
      return reverseChild;
    }
  }

  // Method to verify that the generated tour is valid
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