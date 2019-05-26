package com.froststudios.apic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;

import com.zomato.photofilters.imageprocessors.Filter;
import com.zomato.photofilters.imageprocessors.subfilters.BrightnessSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ColorOverlaySubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.ContrastSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.SaturationSubfilter;
import com.zomato.photofilters.imageprocessors.subfilters.VignetteSubfilter;

public class Filters extends AppCompatActivity {

    static {
        System.loadLibrary("NativeImageProcessor");
    }

    private ImageView preview;

    // Variable for storing the current bitmap edits
    // Reset to original when user removes all edits
    private Bitmap input;

    // Variable for carrying over edited bitmap when switching fragments
    // Reset to original when user removes all edits
    private Bitmap output;

    // Variable for storing the value of currently active fragment
    // to prevent re-loading a fragment when it is already open
    int currentFragmentID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filters);

        input = Variables.finalImageBitmap;
        output = Variables.finalImageBitmap;

        // Set initial fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ColorFilterFragment()).commit();

        // Create options menu for selecting filter type
        ImageButton filter = findViewById(R.id.filter);
        filter.setOnClickListener(popupMenuListener());

        setTitle("Apply Filter");

        // Actionbar
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu_arrow_back);

        preview = findViewById(R.id.imageView);
        preview.setImageBitmap(Variables.finalImageBitmap);
    }

    private View.OnClickListener popupMenuListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view){
                PopupMenu popup = new PopupMenu(getApplicationContext(), view);
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    public boolean onMenuItemClick(MenuItem item) {
                        int id = item.getItemId();
                        switch (id) {

                            case R.id.color:
                                if (currentFragmentID == 0) return true;
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ColorFilterFragment()).commit();
                                input = output;
                                currentFragmentID = 0;
                                break;

                            case R.id.saturation:
                                if (currentFragmentID == 1) return true;
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new SaturationFilterFragment()).commit();
                                input = output;
                                currentFragmentID = 1;
                                break;

                            case R.id.contrast:
                                if (currentFragmentID == 2) return true;
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ContrastFilterFragment()).commit();
                                input = output;
                                currentFragmentID = 2;
                                break;

                            case R.id.brightness:
                                if (currentFragmentID == 3) return true;
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new BrightnessFilterFragment()).commit();
                                input = output;
                                currentFragmentID = 3;
                                break;

                            case R.id.vignette:
                                if (currentFragmentID == 4) return true;
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new VignetteFilterFragment()).commit();
                                input = output;
                                currentFragmentID = 4;
                                break;

                            case R.id.reset:
                                getSupportFragmentManager().beginTransaction().replace(R.id.fragmentContainer, new ColorFilterFragment()).commit();
                                preview.setImageBitmap(Variables.finalImageBitmap);
                                input = Variables.finalImageBitmap;
                                output = Variables.finalImageBitmap;
                                currentFragmentID = 0;
                                return true;
                        }
                        return true;
                    }
                });
                popup.inflate(R.menu.menu_popup_filters);
                popup.show();
            }
        };
    }

    public void applyColorFilter(float red, float green, float blue) {
        Bitmap bitmap = input.copy(Bitmap.Config.ARGB_8888, true);

        Filter filter = new Filter();
        filter.addSubFilter(new ColorOverlaySubfilter(100, red, green, blue));
        output = filter.processFilter(bitmap);

        preview.setImageBitmap(output);
    }

    public void applyContrastFilter(float contrast) {
        Bitmap input = this.input.copy(Bitmap.Config.ARGB_8888, true);

        Filter filter = new Filter();
        filter.addSubFilter(new ContrastSubfilter(contrast));
        output = filter.processFilter(input);

        preview.setImageBitmap(output);
    }

    public void applySaturationFilter(float saturation) {
        Bitmap input = this.input.copy(Bitmap.Config.ARGB_8888, true);

        Filter filter = new Filter();
        filter.addSubFilter(new SaturationSubfilter(saturation));
        output = filter.processFilter(input);

        preview.setImageBitmap(output);
    }

    public void applyBrightnessFilter(int brightness) {
        Bitmap input = this.input.copy(Bitmap.Config.ARGB_8888, true);

        Filter filter = new Filter();
        filter.addSubFilter(new BrightnessSubfilter(brightness));
        output = filter.processFilter(input);

        preview.setImageBitmap(output);
    }

    public void applyVignetteFilter(int strength) {
        Bitmap input = this.input.copy(Bitmap.Config.ARGB_8888, true);

        Filter filter = new Filter();
        filter.addSubFilter(new VignetteSubfilter(this, strength));
        output = filter.processFilter(input);

        preview.setImageBitmap(output);
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
        // Go back to main menu
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void nextStep() {
        Variables.finalImageBitmap = output;

        Intent intent = new Intent(this, CopySelector.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_toolbar, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        previousStep();
    }
}
