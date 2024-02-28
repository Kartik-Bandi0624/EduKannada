package com.example.edukannada.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.edukannada.R;

public class WelcomeActivity extends AppCompatActivity /*implements View.OnClickListener*/ {

    private final int[] layouts = {R.layout.fragment_first_slide, R.layout.fragment_second_slide, R.layout.fragment_third_slide, R.layout.fragment_fourth_slide};
    private ViewPager view_pager;
    private LinearLayout Dots_Layout;
    private Button btnNext;
    private Button btnBack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        view_pager = findViewById(R.id.viewPager);

        MpagerAdapter mPagerAdapter = new MpagerAdapter(layouts, this);

        view_pager.setAdapter(mPagerAdapter);

        Dots_Layout = findViewById(R.id.dotsLayout);

        btnNext = findViewById(R.id.next);
        btnBack = findViewById(R.id.back);
        Button btnSkip = findViewById(R.id.skip);

        Button btnBuy = findViewById(R.id.buyNow);

        btnBack.setOnClickListener(v -> {
            if (getItem(0) > 0) {
                view_pager.setCurrentItem(getItem(-1), true);
            }
        });

        btnNext.setOnClickListener(v -> {
            if (getItem(0) < 3) {
                view_pager.setCurrentItem(getItem(1), true);
            }
        });
        btnSkip.setOnClickListener(v -> startActivity(new Intent(this, MainActivity.class)));

        btnBuy.setOnClickListener(v -> startActivity(new Intent(this, Payment.class)));

        createDots(0);

        view_pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                createDots(position);
                if (position > 0) {
                    btnBack.setVisibility(View.VISIBLE);
                } else {
                    btnBack.setVisibility(View.INVISIBLE);
                }
                if (position >= 3) {
                    btnNext.setVisibility(View.INVISIBLE);
                } else {
                    btnNext.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private int getItem(int i) {
        return view_pager.getCurrentItem() + i;
    }

    private void createDots(int current_position) {
        if (Dots_Layout != null) {
            Dots_Layout.removeAllViews();
            ImageView[] dots = new ImageView[layouts.length];

            for (int i = 0; i < layouts.length; i++) {
                dots[i] = new ImageView(this);
                if (i == current_position) {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.active_dots));
                } else {
                    dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.inactive_dots));
                }
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                params.setMargins(4, 0, 4, 0);
                Dots_Layout.addView(dots[i], params);
            }
        }
    }
//    @Override
//    public void onClick(View view) {
//        int viewId = view.getId();
//
//        if (viewId == R.id.next) {
//            // Code for R.id.next
//        } else if (viewId == R.id.back) {
////            loadHome();
//        }
//    }

//    private void loadHome() {
//        startActivity(new Intent(this, MainActivity.class));
//        finish();
//    }
}