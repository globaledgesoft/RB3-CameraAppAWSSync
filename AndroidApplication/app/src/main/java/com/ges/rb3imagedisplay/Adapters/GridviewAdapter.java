package com.ges.rb3imagedisplay.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ges.rb3imagedisplay.Model.ImageModel;
import com.ges.rb3imagedisplay.R;

import java.util.ArrayList;

public class GridviewAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ImageModel> mImageList;
    private LayoutInflater inflater;

    public GridviewAdapter(Context applicationContext, ArrayList<ImageModel> imageModelArrayList) {
        this.context = applicationContext;
        this.mImageList = imageModelArrayList;
        this.inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return mImageList.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.layout_gridview_item, null);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        icon.setImageBitmap(mImageList.get(i).getBitmap());
        return view;
    }
}