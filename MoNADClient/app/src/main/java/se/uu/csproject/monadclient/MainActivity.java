package se.uu.csproject.monadclient;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import se.uu.csproject.monadclient.recyclerviews.SearchRecyclerViewAdapter;
import se.uu.csproject.monadclient.recyclerviews.Trip;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private Toolbar toolbar;
    private EditText destination;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.actionToolBar);
        destination = (EditText) findViewById(R.id.main_search_destination);
        setSupportActionBar(toolbar);

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        List<Trip> searchResults = new ArrayList<>();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_main);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(linearLayoutManager);
        generateSearchResults(searchResults);
        SearchRecyclerViewAdapter adapter = new SearchRecyclerViewAdapter(searchResults);
        recyclerView.setAdapter(adapter);
        buildGoogleApiClient();
        initializeLocationRequest();
        mGoogleApiClient.connect();
    }

    public void openMainSearch (View view) {
        // Make a quick search based on the current time and the user's current location
        String startPositionLatitude, startPositionLongitude, edPosition, userId, startTime, endTime;
        String requestTime, priority;
        Date now = new Date();
        SimpleDateFormat df = new SimpleDateFormat("yyyy EEE dd MMM HH:mm");

        // Provide some default values since this is a quick search
        startPositionLatitude = String.valueOf(currentLatitude);
        startPositionLongitude = String.valueOf(currentLongitude);
        edPosition = destination.getText().toString();
        userId = ClientAuthentication.getClientId();
        startTime = df.format(now);
        endTime = "null";
        requestTime = df.format(now);
        priority = "distance";

        Log.d("oops", "latitude: " + startPositionLatitude);
        Log.d("oops", "longitude: " + startPositionLongitude);
        new SendQuickTravelRequest().execute(userId, startTime, endTime, requestTime, startPositionLatitude,
                startPositionLongitude, edPosition, priority);

        Intent myIntent = new Intent(MainActivity.this, SearchActivity.class);
        MainActivity.this.startActivity(myIntent);
    }

    private void handleNewLocation(Location location) {
        currentLatitude = location.getLatitude();
        currentLongitude = location.getLongitude();
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    protected void initializeLocationRequest() {
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY)
                .setInterval(600 * 1000)        // 10 minutes, in milliseconds
                .setFastestInterval(60 * 1000); // 1 minute, in milliseconds
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onTouchEvent(MotionEvent event) {
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.
                INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == android.R.id.home){
            NavUtils.navigateUpFromSameTask(this);
        }

        if (id == R.id.action_search) {
            return true;
        }

        if (id == R.id.action_notifications) {
            startActivity(new Intent(this, NotificationsActivity.class));
        }

        if (id == R.id.action_mytrips) {
            startActivity(new Intent(this, TripsActivity.class));
        }

        if (id == R.id.action_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
        }

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }

        if (id == R.id.action_aboutus) {
            //TODO (low priority): Create a toaster with text about the MoNAD project and team
            startActivity(new Intent(this, AboutUsActivity.class));
        }

        if (id == R.id.action_signout) {
            startActivity(new Intent(this, LoginActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }

    public void openTripDetail (View v) {
        //opens Route activity layout
        startActivity(new Intent(this, RouteActivity.class));
    }

    //TEMPORARY FUNCTION TODO: Remove this function once the database connection is set
    private void generateSearchResults(List<Trip> trips){
        Calendar calendar = new GregorianCalendar(2015, 10, 26, 10, 40, 0);
        Date startdate1 = calendar.getTime();
        calendar = new GregorianCalendar(2015, 10, 26, 10, 50, 0);
        Date enddate1 = calendar.getTime();
        calendar = new GregorianCalendar(2015, 10, 26, 10, 45, 0);
        Date startdate2 = calendar.getTime();
        calendar = new GregorianCalendar(2015, 10, 26, 11, 0, 0);
        Date enddate2 = calendar.getTime();
        calendar = new GregorianCalendar(2015, 10, 27, 9, 50, 0);
        Date startdate3 = calendar.getTime();
        calendar = new GregorianCalendar(2015, 10, 27, 10, 5, 0);
        Date enddate3 = calendar.getTime();
        calendar = new GregorianCalendar(2015, 10, 22, 11, 30, 0);
        Date startdate4 = calendar.getTime();
        calendar = new GregorianCalendar(2015, 10, 22, 12, 0, 0);
        Date enddate4 = calendar.getTime();
        trips.add(new Trip(1, "Polacksbacken",startdate1,"Flogsta", enddate1, 10, 0));
        trips.add(new Trip(2, "Gamla Uppsala",startdate2,"Gottsunda", enddate2, 15, 0));
        trips.add(new Trip(3, "Granby",startdate3,"Tunna Backar", enddate3, 15, 0));
        trips.add(new Trip(4, "Kungsgatan", startdate4, "Observatoriet", enddate4, 30, 0));
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!mGoogleApiClient.isConnected()) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            mGoogleApiClient.disconnect();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            // This exception will be thrown on android 6 devices
            // TODO: Figure out a way to ask for location permissions at runtime per the new Android 6
            // TODO: specifications, while also maintaining support for older Android versions
            try{
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            }catch(java.lang.SecurityException e){
                Log.d("oops", e.toString());
            }
        }
        else {
            handleNewLocation(location);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.d("oops", Integer.toString(connectionResult.getErrorCode()));
    }

    @Override
    public void onLocationChanged(Location location) {
        handleNewLocation(location);
    }
}
