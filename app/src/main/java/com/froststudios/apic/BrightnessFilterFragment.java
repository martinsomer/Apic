package com.froststudios.apic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

public class BrightnessFilterFragment extends Fragment {

    private SeekBar brightness;
    private int max = 200;
    private int min = 0;
    private float multiplier = 1.5f;

    // Called when Fragment creates its View object hierarchy
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_brightness_filter, parent, false);
    }

    // Called soon after onCreateView()
    // Called Fragment has finished creating its View object hierarchy
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        brightness = view.findViewById(R.id.brightness);
        brightness.setMax(max - min);
        brightness.setProgress((max - min) / 2);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                int brightnessValue = (int) brightness.getProgress() - (int) (max / multiplier);
                ((Filters)getActivity()).applyBrightnessFilter(brightnessValue);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        brightness.setOnSeekBarChangeListener(listener);
    }

    // Called when Fragment becomes visible
    @Override
    public void onStart() {
        super.onStart();
    }
}