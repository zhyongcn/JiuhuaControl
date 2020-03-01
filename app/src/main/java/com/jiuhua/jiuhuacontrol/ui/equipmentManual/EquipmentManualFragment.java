package com.jiuhua.jiuhuacontrol.ui.equipmentManual;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.jiuhua.jiuhuacontrol.R;

public class EquipmentManualFragment extends Fragment {

    private EquipmentManualViewModel equipmentManualViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        equipmentManualViewModel =
                ViewModelProviders.of(this).get(EquipmentManualViewModel.class);
        View root = inflater.inflate(R.layout.fragment_equipmentmanual, container, false);
        final TextView textView = root.findViewById(R.id.equipmentManual);
        equipmentManualViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}