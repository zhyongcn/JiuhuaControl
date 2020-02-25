package com.jiuhua.jiuhuacontrol.ui.userinfo;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserInfoViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public UserInfoViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is gallery fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}