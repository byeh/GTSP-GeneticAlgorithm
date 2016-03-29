# CO353
CO353 TSP Project

# Dependencies
 + Python
 + Java 8 `http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html`

 # How to run
 To run the python script simply execute `python parse.py`

 There exists a makefile that will automatically build and run the java application. There is an optional parameter to allow the import of existing tours in a bestTours.txt file. 

 To run the program without allowing imports simply type `make run` in the command line. To run the program and allow tour imports, simply type `make import` in the command line. To add data to a tour, simply add 49 lines of cities in a row to the file `bestTours.txt`. In addition, they have to be in the format `lat|long|city|county|state` and no new lines be present.

 To enable Google Maps API visualization, please add the enviroment variable `GOOGLE_MAPS_API_KEY="PUT_KEY_HERE"` to your `.bashrc` or equivalent. For bash `export GOOGLE_MAPS_API_KEY="PUT_KEY_HERE"` is sufficient.

 To modify input parameters, Lines 15-20 in GeneticAlgorithm.java can be modified then recompiled. 