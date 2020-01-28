package com.ges.rb3imagedisplay.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.ges.rb3imagedisplay.Model.ImageModel;
import com.ges.rb3imagedisplay.R;
import com.ges.rb3imagedisplay.Utility.Logger;

import java.util.ArrayList;

/**
 * Adapter class for ViewPager
 */
public class ImageViewPagerAdapter extends PagerAdapter {
    private static final String TAG = ImageViewPagerAdapter.class.getSimpleName();
    private Activity mActivity;
    private ArrayList<ImageModel> modelArrayList;
    private LayoutInflater inflater;

    public ImageViewPagerAdapter(Activity activity,
                                 ArrayList<ImageModel> imageModels) {
        this.mActivity = activity;
        this.modelArrayList = imageModels;
    }

    @Override
    public int getCount() {
        return this.modelArrayList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imgDisplay;
        TextView imgTitle;
        inflater = (LayoutInflater) mActivity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewLayout = inflater.inflate(R.layout.layout_viewpager_item, container,
                false);

        imgDisplay = (ImageView) viewLayout.findViewById(R.id.imgDisplay);
        imgTitle = (TextView) viewLayout.findViewById(R.id.textView_imageTitle);
        imgDisplay.setImageBitmap(modelArrayList.get(position).getBitmap());
        imgTitle.setText(modelArrayList.get(position).getImageTitle());
        Logger.d(TAG, "getCount" + getCount());
        ((ViewPager) container).addView(viewLayout);
        return viewLayout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).removeView((RelativeLayout) object);
    }
}
