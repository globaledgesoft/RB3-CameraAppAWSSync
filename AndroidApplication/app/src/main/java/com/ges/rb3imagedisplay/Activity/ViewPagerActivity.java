package com.ges.rb3imagedisplay.Activity;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.viewpager.widget.ViewPager;

import com.ges.rb3imagedisplay.Adapters.ImageViewPagerAdapter;
import com.ges.rb3imagedisplay.Model.ImageModel;
import com.ges.rb3imagedisplay.R;
import com.ges.rb3imagedisplay.Utility.Constants;
import com.ges.rb3imagedisplay.Utility.Logger;
import com.ges.rb3imagedisplay.Utility.Util;

import java.io.File;
import java.util.ArrayList;

import static android.view.View.FOCUS_LEFT;
import static android.view.View.FOCUS_RIGHT;

/**
 * This class is reponsible to display single image in a ViewPager which on swipe shows
 * all the images downloaded from AWS S3 bucket.
 */
public class ViewPagerActivity extends Activity {
    private static final String TAG = ViewPagerActivity.class.getSimpleName();
    private ViewPager viewPager;
    private ImageViewPagerAdapter ImageViewadapter;
    private ArrayList<ImageModel> imageModelArrayList = new ArrayList<>();
    private ImageView imgLeftSwipe;
    private ImageView imgRightSwipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpager);
        initViews();
    }

    /**
     * This method initialize the views
     */
    private void initViews() {
        viewPager = findViewById(R.id.pager);
        imgLeftSwipe = (ImageView) findViewById(R.id.img_swipe_left);
        imgRightSwipe = (ImageView) findViewById(R.id.img_swipe_right);
        imgRightSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d(TAG, "RIGHT SWIPE " + FOCUS_RIGHT);
                viewPager.arrowScroll(FOCUS_RIGHT);
            }
        });
        imgLeftSwipe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Logger.d(TAG, "LEFT SWIPE " + FOCUS_LEFT);
                viewPager.arrowScroll(FOCUS_LEFT);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        getImageList();
        ImageViewadapter = new ImageViewPagerAdapter(this, imageModelArrayList);
        viewPager.setAdapter(ImageViewadapter);
        int pos = getIntent().getIntExtra(Constants.INTENT_POSITION, 0);
        viewPager.setCurrentItem(pos);
        setSwipeButtonAction(pos);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Logger.d(TAG, "onPageSelected " + position);
                setSwipeButtonAction(position);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    /**
     * This method performs left and right swipe action button visibility
     *
     * @param pos
     */
    private void setSwipeButtonAction(int pos) {

        if (pos == 0) {
            imgLeftSwipe.setVisibility(View.GONE);
            imgRightSwipe.setVisibility(View.VISIBLE);

        } else if (pos == imageModelArrayList.size() - 1) {
            imgLeftSwipe.setVisibility(View.VISIBLE);
            imgRightSwipe.setVisibility(View.GONE);
        } else if (pos > 0 && pos < imageModelArrayList.size() - 1) {
            imgLeftSwipe.setVisibility(View.VISIBLE);
            imgRightSwipe.setVisibility(View.VISIBLE);
        }

    }

    /**
     * This method gets all the images from specified path
     */
    public void getImageList() {
        File path = new File(Constants.ROOT_PATH, Constants.DOWNLOAD_FOLDER);
        String[] imageList = Util.getImageList(getBaseContext());
        for (String str : imageList) {
            ImageModel imageModel = new ImageModel();
            Bitmap mBitmap = BitmapFactory.decodeFile(path + File.separator + str);
            imageModel.setBitmap(mBitmap);
            imageModel.setImageTitle(str);
            imageModelArrayList.add(imageModel);

        }
    }
}