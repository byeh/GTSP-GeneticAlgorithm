all:
		javac Tsp.java GeneticAlgorithm.java City.java Tour.java ImportData.java TSPConstants.java DisplayResults.java TwoOptAlgorithm.java RandomTour.java OrderedCrossover.java NearestNeighbour.java
run: all
			java Tsp
import: all
	       java Tsp import