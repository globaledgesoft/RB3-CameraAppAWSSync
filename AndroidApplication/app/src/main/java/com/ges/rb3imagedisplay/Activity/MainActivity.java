package com.ges.rb3imagedisplay.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.regions.Regions;
import com.ges.rb3imagedisplay.Adapters.GridviewAdapter;
import com.ges.rb3imagedisplay.Interface.IDownloadS3Interface;
import com.ges.rb3imagedisplay.Model.ImageModel;
import com.ges.rb3imagedisplay.R;
import com.ges.rb3imagedisplay.Utility.Constants;
import com.ges.rb3imagedisplay.Utility.Logger;
import com.ges.rb3imagedisplay.Utility.Util;
import com.ges.rb3imagedisplay.aws.S3Downloader;

import java.io.File;
import java.util.ArrayList;

/**
 * This is the launcher activity. This class is responsible to display
 * the images downloaded from AWS S3 bucket in a GridView after the necessary
 * permissions are granted.
 */
public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getCanonicalName();
    private ProgressDialog progressDialog;
    private S3Downloader s3downloaderObj;
    private GridView mGridView;
    private ArrayList<ImageModel> mModelList = new ArrayList<>();
    private static int counter = 0;
    private String[] imageList;
    private CharSequence[] csvFiles = {};
    private CharSequence[] regionEndpoints = {};
    private String regionKey;
    private File root;
    public boolean isAccessed;
    private TextView textView_noImages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        loadImages();

    }

    /**
     * This method loads images to Grid view
     */
    private void loadImages() {
        if (imageList != null && imageList.length > 0)
            updateGridView(imageList);
        else {
            if (Util.isStoragePermissionGranted(this)) {
                checkCredential();
            }
        }
    }

    /**
     * This method initialize the views
     */
    private void initViews() {
        progressDialog = new ProgressDialog(this);
        mGridView = (GridView) findViewById(R.id.simpleGridView);
        imageList = Util.getImageList(getBaseContext());
        textView_noImages = (TextView) findViewById(R.id.textview_no_images);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.refresh:
                if (Util.isStoragePermissionGranted(this)) {
                    String isAccessed = Util.getKeyFromSharedPreference(this)[3];
                    if (isAccessed.equalsIgnoreCase(getString(R.string.string_true)))
                        downloadImagesS3();
                    else checkCredential();
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method sets Gridview adapeter
     */
    private void setGridAdapter() {
        GridviewAdapter gridviewAdapter = new GridviewAdapter(getApplicationContext(), mModelList);
        mGridView.setAdapter(gridviewAdapter);
        if (mModelList.isEmpty()) {
            textView_noImages.setVisibility(View.VISIBLE);
            mGridView.setVisibility(View.GONE);
        } else {
            textView_noImages.setVisibility(View.GONE);
            mGridView.setVisibility(View.VISIBLE);
        }
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), ViewPagerActivity.class);
                intent.putExtra(Constants.INTENT_POSITION, i);
                startActivity(intent);
            }
        });
        hideLoading();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            checkCredential();
            Logger.d(TAG, "Permission: " + permissions[0] + "was " + grantResults[0]);
        } else {
            Toast.makeText(this, R.string.toast_permission_required, Toast.LENGTH_LONG).show();
            finish();
        }
    }

    /**
     * This method downloads images from AWS S3 bucket
     */
    private void downloadImagesS3() {
        showLoading(getString(R.string.progress_download_s3));
        s3downloaderObj = new S3Downloader(this);
        s3downloaderObj.initDownloadFromS3AsyncTask();
        s3downloaderObj.setOns3DownloadDone(new IDownloadS3Interface() {
            @Override
            public void onDownloadSuccess(String response, int count) {
                if (response.equalsIgnoreCase(getString(R.string.response_success))) {
                    counter++;
                    Logger.d(TAG, "onDownloadSuccess");
                    if (count == counter) {
                        counter = 0;
                        imageList = Util.getImageList(getBaseContext());
                        Logger.d(TAG, "imageList.length " + imageList.length);
                        updateGridView(imageList);
                    }
                }
            }

            @Override
            public void onDownloadError(String response) {
                hideLoading();
                Logger.e(TAG, "Error onDownload");
            }
        });
    }

    /**
     * This method fetches images from local path and updates to GridView
     *
     * @param imageList
     */
    private void updateGridView(String[] imageList) {
        mModelList.clear();
        File path = new File(Constants.ROOT_PATH, Constants.DOWNLOAD_FOLDER);
        for (String str : imageList) {
            ImageModel imageModel = new ImageModel();
            Bitmap mBitmap = BitmapFactory.decodeFile(path + "/" + str);
            imageModel.setBitmap(mBitmap);
            imageModel.setImageTitle(str);
            mModelList.add(imageModel);

        }
        setGridAdapter();
    }

    /**
     * This method loads Progress Dialog
     *
     * @param message
     */
    private void showLoading(String message) {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.setMessage(message);
            progressDialog.setCancelable(false);
            progressDialog.show();
        }
    }

    /**
     * This method hides Progress Dialog
     */
    private void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) {
            Logger.d(TAG, "hideLoading");
            progressDialog.dismiss();
        }
    }


    /**
     * Async class for finding the root of Internal Storage and check for credentials.csv
     */
    public class CSVFileAsyncTask extends AsyncTask<Void, Void, CharSequence[]> {
        private ProgressDialog progressDialog;

        public CSVFileAsyncTask(MainActivity activity) {
            progressDialog = new ProgressDialog(activity);
        }

        @Override
        protected void onPreExecute() {
            progressDialog.setMessage(getString(R.string.progress_csv_files));
            progressDialog.setCancelable(false);
            progressDialog.show();
        }


        @Override
        protected CharSequence[] doInBackground(Void... params) {
            try {
                root = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath());
                csvFiles = Util.getfile(MainActivity.this, root, csvFiles);
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return csvFiles;
        }

        @Override
        protected void onPostExecute(CharSequence[] result) {
            progressDialog.dismiss();
            if (result != null && result.toString().isEmpty()) {
                Toast.makeText(MainActivity.this, R.string.toast_error_csvfile, Toast.LENGTH_SHORT).show();
            } else {
                openCredentialsDialog();
            }
        }
    }

    /**
     * Method to open CSV Credentials dialog
     */

    public void openCredentialsDialog() {
        final AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setTitle(R.string.credentials);
        alt_bld.setCancelable(false);
        if (csvFiles.length == 0) {
            alt_bld.setMessage(R.string.noItemFound);
        } else {
            alt_bld.setSingleChoiceItems(csvFiles, -1, new DialogInterface
                    .OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    openRegionsDialog();
                    dialog.dismiss();
                }
            });
        }
        alt_bld.setNegativeButton(Constants.CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    /**
     * Method to open Region Endpoint dialog
     */
    public void openRegionsDialog() {
        final AlertDialog.Builder alt_bld = new AlertDialog.Builder(this);
        alt_bld.setCancelable(false);
        regionEndpoints = new CharSequence[]{Regions.GovCloud.getName(), Regions.US_EAST_1.getName(), Regions.US_WEST_1.getName(), Regions.US_WEST_2.getName(), Regions.EU_WEST_1.getName(), Regions.EU_CENTRAL_1.getName(), Regions.AP_SOUTH_1.getName(), Regions.AP_SOUTHEAST_1.getName(), Regions.AP_SOUTHEAST_2.getName(), Regions.AP_NORTHEAST_1.getName(), Regions.AP_NORTHEAST_2.getName(), Regions.SA_EAST_1.getName(), Regions.CN_NORTH_1.getName()};
        alt_bld.setTitle(R.string.regionEndpoint);
        alt_bld.setCancelable(false);
        alt_bld.setSingleChoiceItems(regionEndpoints, -1, new DialogInterface
                .OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {
                regionKey = (String) regionEndpoints[item];
                SharedPreferences pref = getSharedPreferences(Constants.KEY, 0);
                SharedPreferences.Editor editor = pref.edit();
                isAccessed = true;
                editor.putString(Constants.REGION, regionKey);
                Logger.d(TAG, "REGION INSERT" + regionKey);
                editor.apply();
            }
        });
        alt_bld.setPositiveButton(Constants.OK, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                downloadImagesS3();
            }
        });
        alt_bld.setNegativeButton(Constants.CANCEL, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                dialog.dismiss();
            }
        });
        AlertDialog alert = alt_bld.create();
        alert.show();
    }

    /**
     * Method to open AWS Credentials dialog
     */

    public void checkCredential() {
        if (Util.checkInternet(this)) {
            AlertDialog.Builder alertdialog = new AlertDialog.Builder(MainActivity.this);
            alertdialog.setTitle(R.string.dialogAwsTitle);
            alertdialog.setCancelable(false);
            alertdialog.setMessage(R.string.dialogAwsMessage)
                    .setPositiveButton(Constants.OK, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            CSVFileAsyncTask task = new CSVFileAsyncTask(MainActivity.this);
                            task.execute();
                        }
                    })
                    .setNegativeButton(Constants.CANCEL, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int i) {
                            dialog.dismiss();
                        }
                    });
            alertdialog.show();
        } else
            Toast.makeText(this, R.string.toast_internet_connection, Toast.LENGTH_LONG).show();
    }

}
