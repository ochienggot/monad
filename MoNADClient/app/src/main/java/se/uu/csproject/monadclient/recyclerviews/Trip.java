package se.uu.csproject.monadclient.recyclerviews;

/* This class is derived from the TravelRequest collection
 * The focus is on the assigned route rather than the request itself
 * Therefore, the class attributes are not comprehensive
 */
public class Trip {
    int requestID;
    String username;
    String startPosition; // this is the bus stop coordinate
    String startTime;
    String endPosition; // this is the bus stop coordinate
    String endTime;
    int durationMinutes;
    int userFeedback;

    public Trip(int requestID, String username, String startPosition, String startTime,
                 String endPosition, String endTime, int duration, int userFeedback) {
        this.requestID = requestID;
        this.username = username;
        this.startPosition = startPosition;
        this.startTime = startTime;
        this.endPosition = endPosition;
        this.endTime = endTime;
        this.durationMinutes = duration;
        this.userFeedback = userFeedback;
    }

    // returns time in Milliseconds
    public long getTimeToDeparture(){
        return 15000;
    }
}