package com.ges.rb3imagedisplay.Model;

import android.graphics.Bitmap;

/**
 * Model class for Images
 */
public class ImageModel {
    public Bitmap bitmap;
    public String imageTitle;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getImageTitle() {
        return imageTitle;
    }

    public void setImageTitle(String imageTitle) {
        this.imageTitle = imageTitle;
    }

    @Override
    public String toString() {
        return "ImageModel{" +
                "bitmap=" + bitmap +
                ", imageTitle='" + imageTitle + '\'' +
                '}';
    }


}
