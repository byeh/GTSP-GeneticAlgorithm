import java.lang.*;

class City {

  Double latitude;
  Double longitude;
  String city;
  String county;
  String state;
  City() { 
    latitude = 0.0;
    longitude = 0.0;
    city = "";
    county = "";
    state = "";
  }

  void updateLatitude(double l) {
    latitude = l;
  }

  void updateLongitude(double l) {
    longitude = l;
  }

  void updateCity(String c) {
    city = c;
  }

  void updateCounty(String c) {
    county = c;
  }

  void updateState(String s) {
    state = s;
  }

  void print() {
    System.out.println(latitude);
    System.out.println(longitude);
    System.out.println(city);
    System.out.println(county);
    System.out.println(state);
  }
} 