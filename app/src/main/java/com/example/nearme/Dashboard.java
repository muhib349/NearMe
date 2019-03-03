package com.example.nearme;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.nearme.adapter.UserAdapter;
import com.example.nearme.pojos.UserAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Dashboard extends AppCompatActivity {

    private List<UserAccount> userAccountList = new ArrayList<>();
    private ArrayList<UserAccount> selectedCountryUsers = new ArrayList<UserAccount>();
    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private String[] con = {"Africa", "North America", "Europe","Asia"};
    private List<String> contenent= Arrays.asList(con);
    private HashMap<String, ArrayList<String>> map = new HashMap<String, ArrayList<String>>();
    private Spinner sp_contenent;
    private Spinner sp_country;
    private Toolbar toolbar;
    private FirebaseAuth mAuth;

    private ArrayAdapter<String> contenentAdapter;
    private ArrayAdapter<String> countryAdapter;

    @Override
    protected void onStart() {
        super.onStart();

        if(mAuth == null){
            finish();
            startActivity(new Intent(Dashboard.this,Login.class));
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        toolbar = findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);

        initializeMap();

        mAuth = FirebaseAuth.getInstance();

        sp_country = findViewById(R.id.sp_country);
        sp_contenent = findViewById(R.id.sp_continent);
        recyclerView = findViewById(R.id.recyler_persons);


        userAdapter = new UserAdapter(selectedCountryUsers);
        final RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(userAdapter);

        contenentAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, contenent);
        sp_contenent.setAdapter(contenentAdapter);

        countryAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
                new String[]{"Burkina Faso", "Zimbabwe", "Kenya"});
        sp_country.setAdapter(countryAdapter);


        DatabaseReference allUserReference = FirebaseDatabase.getInstance().getReference("users");
        allUserReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    UserAccount user = userSnapshot.getValue(UserAccount.class);
                    userAccountList.add(user);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Dashboard.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        DatabaseReference sReference = FirebaseDatabase.getInstance().getReference("users/"+mAuth.getCurrentUser().getUid());

        sReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserAccount myProfile = dataSnapshot.getValue(UserAccount.class);


                /*int i=-1,j=-1;
                for(String key:map.keySet()){
                    ++i;
                    ArrayList<String> country = map.get(key);
                    if(country.contains(myProfile.getCurrentCountry())){
                        j = country.indexOf(myProfile.getCurrentCountry());
                        countryAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, country);
                        sp_country.setAdapter(countryAdapter);
                        sp_contenent.setSelection(contenent.indexOf(key));
                        sp_country.setSelection(j);
                        break;
                    }
                }*/
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();


        sp_contenent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ArrayList<String> countryList = new ArrayList<String>();
                countryList = map.get(contenent.get(position));
                countryAdapter = new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, countryList);
                sp_country.setAdapter(countryAdapter);
                changeRecyclerView((String) sp_country.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                changeRecyclerView((String) sp_country.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void initializeMap() {
        ArrayList<String> africanCountry = new ArrayList<String>();
        africanCountry.add("Burkina Faso");
        africanCountry.add("Zimbabwe");
        africanCountry.add("Kenya");
        map.put("Africa", africanCountry);

        ArrayList<String> northAmerican = new ArrayList<String>();
        northAmerican.add("Canada");
        northAmerican.add("US");
        northAmerican.add("Mexico");
        map.put("North America", northAmerican);

        ArrayList<String> europe = new ArrayList<String>();
        europe.add("France");
        europe.add("Germany");
        europe.add("Italy");
        map.put("Europe", europe);

        ArrayList<String> asia = new ArrayList<String>();
        asia.add("Bangladesh");
        asia.add("Pakistan");
        asia.add("India");
        map.put("Asia", asia);
    }

    private void prepareUsers() {
        UserAccount user;
        user = new UserAccount("Gary", "A. Bennett", "garyBennett@rhyta.com", "647-886-4484", "Canada", "Canada", "Manager");
        userAccountList.add(user);
        user = new UserAccount("Cheryl", "R. Delvalle", "cherylelvalle@teleworm.us", "705-565-1128", "Canada", "Bangladesh", "CEO");
        userAccountList.add(user);

        user = new UserAccount("Joao", "Gomes Araujo", "joaoGomesAraujo@rhyta.com", "42328-4734", "Burkina Faso", "Canada", "Software Eng");
        userAccountList.add(user);

        user = new UserAccount("Dennis", "S. Murphy", "dennisSMurphy@teleworm.us", "42328-4734", "Burkina Faso", "Belgium", "Web Developer");
        userAccountList.add(user);


        user = new UserAccount("Victor", "G. Niemi", "victorGNiemi@armyspy.com", "886-4484", "Zimbabwe", "Belgium", "Android Developer");
        userAccountList.add(user);


        user = new UserAccount("John", "Osgood", "johnKOsgood@dayrep.com", "9861674699", "Germany", "Burkina Faso", "Web Designer");
        userAccountList.add(user);

        user = new UserAccount("Dennis", "S. Murphy", "dennisSMurphy@teleworm.us", "42328-4734", "France", "Belgium", "Web Designer");
        userAccountList.add(user);

        user = new UserAccount("Humam", "Yusef Sleiman", "humamYusefSleiman@rhyta.com", "250-276-6006", "US", "Arab", "Back-End Developer");
        userAccountList.add(user);

        user = new UserAccount("Dominic", "Hocking", "dominicHocking@dayrep.com", "306-647-0023", "US", "Belgium", "Android Developer");
        userAccountList.add(user);
    }

    private void changeRecyclerView(String country) {
        selectedCountryUsers.clear();
        for (UserAccount user : userAccountList) {
            if (user.getCurrentCountry().equals(country)) {
                selectedCountryUsers.add(user);
            }
        }
        userAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAuth.signOut();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.dasboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_profile:
                //addSomething();
                return true;
            case R.id.action_signout:
                confirmDialog();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder
                .setMessage("Are You Sure Want to Sign Out?")
                .setPositiveButton("Yes",  new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        mAuth.signOut();
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                })
                .show();
    }
}
