package com.matchfinder.jan.t9_mobileapp.db;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.matchfinder.jan.t9_mobileapp.R;
import com.matchfinder.jan.t9_mobileapp.activities.Homescreen;
import com.matchfinder.jan.t9_mobileapp.activities.Login;
import com.matchfinder.jan.t9_mobileapp.db.entities.Event;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/*
 * Created by taraszaika on 02.01.18.
 * new ParseServer
 */
public class ParseServer extends AppCompatActivity {

    public static Event event;

    // Eine (versteckte) Klassenvariable vom Typ der eigene Klasse
    private static ParseServer instance;

    // Verhindere die Erzeugung des Objektes über andere Methoden
    // Context appContext is an APP-Context is used for LocalDatastore. At most you use "this"-reference of activity.
    // Without Public Access
    private ParseServer(Context appContext) {

        // Enable Local Datastore.
        Parse.enableLocalDatastore(appContext);

        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access. No disable
        defaultACL.setPublicReadAccess(false);
        //False = only master key access
        ParseACL.setDefaultACL(defaultACL, true);

        // Only for Logcat and debug mode
        // Parse.addParseNetworkInterceptor(new ParseLogInterceptor());

        //initialize
        final String YOUR_APPLICATION_ID = "MatchFinder";
        final String YOUR_CLIENT_KEY = "matchfinderclientkey";
        final String YOUR_SERVER_URL = "https://matchfinder.dock.moxd.io/api/";
        Parse.initialize(new Parse.Configuration.Builder(appContext)
                .applicationId(YOUR_APPLICATION_ID)
                .clientKey(YOUR_CLIENT_KEY)
                .server(YOUR_SERVER_URL)   // '/' important after 'api'
                .build());

    }

    // Eine Zugriffsmethode auf Klassenebene, welches dir '''einmal''' ein konkretes
    // Objekt erzeugt und dieses zurückliefert.
    // Durch 'synchronized' wird sichergestellt dass diese Methode nur von einem Thread
    // zu einer Zeit durchlaufen wird. Der nächste Thread erhält immer eine komplett
    // initialisierte Instanz.
    public static synchronized ParseServer getInstance(Context appContext) {
        if (ParseServer.instance == null) {
            ParseServer.instance = new ParseServer(appContext);
        }
        return ParseServer.instance;
    }

    public synchronized void saveEventData(double placeLat, double placeLng, long dateAndTime, int maxPlayersNumber, String description) {

        //currently saves participants as pointer to user, if you just want the id of user add .getobejctid()
        //TODO add checkbox in createEvent activity and xml, if creator wants to participate, only add creator if checkbox enabled

        ParseObject eventObjekt = new ParseObject("Event");

        // Sete Public Read and Write Access
        ParseACL eventACL =  new ParseACL();
        eventACL.setPublicReadAccess(true);
        eventObjekt.setACL(eventACL);
        eventACL.setPublicWriteAccess(true);
        eventObjekt.setACL(eventACL);

        JSONArray participants = new JSONArray();
        participants.put(ParseUser.getCurrentUser());

        eventObjekt.put("createdby", ParseUser.getCurrentUser());
        eventObjekt.put("participants", participants);
        eventObjekt.put("placeLat", placeLat);
        eventObjekt.put("placeLng", placeLng);
        eventObjekt.put("dateAndTime", dateAndTime);

        // only till the checkbox feature implemented else wihtout (maxPleyersNumber - 1)
        maxPlayersNumber = maxPlayersNumber != -1 ? (maxPlayersNumber - 1) : -1;
        eventObjekt.put("maxPlayersNumber", (maxPlayersNumber));

        eventObjekt.put("description", description);

        eventObjekt.saveEventually();

    }

