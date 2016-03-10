import java.lang.*;
import java.io.File;
import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.StringTokenizer;

class ImportData {

  City city;
  ImportData() throws FileNotFoundException { 
    Scanner scanner = new Scanner(new File("cities.csv"));
    scanner.useDelimiter(",");
    int counter = 0;
    int line = 0;
    city = new City();
    while (scanner.hasNextLine() && (line < 1)) {
      String temp = scanner.nextLine();
      parseCity(temp);
      line++;
    }
    scanner.close();
    city.print();
  }

  void parseCity(String s) {
    StringTokenizer st = new StringTokenizer(s, ",");
    city.updateLatitude(Double.parseDouble(st.nextToken()));
    city.updateLongitude(Double.parseDouble(st.nextToken()));
    city.updateCity(st.nextToken());
    city.updateCounty(st.nextToken());
    city.updateState(st.nextToken());
  }
}