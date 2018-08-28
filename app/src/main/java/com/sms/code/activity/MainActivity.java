package com.sms.code.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.MenuItem;

import com.sms.code.R;
import com.sms.code.adapter.ViewPagerAdapter;
import com.sms.code.fragment.CodeFragment;
import com.sms.code.fragment.MyInfoFragment;
import com.sms.code.utils.CommonSharePref;

/**
 * File description
 *
 * @author gao
 * @date 2018/8/28
 */

public class MainActivity extends BaseActivity implements ViewPager.OnPageChangeListener, BottomNavigationView.OnNavigationItemSelectedListener {
    private ViewPager mViewPager;
    private BottomNavigationView mBottomNavigationView;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void initViews() {
        mBottomNavigationView = findViewById(R.id.bottom_nv);
        mViewPager = findViewById(R.id.main_viewpager);
        initListener();
    }

    @Override
    public void initData() {

    }

    private void gotoLogin() {
        String token = CommonSharePref.getInstance(this).getToken();
        if (TextUtils.isEmpty(token.trim())) {
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
    }

    private void initListener() {
        mBottomNavigationView.setOnNavigationItemSelectedListener(this);
        //系统默认选中第一个,但是系统选中第一个不执行onNavigationItemSelected(MenuItem)方法,如果要求刚进入页面就执行clickTabOne()方法,则手动调用选中第一个
        mBottomNavigationView.setSelectedItemId(R.id.tab_code);//根据具体情况调用
        mViewPager.addOnPageChangeListener(this);
        //为viewpager设置adapter
        ViewPagerAdapter pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFragment(new CodeFragment());
        pagerAdapter.addFragment(new MyInfoFragment());
        mViewPager.setAdapter(pagerAdapter);
        mViewPager.setOffscreenPageLimit(2);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //ViewPager和BottomNaviationView联动,当ViewPager的某个页面被选中了,同时设置BottomNaviationView对应的tab按钮被选中
        switch (position) {
            case 0:
                mBottomNavigationView.setSelectedItemId(R.id.tab_code);
                break;
            case 1:
                mBottomNavigationView.setSelectedItemId(R.id.tab_my);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //BottomNaviationView和ViewPager联动,当BottomNaviationView的某个tab按钮被选中了,同时设置ViewPager对应的页面被选中
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.tab_code:
                mViewPager.setCurrentItem(0, false);
                return true;
            case R.id.tab_my:
                mViewPager.setCurrentItem(1, false);
                return true;

            default:
                break;
        }
        return false;
    }
}
