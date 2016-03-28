import java.lang.*;
import java.io.File;
import java.util.*;
import java.util.Random;
import java.util.Scanner;
import java.util.Map;
import java.lang.Math;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;

public class TwoOptAlgorithm {

  public TwoOptAlgorithm() {

  }

  public Tour runTwoOpt(Tour t) {
    Tour best = new Tour(t);
    best = runTwoOptHelper(best,0);
    return best;
  }

  public Tour runTwoOptSwap(Tour t) {
    Tour best = new Tour(t);
    best = runTwoOptHelper(best,1);
    return best;  
  }

  public Tour runSwap(Tour t) {
    return runSwapHelper(t,0);
  }

  public Tour runOneSwap(Tour t) {
    return runSwapHelper(t,1);
  }

  /*
  param = 0: find all 2-opts
  param = 1: find all 2-opts and all swaps
  */
  public Tour runTwoOptHelper(Tour t, int param) {
    Tour bestTour = t;
    // for any this edge
    for(int i = 0; i < TSPConstants.TOUR_SIZE; i++) {

      for(int k = 0; k < Math.min((TSPConstants.TOUR_SIZE- (i+2)), TSPConstants.TOUR_SIZE - 3); k++) {
        // vertex indicies of the 2 selected edges
        int a = i;
        int b = (i+1)%TSPConstants.TOUR_SIZE;
        int c = (k+i+2)%TSPConstants.TOUR_SIZE;
        int d = (k+i+3)%TSPConstants.TOUR_SIZE;

        double distanceAB = bestTour.computeEdge(a,b);
        double distanceCD = bestTour.computeEdge(c,d);
        double distanceAC = bestTour.computeEdge(a,c);
        double distanceBD = bestTour.computeEdge(b,d);

         // use the damn triangle inequality
        if(!((distanceAB + distanceCD) <= (distanceAC + distanceBD))) {
          Tour newTour = new Tour(TSPConstants.TOUR_SIZE);

          // end of first subtour
          for(int j = 0; j < a; j++) {
            newTour.addCity(bestTour.getCity(j));
          }

          // new edge AC
          newTour.addCity(bestTour.getCity(a));
          newTour.addCity(bestTour.getCity(c));

          for(int j = c - 1; j > b; j--) {
            newTour.addCity(bestTour.getCity(j));
          }
          // add edge BD
          newTour.addCity(bestTour.getCity(b));
          newTour.addCity(bestTour.getCity(d));

          // continue on
          for(int j = d+1; j < TSPConstants.TOUR_SIZE; j++) {
            newTour.addCity(bestTour.getCity(j));
          }

          newTour.computeTourLength();
          //System.out.println(newTour.getTourLength());
          bestTour = newTour;
          i = 0;
          k = 0;
        }
      }
    }
    bestTour.computeTourLength();
    if(param > 0) {
      bestTour = runSwap(bestTour);
    }
    
    return bestTour;
  }

  /* 
  param = 0: Run all swaps
  param = 1: Run one swap
  */
  private Tour runSwapHelper(Tour t, int param) {

    Tour bestTour = t;
    for(int i = 0; i < t.getTourSize() - 1; i++) {
      for(int j = i+1; j < t.getTourSize(); j++) {
        Tour currentTour = bestTour.swapCity(i,j);
        currentTour.computeTourLength();
        if(currentTour.getTourLength() < bestTour.getTourLength()) {
          bestTour = currentTour;
          bestTour = runTwoOpt(bestTour);
          if(param == 0) {
            i = 0;
            j = i+1;           
          }

        }
      }
    }
    return bestTour;
  }
}