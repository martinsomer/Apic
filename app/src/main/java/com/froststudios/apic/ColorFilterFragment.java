package com.froststudios.apic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

public class ColorFilterFragment extends Fragment {

    private SeekBar red;
    private SeekBar green;
    private SeekBar blue;

    // Called when Fragment creates its View object hierarchy
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_color_filter, parent, false);
    }

    // Called soon after onCreateView()
    // Called Fragment has finished creating its View object hierarchy
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        red = view.findViewById(R.id.red);
        green = view.findViewById(R.id.green);
        blue = view.findViewById(R.id.blue);

        red.setProgress(0);
        green.setProgress(0);
        blue.setProgress(0);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                float redValue = (float) red.getProgress() / 100;
                float greenValue = (float) green.getProgress() / 100;
                float blueValue = (float) blue.getProgress() / 100;

                ((Filters)getActivity()).applyColorFilter(redValue, greenValue, blueValue);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        red.setOnSeekBarChangeListener(listener);
        green.setOnSeekBarChangeListener(listener);
        blue.setOnSeekBarChangeListener(listener);
    }

    // Called when Fragment becomes visible
    @Override
    public void onStart() {
        super.onStart();
    }
}
