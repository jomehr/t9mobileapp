package com.kick_it.jan.t9_mobileapp.db;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

/*
 * Created by taraszaika on 02.01.18.
 * new ParseServer
 */
public class ParseServer {

    // Eine (versteckte) Klassenvariable vom Typ der eigene Klasse
    private static ParseServer instance;

    // Verhindere die Erzeugung des Objektes über andere Methoden
    // Context appContext is an APP-Context is used for LocalDatastore. At most you use "this"-reference of activity.
    private ParseServer (Context appContext) {

        // Enable Local Datastore.
        Parse.enableLocalDatastore(appContext);

        ParseUser.enableAutomaticUser();
        ParseACL defaultACL = new ParseACL();
        // Optionally enable public read access.
        // defaultACL.setPublicReadAccess(true);
        //False = only master key access
        ParseACL.setDefaultACL(defaultACL, true);

        // Only for logcat and debug mode
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

    public synchronized void loadEventData(final Context appContext)  {

        //save context to handle it into the inner class for making Toast on UI
        final Context con = appContext;

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Event");

        ///Example
        query.getInBackground("UDcxbKDuWD", new GetCallback<ParseObject>() {
            public void done(ParseObject object, ParseException e) {
                if (e == null) {
                    // object will be your event
                    Toast.makeText(con,"Ort: " + object.getLong("placeLat")+ " , " + object.getLong("placeLng")
                            ,Toast.LENGTH_SHORT).show();
                } else {
                    // something went wrong
                    Log.d("Getting", "Something wrong getting File " + e);
                }
            }
        });
        ///

    }
}
