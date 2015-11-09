package se.uu.csproject.monadclient;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import se.uu.csproject.monadclient.recyclerviews.FullTrip;
import se.uu.csproject.monadclient.recyclerviews.PartialTrip;
import se.uu.csproject.monadclient.recyclerviews.SearchRecyclerViewAdapter;
import se.uu.csproject.monadclient.recyclerviews.TripRecyclerViewAdapter;


public class TripsActivity extends MenuedActivity implements AsyncResponse{
    private Toolbar toolbar;
    private ImageButton button;
    private Context context;

    //TODO Ilyass: (high priority) Make specific trips selectable
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trips);
        toolbar = (Toolbar) findViewById(R.id.actionToolBar);
        setSupportActionBar(toolbar);
        context = getApplicationContext();

        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        getBookings();
    }

    private void getBookings(){
        String userId = ClientAuthentication.getClientId();
        SendUserBookingsRequest asyncTask = new SendUserBookingsRequest();
        asyncTask.delegate = this;
        asyncTask.execute(userId);
    }

    public void processFinish(ArrayList<FullTrip> bookings){
        if (bookings.isEmpty()){
            CharSequence text = "You haven't booked any trips.";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view_active);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            recyclerView.setLayoutManager(linearLayoutManager);
            TripRecyclerViewAdapter adapter = new TripRecyclerViewAdapter(bookings);
            recyclerView.setAdapter(adapter);
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//
//        if(ClientAuthentication.getPassword().equals("0")){
//            // Inflate the menu; this adds items to the action bar if it is present.
//            getMenuInflater().inflate(R.menu.menu_main_google, menu);
//            return true;
//        }
//        else {
//            getMenuInflater().inflate(R.menu.menu_main, menu);
//            return true;
//        }
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        if (id == android.R.id.home) {
//            NavUtils.navigateUpFromSameTask(this);
//        }
//
//        if (id == R.id.action_search) {
//            startActivity(new Intent(this, MainActivity.class));
//        }
//
//        if (id == R.id.action_notifications) {
//            startActivity(new Intent(this, NotificationsActivity.class));
//        }
//
//        if (id == R.id.action_mytrips) {
//            return true;
//        }
//
//        if (id == R.id.action_profile) {
//            startActivity(new Intent(this, ProfileActivity.class));
//        }
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            startActivity(new Intent(this, SettingsActivity.class));
//        }
//
//        if (id == R.id.action_aboutus) {
//            //TODO (low priority): Create a toaster with text about the MoNAD project and team
//            startActivity(new Intent(this, AboutUsActivity.class));
//        }
//
//        if (id == R.id.action_signout) {
//            startActivity(new Intent(this, LoginActivity.class));
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.action_mytrips){
            return true;
        }
        else {
            return super.onOptionsItemSelected(item);
        }
    }

    //TEMPORARY FUNCTION TODO: Remove this function once the database connection is set
    private void generateTrips(List<FullTrip> trips){
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

        ArrayList<PartialTrip> partialTrips = new ArrayList<>();
        ArrayList<String> trajectory = new ArrayList<>();
        trajectory.add("BMC");
        trajectory.add("Akademiska Sjukhuset");
        trajectory.add("Ekeby Bruk");
        trajectory.add("Ekeby");
        PartialTrip partialTrip = new PartialTrip("1",2,3, "Polacksbacken",startdate1,"Flogsta", enddate1, trajectory);
        partialTrips.add(partialTrip);
        trips.add(new FullTrip("1", "2", partialTrips, 10, true, 0));
        partialTrip = new PartialTrip("1",2,3, "Gamla Uppsala",startdate2,"Gottsunda", enddate2, trajectory);
        partialTrips.clear(); partialTrips.add(partialTrip);
        trips.add(new FullTrip("2", "3", partialTrips, 15, true, 0));
        partialTrip = new PartialTrip("1",2,3, "Granby",startdate3,"Tunna Backar", enddate3, trajectory);
        partialTrips.clear(); partialTrips.add(partialTrip);
        trips.add(new FullTrip("3", "4", partialTrips, 15, true, 0));
        partialTrip = new PartialTrip("1",2,3, "Kungsgatan", startdate4, "Observatoriet", enddate4, trajectory);
        partialTrips.clear(); partialTrips.add(partialTrip);
        trips.add(new FullTrip("4", "5", partialTrips, 30, true, 0));
    }
}