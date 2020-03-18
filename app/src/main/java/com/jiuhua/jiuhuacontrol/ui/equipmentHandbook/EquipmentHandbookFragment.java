package com.jiuhua.jiuhuacontrol.ui.equipmentHandbook;

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

public class EquipmentHandbookFragment extends Fragment {

    private EquipmentHandbookViewModel equipmentHandbookViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        equipmentHandbookViewModel =
                ViewModelProviders.of(this).get(EquipmentHandbookViewModel.class);
        View root = inflater.inflate(R.layout.fragment_equipment_handbook, container, false);
        final TextView textView = root.findViewById(R.id.equipmentHandbook);
        equipmentHandbookViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}