  public class Location {

    Double latitude;
    Double longitude;

    public Location(Double lat, Double lon) {
      latitude = lat;
      longitude = lon;
    }

    public void updateLatitude(double l) {
      latitude = l;
    }

    public void updateLongitude(double l) {
      longitude = l;
    }

    public void print() {
      System.out.println("Latitude: " + latitude + ", Longitude: " + longitude);
    }
  }