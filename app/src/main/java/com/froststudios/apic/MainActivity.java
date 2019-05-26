package com.froststudios.apic;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Äpic - Memories Matter!");

        Button imageSelector = findViewById(R.id.selectImage);
        imageSelector.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                getWritePermission();
            }
        });
    }

    private void getWritePermission() {
        if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        } else {
            nextStep();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                nextStep();
            }
        }
    }

    private void nextStep() {
        Intent intent = new Intent(this, ImagePicker.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    // Create options menu
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_popup, menu);
        return true;
    }

    // Options menu onclick
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.location:
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.fuji.ee/kontakt/"));
                startActivity(intent);
                break;
            case R.id.terms:
                Intent terms = new Intent(this, TermsAndConditions.class);
                startActivity(terms);
                break;
            case R.id.license:
                Intent license = new Intent(this, License.class);
                startActivity(license);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
