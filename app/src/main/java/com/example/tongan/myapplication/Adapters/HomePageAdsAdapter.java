package com.example.tongan.myapplication.Adapters;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.tongan.myapplication.R;

public class HomePageAdsAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;
    private int[] ads = {R.drawable.test_ads1, R.drawable.test_ads2};

    public HomePageAdsAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return ads.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {

        layoutInflater = layoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.home_page_ads_layout, container, false);
        ImageView imageView = view.findViewById(R.id.adsImageView);
        imageView.setImageResource(ads[position]);

        ViewPager vp = (ViewPager) container;
        vp.addView(view, 0);
        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout)object);
    }


}
