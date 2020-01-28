package com.ges.rb3imagedisplay.Utility;

import android.os.Environment;

/**
 * This class contains all constants being used throughout the code
 */
public class Constants {
    public static final String ROOT_PATH = Environment.getExternalStorageDirectory().toString() + "/Android/data/" + "com.ges.rb3imagedisplay";
    public static final String DOWNLOAD_FOLDER = "S3Images";

    //Dialog related
    public static final String OK = "Ok";
    public static final String CANCEL = "Cancel";
    public static final String CSV = ".csv";
    public static final String CREDENTIALS = "credentials.csv";

    // Shared Preference related
    public static final String REGION = "region";
    public static final String KEY = "key";
    public static final String ACCESS_KEY = "accessKey";
    public static final String SECRET_KEY = "secretKey";
    public static final String CUSTOMER_ENDPOINT = "customer_endpoint";


    //Intent Keys
    public static final String INTENT_POSITION = "position";

    //AWS related
    public static final String BUCKET_NAME = "rb3imgesupload";

    //Permission related
    public static final int REQUEST_CODE_STORAGE = 1;


}
