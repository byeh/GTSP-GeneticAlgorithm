import java.lang.*;
import java.util.Map;
import java.util.*;
import java.util.ArrayList;

public class TSPConstants {
  // ------------------------------------------------------
  //
  // Some defined constants for the problem we are solving
  //
  // ------------------------------------------------------
  public static final int NUMBER_STATES = 49;
  public static final int TOUR_SIZE = 49;
  public static final int NUMBER_CITIES = 115475;
  public static final String[] STATE_LIST = {
    "AL", "AZ", "AR", "CA", "CO", "CT", "DC",
    "DE", "FL", "GA", "ID", "IL", "IN", 
    "IA", "KS", "KY", "LA", "ME", "MD", 
    "MA", "MI", "MN", "MS", "MO", "MT", 
    "NE", "NV", "NH", "NJ", "NM", "NY", 
    "NC", "ND", "OH", "OK", "OR", "PA", 
    "RI", "SC", "SD", "TN", "TX", "UT", 
    "VT", "VA", "WA", "WV", "WI", "WY"};

  public static final HashMap <String, String> STATE_NAME;
  static {
    STATE_NAME = new HashMap<String, String>();
    STATE_NAME.put("AL", "Alabama");
    STATE_NAME.put("AZ","Arizona");
    STATE_NAME.put("AR","Arkansas");
    STATE_NAME.put("CA","California");
    STATE_NAME.put("CO","Colorado");
    STATE_NAME.put("CT","Connecticut");
    STATE_NAME.put("DE","Delaware");
    STATE_NAME.put("DC","District Of Columbia");
    STATE_NAME.put("FL","Florida");
    STATE_NAME.put("GA","Georgia");
    STATE_NAME.put("ID","Idaho");
    STATE_NAME.put("IL","Illinois");
    STATE_NAME.put("IN","Indiana");
    STATE_NAME.put("IA","Iowa");
    STATE_NAME.put("KS","Kansas");
    STATE_NAME.put("KY","Kentucky");
    STATE_NAME.put("LA","Louisiana");
    STATE_NAME.put("ME","Maine");
    STATE_NAME.put("MD","Maryland");
    STATE_NAME.put("MA","Massachusetts");
    STATE_NAME.put("MI","Michigan");
    STATE_NAME.put("MN","Minnesota");
    STATE_NAME.put("MS","Mississippi");
    STATE_NAME.put("MO","Missouri");
    STATE_NAME.put("MT","Montana");
    STATE_NAME.put("NE","Nebraska");
    STATE_NAME.put("NV","Nevada");
    STATE_NAME.put("NH","New Hampshire");
    STATE_NAME.put("NJ","New Jersey");
    STATE_NAME.put("NM","New Mexico");
    STATE_NAME.put("NY","New York");
    STATE_NAME.put("NC","North Carolina");
    STATE_NAME.put("ND","North Dakota");
    STATE_NAME.put("OH","Ohio");
    STATE_NAME.put("OK","Oklahoma");
    STATE_NAME.put("OR","Oregon");
    STATE_NAME.put("PA","Pennsylvania");
    STATE_NAME.put("RI","Rhode Island");
    STATE_NAME.put("SC","South Carolina");
    STATE_NAME.put("SD","South Dakota");
    STATE_NAME.put("TN","Tennessee");
    STATE_NAME.put("TX","Texas");
    STATE_NAME.put("UT","Utah");
    STATE_NAME.put("VT","Vermont");
    STATE_NAME.put("VA","Virginia");
    STATE_NAME.put("WA","Washington");
    STATE_NAME.put("WV","West Virginia");
    STATE_NAME.put("WI","Wisconsin");
    STATE_NAME.put("WY","Wyoming");    
  }

}
 
  