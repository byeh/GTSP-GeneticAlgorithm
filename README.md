# CO353 - A Travelling Salesman Problem
CO353 TSP Project

# Overview

This project is an attempt at implementing a genetic algorithm to solve a large scale Generalized Travelling Salesman Problem (GTSP). The genetic heuristic algorithm implemented here is inspired by the genetic algorithm published in the paper "The Generalized Traveling Salesman Problem: A New Genetic Algorithm Approach" by John Silberholz and Bruce L. Golden (`http://josilber.scripts.mit.edu/GTSP.pdf`).

In their paper, they test instances of the GTSP with up to 1,000 nodes, but with a large number of small clusters of approximately 5 nodes each. The goal for this project is to adopt heuristic methods from literature to determine whether this implemnetation would be effective in cases where there are significantly more nodes (over 100,000) and fewer clusters (49). 

# Dependencies
 + Python
 + Java 8 `http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html`

 # Setup

 To enable Google Static Maps API visualization, please add the enviroment variable `GOOGLE_MAPS_API_KEY="PUT_KEY_HERE"` to your `.bashrc` or equivalent.
 
 There exists a makefile that will automatically build and run the java application. Simply just type `make run` on the command line. 
 
 Note that there exists a lot of verbose output for debugging and investigating the performance of the algorithm in each iteration.

<!--
 To run the program without allowing imports simply type `make run` in the command line. To run the program and allow tour imports, simply type `make import` in the command line. To add data to a tour, simply add 49 lines of cities in a row to the file `bestTours.txt`. In addition, they have to be in the format `lat|long|city|county|state` and no new lines be present.
 
  To modify input parameters, Lines 15-20 in GeneticAlgorithm.java can be modified then recompiled. 
-->



