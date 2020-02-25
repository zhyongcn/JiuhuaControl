package com.jiuhua.jiuhuacontrol.ui.partsstore;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.jiuhua.jiuhuacontrol.R;

public class PartsStoreFragment extends Fragment {

    private PartsStoreViewModel mViewModel;

    public static PartsStoreFragment newInstance() {
        return new PartsStoreFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.parts_store_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(PartsStoreViewModel.class);
        // TODO: Use the ViewModel
    }

}
