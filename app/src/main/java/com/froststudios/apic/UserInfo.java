package com.froststudios.apic;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.Toast;

public class UserInfo extends AppCompatActivity {

    private EditText firstName;
    private EditText lastName;
    private EditText phoneNumber;
    private EditText mail;
    private RadioButton address1;
    private RadioButton address2;
    private EditText userAddress;
    private CheckBox checkBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        // Actionbar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_arrow_back);

        setTitle("Customer Information");

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);
        phoneNumber = findViewById(R.id.phone);
        mail = findViewById(R.id.mail);
        address1 = findViewById(R.id.address1);
        address2 = findViewById(R.id.address2);
        userAddress = findViewById(R.id.userAddress);
        checkBox = findViewById(R.id.checkBox);

        ImageButton location = findViewById(R.id.location);
        location.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showLocation();
            }
        });

        Button terms = findViewById(R.id.terms);
        terms.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                openTermsAndConditions();
            }
        });

        address1.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                address2.setChecked(false);
            }
        });

        address2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                address1.setChecked(false);
            }
        });
    }

    private void showLocation() {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.fuji.ee/kontakt/"));
        startActivity(intent);
    }

    private void openTermsAndConditions() {
        Intent terms = new Intent(this, TermsAndConditions.class);
        startActivity(terms);
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.accept:
                nextStep();
                break;

            case android.R.id.home:
                previousStep();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void previousStep() {
        finish();
    }

    private void nextStep() {
        if (checkInfo()) {
            if (isNetworkAvailable()) {
                saveInfo();
                Intent intent = new Intent(this, Upload.class);
                startActivity(intent);
            } else {
                Toast.makeText(this, "No Internet connection.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private boolean checkInfo() {
        // First name is empty or does not contain only letters and space
        if (firstName.getText().toString().equals("") || !firstName.getText().toString().matches("^[a-zA-Z]+( [a-zA-Z]+)*$")) {
            Toast.makeText(this, "Invalid first name.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Last name is empty or does not contain only letters and space
        if (lastName.getText().toString().equals("") || !lastName.getText().toString().matches("^[a-zA-Z]+( [a-zA-Z]+)*$")) {
            Toast.makeText(this, "Invalid last name.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if phone not matching regex
        if (phoneNumber.getText().toString().equals("") || !phoneNumber.getText().toString().matches("^(\\+([0-9]{1,2}-)?[0-9]{1,4})?[0-9]{3,14}$")) {
            Toast.makeText(this, "Invalid phone number.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // If mail not matching regex
        if (!mail.getText().toString().matches("^[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)*\\.[a-zA-Z0-9]+$")) {
            Toast.makeText(this, "Invalid e-mail address.", Toast.LENGTH_SHORT).show();
            return false;
        }

        // Check if address empty
        if (address2.isChecked()) {
            if (userAddress.getText().toString().equals("") || !userAddress.getText().toString().matches("^[a-zA-Z0-9]+(( [a-zA-Z0-9]+)?(\\.[ ]?[a-zA-Z0-9]+)?(,[ ]?[a-zA-Z0-9]+)?(-[a-zA-Z0-9]+)?)*$")) {
                Toast.makeText(this, "Invalid address.", Toast.LENGTH_SHORT).show();
                return false;
            }
        }

        // Checkbox is not checked
        if (!checkBox.isChecked()) {
            Toast.makeText(this, "Please Accept Terms and Conditions.", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void saveInfo() {
        Variables.firstName = firstName.getText().toString();
        Variables.lastName = lastName.getText().toString();
        Variables.phone = phoneNumber.getText().toString();
        Variables.mail = mail.getText().toString();
        if (address1.isChecked()) {
            Variables.address = "Stuudio";
        } else {
            Variables.address = userAddress.getText().toString();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }
}
