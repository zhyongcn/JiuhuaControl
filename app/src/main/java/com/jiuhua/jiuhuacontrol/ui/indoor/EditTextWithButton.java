package com.jiuhua.jiuhuacontrol.ui.indoor;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.jiuhua.jiuhuacontrol.R;
//TODO: 自定义view的很多情况可以使用约束布局了，这个自定义没有必要了。
//TODO: 另外约束布局好像效率也高一些。
public class EditTextWithButton extends LinearLayout implements View.OnClickListener {
    private int setting_temperature;
    private EditText editText;
    private ImageView addImageView, subImageView;

    //构造函数
    public EditTextWithButton(Context context) {
        super(context);
    }

    public EditTextWithButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextWithButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public EditTextWithButton(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }


    private void initview(Context context) {
        View inflate = View.inflate(context, R.layout.edittext_withbutton,this);
        editText = inflate.findViewById(R.id.set_temperature);
        addImageView = inflate.findViewById(R.id.temperatureAddButton);
        subImageView = inflate.findViewById(R.id.temperatureReduceButtton);

        addImageView.setOnClickListener(this);
        subImageView.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.temperatureAddButton:
                setting_temperature++;
                break;
            case R.id.temperatureReduceButtton:
                setting_temperature--;
                break;
        }

    }


}
