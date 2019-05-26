package com.froststudios.apic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

public class ContrastFilterFragment extends Fragment {

    private SeekBar contrast;
    private int max = 250;
    private int min = 50;
    private float multiplier = 2;

    // Called when Fragment creates its View object hierarchy
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_contrast_filter, parent, false);
    }

    // Called soon after onCreateView()
    // Called Fragment has finished creating its View object hierarchy
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        contrast = view.findViewById(R.id.contrast);
        contrast.setMax(max - min);
        contrast.setProgress((max - min) / 2);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                float contrastValue = (float) (contrast.getProgress() + min) / (max / multiplier);
                ((Filters)getActivity()).applyContrastFilter(contrastValue);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        contrast.setOnSeekBarChangeListener(listener);
    }

    // Called when Fragment becomes visible
    @Override
    public void onStart() {
        super.onStart();
    }
}
