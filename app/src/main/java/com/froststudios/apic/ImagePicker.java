package com.froststudios.apic;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ImagePicker extends AppCompatActivity {

    private int PICK_IMAGE = 200;
    private Uri originalImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_picker);

        setTitle("Select image");

        openGallery();
    }

    // Get image from gallery
    private void openGallery() {
        Intent gallery = new Intent();
        gallery.setAction(Intent.ACTION_GET_CONTENT);
        gallery.setType("image/*");

        String[] mimeTypes = {"image/jpg", "image/jpeg","image/png"};
        gallery.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        startActivityForResult(gallery, PICK_IMAGE);
    }

    // Activity results
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Image selection result
        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE && data != null) {
            originalImageUri = data.getData();
            nextStep();
        } else {
            //Toast.makeText(this, "Image selection failed.", Toast.LENGTH_SHORT).show();
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
        Variables.originalImageUri = originalImageUri;
        Intent intent = new Intent(this, Crop.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}
