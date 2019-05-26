package com.froststudios.apic;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import java.io.File;
import java.io.IOException;

import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.model.AspectRatio;

public class Crop extends AppCompatActivity {

    private Uri croppedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        setTitle("Crop Photo");

        Uri originalImageUri = Variables.originalImageUri;
        croppedImageUri = Uri.fromFile(new File(getCacheDir(), "crop.png"));

        UCrop.Options options = new UCrop.Options();

        options.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimaryDark)); // Status bar color
        options.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary)); // Toolbar color
        options.setToolbarWidgetColor(ContextCompat.getColor(this, android.R.color.white)); // Toolbar text color

        options.setActiveWidgetColor(ContextCompat.getColor(this, R.color.colorPrimary)); // Selected aspect ratio indicator and text color

        options.setActiveControlsWidgetColor(ContextCompat.getColor(this, android.R.color.white)); // Selected tool icon color
        // No background color for tools background :c

        options.setToolbarTitle("Crop Photo");
        options.setFreeStyleCropEnabled(false);


        options.setAspectRatioOptions(1,
                new AspectRatio("2:3", 2, 3),
                new AspectRatio("3:2", 3, 2));

        UCrop.of(originalImageUri, croppedImageUri)
                .withOptions(options)
                .start(this);
    }

    // Activity results
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Image crop result
        if (resultCode == RESULT_OK && requestCode == UCrop.REQUEST_CROP && data != null) {
            croppedImageUri = UCrop.getOutput(data);
            nextStep();
        } else {
            //Toast.makeText(this, "Cropping failed.", Toast.LENGTH_SHORT).show();
            previousStep();
        }
    }

    private void previousStep() {
        // Go back to main menu
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    private void nextStep() {
        try {
            Variables.finalImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), croppedImageUri);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(this, Filters.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
