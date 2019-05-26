package com.froststudios.apic;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReviewImages extends AppCompatActivity {

    private TextView totalSum;

    private ItemsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review_images);

        // Actionbar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_arrow_back);

        setTitle("Cart");

        totalSum = findViewById(R.id.totalSum);

        Button addImage = findViewById(R.id.addImage);
        addImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                addImage();
            }
        });

        populateList();
        updateTotalPrice();
    }

    private void populateList() {
        // Construct the data source
        ArrayList<Item> arrayOfItems = new ArrayList<>();

        // Create the adapter to convert the array to views
        adapter = new ItemsAdapter(this, arrayOfItems);

        // Attach the adapter to a ListView
        ListView listView = findViewById(R.id.items_container);
        listView.setAdapter(adapter);

        // Add items to adapter
        for (Item i : Variables.itemsArray) {
            adapter.add(i);
        }
    }

    public void updateItemsList() {
        adapter.clear();
        for (Item i : Variables.itemsArray) {
            adapter.add(i);
        }
        adapter.notifyDataSetChanged();
    }

    public void updateTotalPrice() {
        float price = totalImageCount() * Variables.price;
        String text = "Total: " + price + "€";
        totalSum.setText(text);
    }

    private int totalImageCount() {
        int count = 0;
        for (Item i : Variables.itemsArray) {
            count += i.amount;
        }
        return count;
    }

    private void addImage() {
        Intent intent = new Intent(this, ImagePicker.class);
        startActivity(intent);
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

    private void nextStep() {
        if (Variables.itemsArray.size() < 1) {
            Toast.makeText(this, "No images selected.", Toast.LENGTH_SHORT).show();
        } else {
            Intent intent = new Intent(this, UserInfo.class);
            startActivity(intent);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    private void previousStep() {
        // Go back to main menu
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        previousStep();
    }
}
