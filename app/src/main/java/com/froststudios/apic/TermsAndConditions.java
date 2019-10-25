package com.froststudios.apic;

import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

public class TermsAndConditions extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions);

        setTitle("Terms and Conditions");

        // Actionbar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_arrow_back);

        try {
            WebView webView = findViewById(R.id.termsView);
            webView.loadUrl("file:///android_asset/Apic-Terms-and-Conditions.htm");
        } catch (Exception e) {
            Toast.makeText(this, "Terms and Conditions could not be loaded.", Toast.LENGTH_SHORT).show();
            this.finish();
        }
    }

    // Handle back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
