package com.froststudios.apic;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class CopySelector extends AppCompatActivity {

    private TextView copyNumber;
    private int amount = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_copy_selector);

        setTitle("Select Amount");

        copyNumber = findViewById(R.id.copyNumber);

        // Actionbar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_arrow_back);

        Button addImage = findViewById(R.id.addImage);
        addImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                amount += 1;
                copyNumber.setText(String.valueOf(amount));
                updateTotalPrice();
            }
        });

        Button subtractImage = findViewById(R.id.subtractImage);
        subtractImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                if (amount > 1) {
                    amount -= 1;
                    copyNumber.setText(String.valueOf(amount));
                    updateTotalPrice();
                }
            }
        });

        ImageView imageView = findViewById(R.id.imageView);
        imageView.setImageBitmap(Variables.finalImageBitmap);

        updateTotalPrice();
    }

    private void updateTotalPrice() {
        float price = amount * Variables.price;
        String text = amount + " / " + price + "€";
        copyNumber.setText(text);
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

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    private void nextStep() {
        Variables.itemsArray.add(new Item(Variables.finalImageBitmap, amount));
        Intent intent = new Intent(this, ReviewImages.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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
