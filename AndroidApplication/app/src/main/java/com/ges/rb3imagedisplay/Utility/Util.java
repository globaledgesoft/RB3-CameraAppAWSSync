package com.ges.rb3imagedisplay.Utility;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build;

import androidx.core.app.ActivityCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Class for commonly used methods
 */
public class Util {

    private static final String TAG = Util.class.getSimpleName();

    /**
     * Method to create local directory in internal storage
     *
     * @param context
     * @return
     */
    public static File createLocalDirectory(Context context) {
        File rootFile = new File(Constants.ROOT_PATH, Constants.DOWNLOAD_FOLDER);
        if (!rootFile.exists()) {
            rootFile.mkdirs();
        }
        return rootFile;
    }

    /**
     * This method returns array of images present in specified path
     *
     * @param context
     * @return
     */
    public static String[] getImageList(Context context) {
        String[] fileNames = null;
        File path = new File(Constants.ROOT_PATH, Constants.DOWNLOAD_FOLDER);
        if (path.exists()) {
            fileNames = path.list();
        }
        return fileNames;
    }

    /**
     * This method is to read Csv File and parse ad save it in preference
     *
     * @param context
     * @param path
     * @throws FileNotFoundException
     */
    //
    private static void readFileData(Context context, String path) throws FileNotFoundException {
        String[] data = null;
        File file = new File(path);
        if (file.exists()) {
            BufferedReader br = new BufferedReader(new FileReader(file));

            try {
                String csvLine;
                while ((csvLine = br.readLine()) != null) {
                    data = csvLine.split(",");
                }
                putKeyInSharedPreference(context, data[0], data[1]);
            } catch (IOException ex) {
                throw new RuntimeException("Error in reading CSV file: " + ex);
            }
        }
    }

    /**
     * This method is to save data in Shared preference
     *
     * @param context
     * @param accessKey
     * @param secretKey
     */
    public static void putKeyInSharedPreference(Context context, String accessKey, String secretKey) {
        SharedPreferences pref = context.getSharedPreferences(Constants.KEY, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(Constants.ACCESS_KEY, accessKey);
        editor.putString(Constants.SECRET_KEY, secretKey);
        editor.putString(Constants.CUSTOMER_ENDPOINT, "");
        editor.apply();
    }

    /**
     * This method gets credentials.csv file present in internal storage
     *
     * @param context
     * @param dir
     * @param csvFiles
     * @return
     */
    public static CharSequence[] getfile(Context context, File dir, CharSequence[] csvFiles) {
        ArrayList<File> fileList = new ArrayList<File>();
        File listFile[] = dir.listFiles();
        if (listFile != null && listFile.length > 0) {
            for (int i = 0; i < listFile.length; i++) {
                if (listFile[i].getName().endsWith(Constants.CSV)) {
                    if (listFile[i].getName().equals(Constants.CREDENTIALS)) {
                        fileList.add(listFile[i]);
                        File yourFile = new File(dir, listFile[i].getName());
                        csvFiles = new CharSequence[]{yourFile.toString()};
                        try {
                            readFileData(context, yourFile.toString());
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }

                }
            }
        }
        return csvFiles;
    }

    /**
     * This method gets AWS keys saved in Shared Preference
     *
     * @param context
     * @return
     */
    public static String[] getKeyFromSharedPreference(Context context) {
        SharedPreferences sharedpreferences = context.getSharedPreferences(Constants.KEY,
                Context.MODE_PRIVATE);
        String accessKey = sharedpreferences.getString(Constants.ACCESS_KEY, "");
        String secreteKey = sharedpreferences.getString(Constants.SECRET_KEY, "");
        String regionEndpoint = sharedpreferences.getString(Constants.REGION, "");
        return new String[]{accessKey, secreteKey, regionEndpoint};
    }


    /**
     * Method to display Error dialog
     *
     * @param context
     * @param title
     */
    public static void errorDialog(final Context context, String title) {
        final AlertDialog.Builder alt_bld = new AlertDialog.Builder(context);
        alt_bld.setTitle(title);
        alt_bld.setCancelable(true);
        alt_bld.setPositiveButton(Constants.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    /**
     * Method to check internet connectivity
     *
     * @param context
     * @return
     */
    public static boolean checkInternet(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
            return networkInfo != null && networkInfo.isConnected();
        }

        Network[] networks = connectivityManager.getAllNetworks();
        boolean hasInternet = false;
        if (networks.length > 0) {
            for (Network network : networks) {
                NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(network);
                if (nc.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET))
                    hasInternet = true;
            }
        }
        return hasInternet;
    }

    /**
     * Method to check Read/Write Permission
     *
     * @param activity
     * @return
     */
    public static boolean isStoragePermissionGranted(Activity activity) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) {
                return true;
            } else {
                Logger.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, Constants.REQUEST_CODE_STORAGE);
                return false;
            }
        } else {
            Logger.v(TAG, "Permission is granted");
            return true;
        }
    }
}

