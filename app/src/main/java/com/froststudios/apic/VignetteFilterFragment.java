package com.froststudios.apic;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

public class VignetteFilterFragment extends Fragment {


    private SeekBar vignette;

    // Called when Fragment creates its View object hierarchy
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_vignette_filter, parent, false);
    }

    // Called soon after onCreateView()
    // Called Fragment has finished creating its View object hierarchy
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        vignette = view.findViewById(R.id.vignette);
        vignette.setMax(255);
        vignette.setProgress(0);

        SeekBar.OnSeekBarChangeListener listener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                int strength = vignette.getProgress();
                ((Filters)getActivity()).applyVignetteFilter(strength);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        };

        vignette.setOnSeekBarChangeListener(listener);
    }

    // Called when Fragment becomes visible
    @Override
    public void onStart() {
        super.onStart();
    }
}