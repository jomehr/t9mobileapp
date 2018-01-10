package com.matchfinder.jan.t9_mobileapp.db;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.matchfinder.jan.t9_mobileapp.activities.Homescreen;
import com.matchfinder.jan.t9_mobileapp.activities.Login;
import com.matchfinder.jan.t9_mobileapp.db.entities.Event;
import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.List;

/*
 * Created by taraszaika on 02.01.18.
 * new ParseServer
 */
public class ParseServer extends AppCompatActivity{

    public static Event event;
    private boolean b;

    // Eine (versteckte) Klassenvariable vom Typ der eigene Klasse
    private static ParseServer instance;

    // Verhindere die Erzeugung des Objektes über andere Methoden
    // Context appContext is an APP-Context is used for LocalDatastore. At most you use "this"-reference of activity.
    private ParseServer (Context appContext) {

        // Enable Local Datastore.
        Parse.enableLocalDatastore(appContext);

        //ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        defaultACL.setPublicReadAccess(true);
        //False = only master key access
        ParseACL.setDefaultACL(defaultACL, true);

        // Only for Logcat and debug mode
        // Parse.addParseNetworkInterceptor(new ParseLogInterceptor());

        //initialize
        final String YOUR_APPLICATION_ID = "MatchFinder";
        final String YOUR_CLIENT_KEY = "matchfinderclientkey";
        final String YOUR_SERVER_URL = "https://matchfinder.dock.moxd.io/api/";
        Parse.initialize(new Parse.Configuration.Builder(appContext.getApplicationContext())
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
    public static synchronized ParseServer getInstance (Context appContext) {
        if (ParseServer.instance == null) {
            ParseServer.instance = new ParseServer (appContext);
        }
        return ParseServer.instance;
    }

    public synchronized void saveEventData (double placeLat, double placeLng, long dateAndTime, int maxPlayersNumber, String description) {

        ParseObject eventObjekt = new ParseObject("Event");

        eventObjekt.put("placeLat", placeLat);
        eventObjekt.put("placeLng", placeLng);
        eventObjekt.put("dateAndTime", dateAndTime);
        eventObjekt.put("maxPleyersNumber", maxPlayersNumber);
        eventObjekt.put("description", description);

        eventObjekt.saveInBackground();

    }

    public synchronized void loadEventData(Context appContex)  {

        //save context to handle it into the inner class for making Toast on UI
        final Context con = appContex;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");

        ///Example
        try {
            ParseObject object = query.get("SkNnLghmo3");
            event = new Event(object.getObjectId(),
                    object.getDouble("placeLat"),
                    object.getDouble("placeLng"),
                    object.getLong("dateAndTime"),
                    object.getInt("maxPlayersNumber"),
                    object.getString("description"));
            Toast.makeText(con,"Ort: " + object.getDouble("placeLat")+ " , " + object.getDouble("placeLng")
                    ,Toast.LENGTH_SHORT).show();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        /*
        query.getInBackground("mmcotyyC79", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your event
                      event = new Event(object.getObjectId(),
                            object.getDouble("placeLat"),
                            object.getDouble("placeLng"),
                            object.getLong("dateAndTime"),
                            object.getInt("maxPlayersNumber"),
                            object.getString("description"));
                    Toast.makeText(con,"Ort: " + object.getDouble("placeLat")+ " , " + object.getDouble("placeLng")
                            ,Toast.LENGTH_SHORT).show();
                } else {
                    // something went wrong
                    Log.d("Getting", "Something wrong getting File " + e);
                }
            }
        });
        */
        ///
    }
    //TODO implement class to decode and resize images and test commented code (add byte[] data to function)
    public synchronized void saveProfileData (String name, String birthday, String residence, String descriptopn, String team, String favouriteArea, String experience, String favouriteTeam) {

        String userid = ParseUser.getCurrentUser().getObjectId();
        ParseObject profileObjekt = new ParseObject("Profile");
/*        ParseFile profilePicture = new ParseFile(userid+"profilimage.png", data);
        try {
            profilePicture.save();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        profileObjekt.put("picture", profilePicture);*/
        profileObjekt.put("user", ParseObject.createWithoutData(ParseUser.class, userid));
        profileObjekt.put("name", name);
        profileObjekt.put("birtday", birthday);
        profileObjekt.put("residence", residence);
        profileObjekt.put("description", descriptopn);
        profileObjekt.put("team", team);
        profileObjekt.put("favouriteArea", favouriteArea);
        profileObjekt.put("experience", experience);
        profileObjekt.put("favouriteTeam", favouriteTeam);

        profileObjekt.saveEventually();
    }

    public synchronized void registerUser (final Context appcontext, String email, final String username, String password, final Activity activity) {

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
                    Toast.makeText(appcontext, "User erstellt: "+ username, Toast.LENGTH_SHORT).show();
                    activity.startActivity(new Intent(appcontext, Login.class));
                    activity.finish();
                } else {
                    Toast.makeText(appcontext, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public synchronized void loginUser (final Context appcontext, final String username, String password, final Activity activity) {

        if (ParseUser.getCurrentUser() != null) {
            ParseUser.logOut();
        }

        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser parseUser, ParseException e) {
                if (parseUser != null) {
                    Toast.makeText(appcontext, "Login erfolgreich", Toast.LENGTH_SHORT).show();
                    activity.startActivity(new Intent( appcontext, Homescreen.class));
                    activity.finish();
                } else {
                    Toast.makeText(appcontext, e.getMessage(),Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public synchronized boolean logOut() {
        ParseUser curUser = ParseUser.getCurrentUser();
        if (curUser != null) {
            ParseUser.logOut();
            return true;
        }else {
            return false;
        }
    }

    public void loadUserList(final ArrayAdapter mUserAdapter) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
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

}