    public synchronized void addParticipantsToEvent(final Context appcontext, final String eventId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.getInBackground(eventId, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    boolean containsCurrentUser = false;
                    JSONArray participants = object.getJSONArray("participants");
                    int maxPlayersNumber = object.getInt("maxPlayersNumber");

                    // Check if CurrentUser is already a participant
                    for (int i = 0; i < participants.length(); i++) {
                        try {
                            JSONObject user = (JSONObject) participants.get(i);
                            String participantUserId = user.getString("objectId");
                            String currentUserId = ParseUser.getCurrentUser().getObjectId();
                            if (currentUserId.equals(participantUserId)) {
                                containsCurrentUser = true;
                            }
                        } catch (JSONException e1) {
                            e1.printStackTrace();
                        }
                    }

                    // Add participant only if
                    // (CurrentUser is not a participant yet AND maxPlayersNumber is greater than 0)
                    // OR
                    // (CurrentUser is not a participant yet AND maxPlayersNumber is not limited (equal -1))
                    if ((!containsCurrentUser && maxPlayersNumber > 0) || (!containsCurrentUser && maxPlayersNumber == -1)) {
                        object.addUnique("participants", ParseUser.getCurrentUser());
                        object.put("maxPlayersNumber", (maxPlayersNumber - 1));
                        object.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e == null) {
                                    Toast.makeText(appcontext, R.string.participant_added, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(appcontext, e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                    else if (containsCurrentUser) {
                        Toast.makeText(appcontext, R.string.you_are_already_a_participant, Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(appcontext, R.string.you_can_not_join_this_game, Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(appcontext, e.getMessage() + eventId, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public synchronized void loadEventData(final Context appContex, final GoogleMap googleMap) {
        //TODO only show Events in the area of the user, currently every event in the database
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> eventList, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < eventList.size(); i++) {
                        ParseObject object = eventList.get(i);
                        LatLng coordinate = new LatLng(object.getDouble("placeLat"), object.getDouble("placeLng"));
                        googleMap.addMarker(new MarkerOptions().position(coordinate).title(object.getObjectId())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_ball2)));
                    }
                    Toast.makeText(appContex, eventList.size() + " Events gefunden", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(appContex, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //TODO implement class to decode and resize images and test commented code (add byte[] data to function)
    //TODO check if profile already exist and just update in that case, currently it creates another profile
    public synchronized void saveProfileData(String name, String birthday, String residence, String description, String team, String experience, String favouriteTeam) {

        ParseObject profileObjekt = new ParseObject("Profile");
        ParseUser user = ParseUser.getCurrentUser();
/*        ParseFile profilePicture = new ParseFile(userid+"profilimage.png", data);
        try {
            profilePicture.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        profileObjekt.put("picture", profilePicture);*/
        profileObjekt.put("user", user);
        profileObjekt.put("name", name);
        profileObjekt.put("birthday", birthday);
        profileObjekt.put("residence", residence);
        profileObjekt.put("description", description);
        profileObjekt.put("team", team);
        profileObjekt.put("experience", experience);
        profileObjekt.put("favouriteTeam", favouriteTeam);

        user.put("profileInit", true);
        user.saveEventually();

        profileObjekt.saveEventually();
    }

    public synchronized void loadProfileData
            (final Context appContext, final LinearLayout profileData, final TextView realName, final TextView birthdayText,
             final TextView residenceText, final TextView descriptionText, final TextView experienceText, final TextView favouriteTeamText) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Profile");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    realName.setText(objects.get(0).getString("name"));
                    birthdayText.setText(objects.get(0).getString("birthday"));
                    residenceText.setText(objects.get(0).getString("residence"));
                    descriptionText.setText(objects.get(0).getString("description"));
                    experienceText.setText(objects.get(0).getString("experience"));
                    favouriteTeamText.setText(objects.get(0).getString("favouriteTeam"));
                    profileData.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(appContext, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public synchronized void registerUser(final Context appcontext, String email, final String username, String password, final Activity activity) {

        if (ParseUser.getCurrentUser() != null) {
            ParseUser.logOut();
        }

        ParseUser user = new ParseUser();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(appcontext, "User erstellt: " + username, Toast.LENGTH_SHORT).show();
                    activity.startActivity(new Intent(appcontext, Login.class));
                    activity.finish();
                } else {
                    Toast.makeText(appcontext, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public synchronized void loginUser(final Context appcontext, final String username, String password, final Activity activity) {

        if (ParseUser.getCurrentUser() != null) {
            ParseUser.logOut();
        }

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    Toast.makeText(appcontext, "Login erfolgreich", Toast.LENGTH_SHORT).show();
                    activity.startActivity(new Intent(appcontext, Homescreen.class));
                    activity.finish();
                } else {
                    Toast.makeText(appcontext, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public synchronized boolean logOut() {
        ParseUser curUser = ParseUser.getCurrentUser();
        if (curUser != null) {
            ParseUser.logOut();
            return true;
        } else {
            return false;
        }
    }

    public synchronized void loadUserList(final ArrayAdapter mUserAdapter) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.addAscendingOrder("username");
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> userObjects, ParseException error) {
                if (userObjects != null) {
                    mUserAdapter.clear();
                    for (int i = 0; i < userObjects.size(); i++) {
                        mUserAdapter.add(userObjects.get(i).getUsername());
                    }
                }
            }
        });
    }

    public synchronized void saveLeagueData(final Context context, String type, String city, int maxTeams, String target, String description) {

        ParseObject leagueObject = new ParseObject("League");

        ParseACL leagueACL =  new ParseACL();
        leagueACL.setPublicReadAccess(true);
        leagueObject.setACL(leagueACL);
        leagueACL.setPublicWriteAccess(true);
        leagueObject.setACL(leagueACL);

        leagueObject.put("createdby", ParseUser.getCurrentUser());
        leagueObject.put("type", type);
        leagueObject.put("inCity", city);
        leagueObject.put("maxTeams", maxTeams);
        leagueObject.put("target", target);
        leagueObject.put("description", description);

        leagueObject.saveEventually(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(context, "Liga wurde gespeichert", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
