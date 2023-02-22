package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater inflater;
    public SliderAdapter(Context context) {
        this.context = context;
    }

    int images[] = {
            R.drawable.welcome,
            R.drawable.inst
    };


    int headings[] = {
            R.string.first_slide_title,
            R.string.second_slide_title
    };

    int descri[] = {
            R.string.first_slide_desc,
            R.string.second_slide_desc
    };


    @Override
    public int getCount() {
        return headings.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (ConstraintLayout) object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slides_layout , container , false);

        ImageView imageView = view.findViewById(R.id.sliderImage);
        TextView heading = view.findViewById(R.id.sliderHeading);
        TextView desc = view.findViewById(R.id.sliderDesc);

        imageView.setImageResource(images[position]);
        heading.setText(headings[position]);
        desc.setText(descri[position]);

        container.addView(view);


        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((ConstraintLayout) object);

    }
}
