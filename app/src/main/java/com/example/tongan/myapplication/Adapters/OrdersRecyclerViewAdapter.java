package com.example.tongan.myapplication.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.tongan.myapplication.Classes.PostService;
import com.example.tongan.myapplication.R;

import java.util.ArrayList;

public class OrdersRecyclerViewAdapter extends PagerAdapter {

    private Context context;
    private LayoutInflater layoutInflater;

    private ArrayList<PostService> postServicesAL;

    public OrdersRecyclerViewAdapter(Context context, ArrayList<PostService> postServicesAL) {
        this.context = context;
        this.postServicesAL = postServicesAL;
    }

    @Override
    public int getCount() {
        return postServicesAL.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(@NonNull ViewGroup viewGroup, int position) {

        layoutInflater = layoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.orders_recycler_view_list_item, viewGroup, false);

        return view;
    }

}
