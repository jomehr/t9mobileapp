package com.matchfinder.jan.t9_mobileapp.menu;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.Toast;

import com.matchfinder.jan.t9_mobileapp.R;
import com.matchfinder.jan.t9_mobileapp.activities.Homescreen;
import com.matchfinder.jan.t9_mobileapp.activities.Login;
import com.matchfinder.jan.t9_mobileapp.activities.Profile;
import com.matchfinder.jan.t9_mobileapp.activities.Search;
import com.matchfinder.jan.t9_mobileapp.db.ParseServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/*
 * Created by Christopher on 31.12.2017.
 */

public class menu_faq extends AppCompatActivity {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_faq);

        Toolbar myToolbar = findViewById(R.id.toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        try {
            getSupportActionBar().setTitle(R.string.faq);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        myToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), Homescreen.class));
            }
        });

        listView = findViewById(R.id.lvExp);
        initData();
        listAdapter = new com.matchfinder.jan.t9_mobileapp.util.ExpandableListAdapter(this, listDataHeader, listHash);
        listView.setAdapter(listAdapter);
    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("Abstract");
        listDataHeader.add("Projektidee");
        listDataHeader.add("Markt-/Konkurrenzanalyse");
        //listDataHeader.add("UWP");

        List <String> edmtDev = new ArrayList<>();
        edmtDev.add("Für Freizeitfußballer ist das Kicken auf dem Bolzplatz eine willkommene Möglichkeit, um mit den Kumpels in zwangloser Atmosphäre die Kugel laufen zu lassen. Die Faszination Straßenfußball ist schnell erklärt: Eine Ball, zwei Tore und eine bunt zusammen gemischte Gruppe aus Fußballbegeisterten - schon kann das Spiel beginnen!");

        List<String> androidStudio = new ArrayList<>();
        androidStudio.add("App soll es ermöglichen, dass sich völlig Fremde zum Fußball spielen/bolzen verabreden können\n" +
                "Dazu können Nutzerprofile angelegt werden; Einzelprofile sowie Profile von kompletten “Bolzplatzteams”\n" +
                "Nutzerprofile können individualisiert werden: z.B. mit Benutzername, Foto, Bolzplatzteam, Stammbolzplatz, fußballerische Fähigkeiten (in Form einer Skala) \n" +
                "User der App müssen ihren Standort freigeben! So kann über einen Radar eingesehen werden, welche Nutzer oder Teams sich in unmittelbarer Nähe befinden\n" +
                "Außerdem könnte es eine Filterfunktion geben, um nach anderen Teams/Spielern zu suchen, z.B. Maximale Anzahl an Spielern im Gegnerteam, Entfernung der beiden Teams zueinander, Entfernung zum nächsten Bolzplatz/Soccerhalle\n" +
                "Jeder hat die Möglichkeit einen anderen Spieler/Verein anzuschreiben \n" +
                "Den Bolzplatz als Grundpfeiler des Amateurfußballs vermarkten \n" +
                "Die Vorzüge des traditionellen Bolzens herausstreichen ");

        List<String> xamarin = new ArrayList<>();
        xamarin.add("Die App soll sich an alle Menschen im fußballfähigen Alter richten, die ein Smartphone besitzen\n" +
                "Als Konkurrenz können natürlich Amateurvereine gesehen werden\n" +
                "Ein richtiges Konkurrenzprodukt zu unserer Idee gibt es aber derzeit nicht (zumindest im Raum Köln)\n" +
                "Eine nicht auf dem deutschen Markt vorhandene Konkurrenz-App ist: \"Find a Player\"");

        //List<String> uwp = new ArrayList<>();
        //uwp.add("uwp Expandable ListView");
        //uwp.add("uwp Google maps");
        //uwp.add("uwp Firebase");
        //uwp.add("uwp Chat Application");

        listHash.put(listDataHeader.get(0), edmtDev);
        listHash.put(listDataHeader.get(1), androidStudio);
        listHash.put(listDataHeader.get(2), xamarin);
        //listHash.put(listDataHeader.get(3), uwp);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_search:
                startActivity(new Intent(this, Search.class));
                return true;
            case R.id.action_profile:
                startActivity(new Intent(this, Profile.class));
                return true;
            case R.id.action_settings:
                startActivity(new Intent(this, menu_settings.class));
                return true;
            case R.id.action_developer:
                startActivity(new Intent(this, menu_developer.class));
                return true;
            case R.id.action_faq:
                startActivity(new Intent(this, menu_faq.class));
                return true;
            case R.id.action_sign_out:
                ParseServer ps =ParseServer.getInstance(this);
                if (ps.logOut()) {
                    startActivity(new Intent(this, Login.class));
                    finish();
                }else {
                    Toast.makeText(this, "Fehler beim Logout",Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.action_data_privacy:
                startActivity(new Intent(this, menu_data_privacy.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}