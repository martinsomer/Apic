package com.froststudios.apic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ItemsAdapter extends ArrayAdapter<Item> {
    ItemsAdapter(Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        final Item item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        // Lookup view for data population
        ImageView image = convertView.findViewById(R.id.image);
        TextView amount = convertView.findViewById(R.id.amount);
        Button addImage = convertView.findViewById(R.id.addImage);
        Button subtractImage = convertView.findViewById(R.id.subtractImage);
        ImageButton delete = convertView.findViewById(R.id.delete);

        // Add listeners for clicks
        addImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
            Variables.itemsArray.get(position).amount += 1;
            updateList();
            }
        });

        subtractImage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
            if (Variables.itemsArray.get(position).amount > 1) {
                Variables.itemsArray.get(position).amount -= 1;
                updateList();
            }
            }
        });

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                confirmDelete(position);
            }
        });

        // Populate the data into the template view using the data object
        //image.setImageBitmap(item.bitmap);
        image.setImageBitmap(getCroppedBitmap(item.bitmap));
        amount.setText(String.valueOf(item.amount));

        // Return the completed view to render on screen
        return convertView;
    }

    // Scale down bitmaps to avoid lag
    private Bitmap getCroppedBitmap(Bitmap src) {
        Bitmap dst;
        if (src.getWidth() >= src.getHeight()) {
            dst = Bitmap.createBitmap(
                    src,
                    src.getWidth()/2 - src.getHeight()/2,
                    0,
                    src.getHeight(),
                    src.getHeight()
            );

        } else {
            dst = Bitmap.createBitmap(
                    src,
                    0,
                    src.getHeight()/2 - src.getWidth()/2,
                    src.getWidth(),
                    src.getWidth()
            );
        }


        // On OnePlus 3 screen, 150px is 75dp
        // This is the threshold where image quality is good enough
        // Convert this into pixels depending on device
        int targetSize = 75 * (int) getContext().getResources().getDisplayMetrics().density;
        return Bitmap.createScaledBitmap(dst, targetSize, targetSize, true);
    }

    private void updateList() {
        ((ReviewImages)getContext()).updateItemsList();
        ((ReviewImages)getContext()).updateTotalPrice();
    }

    private void confirmDelete(final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder
                .setMessage("Remove item?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Variables.itemsArray.remove(position);
                        updateList();
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
