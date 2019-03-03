package com.example.nearme;

import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nearme.pojos.UserAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class Registration extends AppCompatActivity {

    private Toolbar toolbar;
    private Spinner spnr_country, spnr_residence;
    private AutoCompleteTextView actv_jobTitle;
    private EditText et_firstname, et_lastname, et_email, et_password, et_phone;
    private ProgressBar progressBar;
    private Button btn_createAccount;
    private FirebaseAuth mAuth;

    private String[] countries = {"Select Country", "Canada", "US", "Burkina Faso", "France", "Germany","Bangladesh"};
    private String[] residence = {"Select Country of Residence", "Canada", "US", "Burkina Faso", "France","Germany", "Bangladesh"};
    private String[] jobs = {"Software Eng", "Senior Software Eng", "Web Developer", "Android Developer"};


    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        initializeFields();

        mAuth = FirebaseAuth.getInstance();

        toolbar = findViewById(R.id.reg_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        //country adapter
        ArrayAdapter<String> countryAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, countries) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }

        };
        spnr_country.setAdapter(countryAdapter);

        ///residence adapter
        ArrayAdapter<String> residenceAdapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item, residence) {
            @Override
            public boolean isEnabled(int position) {
                if (position == 0) {
                    // Disable the first item from Spinner
                    // First item will be use for hint
                    return false;
                } else {
                    return true;
                }
            }

            @Override
            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                if (position == 0) {
                    // Set the hint text color gray
                    tv.setTextColor(Color.GRAY);
                } else {
                    tv.setTextColor(Color.BLACK);
                }
                return view;
            }
        };
        spnr_residence.setAdapter(residenceAdapter);

        //Autocompletion of job field
        ArrayAdapter<String> jobsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, jobs);
        actv_jobTitle.setAdapter(jobsAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        btn_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registerUser();
            }
        });
    }

    private void registerUser() {
        final String firstname = et_firstname.getText().toString().trim();
        final String lastname = et_lastname.getText().toString().trim();
        final String email = et_email.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        final String phone = et_phone.getText().toString().trim();
        final String country = (String) spnr_country.getSelectedItem();
        final String residence = (String) spnr_residence.getSelectedItem();
        final String jobTitle = actv_jobTitle.getText().toString().trim();

        if (firstname.isEmpty()) {
            et_firstname.setError(getString(R.string.emptyField));
            et_firstname.requestFocus();
            return;
        }
        if (lastname.isEmpty()) {
            et_lastname.setError(getString(R.string.emptyField));
            et_lastname.requestFocus();
            return;
        }
        if (email.isEmpty()) {
            et_email.setError(getString(R.string.emptyField));
            et_email.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            et_email.setError(getString(R.string.invalidEmail));
            et_email.requestFocus();
            return;
        }
        if (password.isEmpty()) {
            et_password.setError(getString(R.string.emptyField));
            et_password.requestFocus();
            return;
        }
        if (password.length() < 6) {
            et_password.setError(getString(R.string.invalidLength));
            et_password.requestFocus();
            return;
        }
        if (phone.isEmpty()) {
            et_phone.setError(getString(R.string.emptyField));
            et_phone.requestFocus();
            return;
        }
        if (phone.length() < 10) {
            et_phone.setError(getString(R.string.phoneLength));
            et_phone.requestFocus();
            return;
        }
        if (spnr_country.getSelectedItemId() == 0) {
            Toast.makeText(this, getString(R.string.emptyCountry), Toast.LENGTH_SHORT).show();
            return;
        }
        if (spnr_residence.getSelectedItemId() == 0) {
            Toast.makeText(this, getString(R.string.emptyResidence), Toast.LENGTH_SHORT).show();
            return;
        }

        if (jobTitle.isEmpty()) {
            actv_jobTitle.setError(getString(R.string.emptyField));
            actv_jobTitle.requestFocus();
            return;
        }


        progressBar.setVisibility(View.VISIBLE);

        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //we will complete here with additional field
                            UserAccount user = new UserAccount(firstname, lastname, email, phone, country, residence, jobTitle);
                            FirebaseDatabase.getInstance().getReference("users")
                                    .child(mAuth.getCurrentUser().getUid())
                                    .setValue(user)
                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            progressBar.setVisibility(View.GONE);
                                            if (task.isSuccessful()) {
                                                Toast.makeText(Registration.this, getString(R.string.regSuccess), Toast.LENGTH_LONG).show();
                                                finish();
                                                startActivity(new Intent(Registration.this,Dashboard.class));
                                            } else {
                                                Toast.makeText(Registration.this, "Something went wrong", Toast.LENGTH_LONG).show();
                                                progressBar.setVisibility(View.GONE);
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(Registration.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }

    private void initializeFields() {
        spnr_country = findViewById(R.id.sp_reg_country);
        spnr_residence = findViewById(R.id.sp_reg_residence);
        actv_jobTitle = findViewById(R.id.actv_jobtitle);
        et_firstname = findViewById(R.id.et_firstname);
        et_lastname = findViewById(R.id.et_lastname);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        btn_createAccount = findViewById(R.id.btn_createAccount);
        progressBar = findViewById(R.id.progress_bar);
        et_phone = findViewById(R.id.et_phone);
    }


}
