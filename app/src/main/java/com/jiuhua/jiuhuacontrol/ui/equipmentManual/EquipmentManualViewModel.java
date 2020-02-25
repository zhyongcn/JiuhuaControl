package com.jiuhua.jiuhuacontrol.ui.equipmentManual;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EquipmentManualViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EquipmentManualViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is equipmentmaunal fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}