package com.jiuhua.jiuhuacontrol.ui.equipmentHandbook;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EquipmentHandbookViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public EquipmentHandbookViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is equipmentmaunal fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}