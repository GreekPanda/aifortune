package cn.aifortune.baseframe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by liaohh on 17-3-17.
 */

public class IntroActivity extends AppCompatActivity {
    private ViewPager mViewPage;
    private MyViewPagerAdapter myViewPagerAdapter;
    private LinearLayout dotsLayout;
    private TextView[] dots;
    private int[] layouts;
    private Button btnSkip, btnNext;
    private PrefManager prefManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        prefManager = new PrefManager(this);
        if (!prefManager.isFirstTimeLaunch()) {
            launchHomeScreen();
            finish();
        }

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }


        setContentView(R.layout.activity_welcome);

        mViewPage = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layout_dots);
        btnNext = (Button) findViewById(R.id.btn_next);
        btnSkip = (Button) findViewById(R.id.btn_skip);

        layouts = new int[]{
                R.layout.welcome_slide1,
                R.layout.welcome_slide2,
                R.layout.welcome_slide3
        };

        addBottomDots(0);

        changeStatusBarColor();


        myViewPagerAdapter = new MyViewPagerAdapter();
        mViewPage.setAdapter(myViewPagerAdapter);
        mViewPage.addOnPageChangeListener(viewPagerChangeListener);

        btnSkip.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                launchHomeScreen();
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                int current = getItem(+1);
                if (current < layouts.length) {
                    mViewPage.setCurrentItem(current);
                } else {
                    launchHomeScreen();
                }
            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[layouts.length];

        int[] colorActive = getResources().getIntArray(R.array.array_dots_active);
        int[] colorInActive = getResources().getIntArray(R.array.array_dots_inactive);

        dotsLayout.removeAllViews();

        for (int i = 0; i < dots.length; ++i) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(colorInActive[currentPage]);
            dotsLayout.addView(dots[i]);
        }


        if (dots.length > 0) {
            dots[currentPage].setTextColor(colorActive[currentPage]);
        }
    }

    private int getItem(int index) {
        return (mViewPage.getCurrentItem() + index);
    }


    private void launchHomeScreen() {
        prefManager.setFirstTimeLaunch(false);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private void changeStatusBarColor() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

    }

    ViewPager.OnPageChangeListener viewPagerChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addBottomDots(position);

            if (position == layouts.length - 1) {
                btnNext.setText(getString(R.string.start));
                btnSkip.setVisibility(View.GONE);
            } else {
                btnNext.setText(R.string.next);
                btnSkip.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    public class MyViewPagerAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public MyViewPagerAdapter() {
        }

        @Override
        public int getCount() {
            return layouts.length;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(layouts[position], container, false);
            container.addView(view);
            return view;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }
}