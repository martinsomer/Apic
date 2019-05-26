package com.froststudios.apic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

public class SaturationFilterFragment extends Fragment {

    private SeekBar saturation;
    private int max = 250;
    private int min = 50;
    private float multiplier = 2;

    // Called when Fragment creates its View object hierarchy
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_saturation_filter, parent, false);
    }

    // Called soon after onCreateView()
    // Called Fragment has finished creating its View object hierarchy
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        saturation = view.findViewById(R.id.saturation);
        saturation.setMax(max - min);
        saturation.setProgress((max - min) / 2);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                float saturationValue = (float) (saturation.getProgress() + min) / (max / multiplier);
                ((Filters)getActivity()).applySaturationFilter(saturationValue);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        saturation.setOnSeekBarChangeListener(listener);
    }

    // Called when Fragment becomes visible
    @Override
    public void onStart() {
        super.onStart();
    }
}
