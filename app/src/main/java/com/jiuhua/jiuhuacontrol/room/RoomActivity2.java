package com.jiuhua.jiuhuacontrol.room;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jiuhua.jiuhuacontrol.R;

import java.util.ArrayList;
import java.util.List;

public class RoomActivity2 extends AppCompatActivity {

    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private List<Fragment> fragmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room2);
        //TODO 接收Intent的参数

        fragmentList = new ArrayList<Fragment>();
        fragmentList.add(new HistoryFragment());
        fragmentList.add(new RoomFragment());
        fragmentList.add(new PeroidFragment());

        viewPager = findViewById(R.id.viewpager);
        pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setCurrentItem(1);   //当前的页面是1号界面（0是开始），将来添加要调整fragmentList里面的页面顺序，
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void onBackPressed() { //如果不想直接退出activity，这里需要override。
        super.onBackPressed();
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {

        public ScreenSlidePagerAdapter(FragmentManager supportFragmentManager) {
            super(supportFragmentManager);

        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);  //??没有具体使用估计有问题，
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }
    }

    public static void jumpToRoomActivity(Context context) {
        Intent intent = new Intent(context, RoomActivity2.class);
        //intent.putExtra("RoomID",roomid);
        //TODO 许多参数
        context.startActivity(intent);
    }

}
