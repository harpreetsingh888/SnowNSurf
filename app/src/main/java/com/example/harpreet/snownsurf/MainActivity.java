package com.example.harpreet.snownsurf;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Class Variables
    String[] mountainItems;
    ArrayList<String> mountainListItems;
    ArrayAdapter<String> adapter;
    ListView mountainListView;
    EditText mountainEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_snow) {
            RelativeLayout snowView = (RelativeLayout) findViewById(R.id.snowContent);
            RelativeLayout surfView = (RelativeLayout) findViewById(R.id.surfContent);
            mountainListView = (ListView) findViewById(R.id.mountainList);
            mountainEditText = (EditText) findViewById(R.id.searchText);

            if (surfView != null && surfView.getVisibility() == View.VISIBLE) {
                surfView.setVisibility(View.INVISIBLE);
            }

            if (snowView != null) {
                if (snowView.getVisibility() != View.VISIBLE) {
                    snowView.setVisibility(View.VISIBLE);
                    initiateMountainList();
                    registerClickCallback();
                    mountainEditText.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            if (s.toString().equals("")) {
                                //reset mountain list view
                                initiateMountainList();
                            } else {
                                //perform search
                                searchMountainListItem(s.toString());
                            }
                        }

                        @Override
                        public void afterTextChanged(Editable s) {

                        }
                    });
                }
            }
        } else if (id == R.id.nav_surf) {
            TextView surfText = new TextView(this);
            surfText.setTextSize(40);
            surfText.setText("SURFING");

            RelativeLayout surfView = (RelativeLayout) findViewById(R.id.surfContent);
            RelativeLayout snowView = (RelativeLayout) findViewById(R.id.snowContent);

            if (snowView != null && snowView.getVisibility() == View.VISIBLE) {
                snowView.setVisibility(View.INVISIBLE);
            }

            if (surfView != null) {
                if (surfView.getVisibility() != View.VISIBLE) {
                    surfView.setVisibility(View.VISIBLE);
                }

//                surfView.addView(surfText);
            }

        }
//        else if (id == R.id.nav_slideshow) {
//
//        } else if (id == R.id.nav_manage) {
//
//        } else if (id == R.id.nav_share) {
//
//        } else if (id == R.id.nav_send) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void initiateMountainList() {
        mountainItems = new String[] {"Coronet Peak", "Remarkables", "Cardrona", "Treble Cone"};
        mountainListItems = new ArrayList<>(Arrays.asList(mountainItems));
        adapter = new ArrayAdapter<String>(this, R.layout.mountain_list_item, R.id.mountainItem, mountainListItems);
        mountainListView.setAdapter(adapter);
        mountainListView.setVisibility(View.INVISIBLE);
//        mountainListView.setOnItemClickListener(new MountainItemList());
    }

    public void searchMountainListItem(String searchText) {
        for (String item:mountainItems) {
            if (!item.toLowerCase().contains(searchText.toLowerCase())) {
                mountainListItems.remove(item);
            }
        }

        if (mountainListView.getVisibility() != View.VISIBLE) {
            mountainListView.setVisibility(View.VISIBLE);
        }

        adapter.notifyDataSetChanged();
    }


    private void registerClickCallback() {
//        ListView listView = (ListView) findViewById(R.id.mountainList);

        mountainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                double latitude = 0.0;
                double longitude = 0.0;
                ViewGroup vg = (ViewGroup) view;
                Integer zeroPosition = 0;
                TextView currentTextView = (TextView) vg.getChildAt(zeroPosition);
                String location = currentTextView.getText().toString();

                Geocoder geocoder = new Geocoder(MainActivity.this);
                List<Address> addresses = null;
                try {
                    addresses = geocoder.getFromLocationName(location, 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if(addresses != null && addresses.size() > 0) {
                    latitude = addresses.get(0).getLatitude();
                    longitude = addresses.get(0).getLongitude();
                }

                String apiUrl = "https://api.forecast.io/forecast/e2fa18960935f3fd3c358f65922f4c6c/"+latitude+","+longitude;

                JsonHandling jsonhandling =  new JsonHandling();
                String result = jsonhandling.doInBackground(apiUrl);

                displayData(result);

            }
        });
    }

    private void displayData(String result) {
        String test;
    }
}

