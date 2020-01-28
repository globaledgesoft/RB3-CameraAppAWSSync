package com.ges.rb3imagedisplay.aws;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.mobileconnectors.s3.transferutility.TransferListener;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferObserver;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferState;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.ges.rb3imagedisplay.Activity.MainActivity;
import com.ges.rb3imagedisplay.Interface.IDownloadS3Interface;
import com.ges.rb3imagedisplay.R;
import com.ges.rb3imagedisplay.Uility.Constants;
import com.ges.rb3imagedisplay.Uility.Util;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class S3Downloader {
    private static final String TAG = S3Downloader.class.getSimpleName();

    private Context context;
    private File rootFile;
    private TransferUtility transferUtility;
    private IDownloadS3Interface s3DownloadInterface;

    public S3Downloader(Context context) {
        this.context = context;
        this.rootFile = Util.createLocalDirectory(context);
    }

    /**
     * This method initializes download from AWS S3 bucket
     *
     * @param fileName
     * @param imageCount
     */
    public void initDownload(String fileName, int imageCount) {
        File fileToDownload = new File(rootFile.getPath(), fileName);

        TransferObserver transferObserver = transferUtility.download(
                Constants.BUCKET_NAME,
                fileName,
                fileToDownload
        );

        transferObserver.setTransferListener(new DownloadListener(imageCount));
    }

    /**
     * Methos to execute DownloadFromS3AsyncTask
     */
    public void initDownloadFromS3AsyncTask() {
        new DownloadFromS3AsyncTask().execute();
    }

    private class DownloadListener implements TransferListener {
        int imageCount = 0;

        public DownloadListener(int count) {
            this.imageCount = count;
        }

        @Override
        public void onError(int id, Exception e) {
            Log.e(TAG, "Error during upload: " + id, e);
            s3DownloadInterface.onDownloadError(e.toString());
            s3DownloadInterface.onDownloadError("Error");
        }

        @Override
        public void onProgressChanged(int id, long bytesCurrent, long bytesTotal) {
            Log.d(TAG, String.format("onProgressChanged: %d, total: %d, current: %d",
                    id, bytesTotal, bytesCurrent));
        }

        @Override
        public void onStateChanged(int id, TransferState newState) {
            Log.d(TAG, "onStateChanged: " + id + ", " + newState);
            if (newState == TransferState.COMPLETED) {
                s3DownloadInterface.onDownloadSuccess("Success", imageCount);
            }
        }
    }

    /**
     * Async task to download file list from AWS S3
     */
    public class DownloadFromS3AsyncTask extends AsyncTask<Void, Void, List<String>> {
        AmazonS3Client amazonS3Client;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            amazonS3Client = AmazonUtil.getS3Client(context);
            transferUtility = AmazonUtil.getTransferUtility(context, amazonS3Client);
        }

        @Override
        protected List<String> doInBackground(Void... voids) {

            return getObjectNamesForBucket(Constants.BUCKET_NAME, amazonS3Client);
        }

        @Override
        protected void onPostExecute(List<String> strings) {
            super.onPostExecute(strings);
            Log.d(TAG, "strings " + strings.size());
            for (String file : strings) {
                if (file.equalsIgnoreCase("AmazonS3Exception")) {
                    Util.errorDialog(context, context.getResources().getString(R.string.error_region));
                    MainActivity.isAccessed = false;
                    s3DownloadInterface.onDownloadError(context.getString(R.string.response_error));
                } else {
                    MainActivity.isAccessed = true;
                    initDownload(file, strings.size());
                }
            }
        }
    }

    /**
     * @param bucket
     * @param s3Client
     * @return object with list of files
     * @desc This method is used to return list of files name from S3 Bucket
     */
    private List<String> getObjectNamesForBucket(String bucket, AmazonS3 s3Client) {
        List<String> objectNames = new ArrayList<String>();
        try {
            ObjectListing objects = s3Client.listObjects(bucket);
            Iterator<S3ObjectSummary> iterator = objects.getObjectSummaries().iterator();
            while (iterator.hasNext()) {
                objectNames.add(iterator.next().getKey());
            }
            while (objects.isTruncated()) {
                objects = s3Client.listNextBatchOfObjects(objects);
                iterator = objects.getObjectSummaries().iterator();
                while (iterator.hasNext()) {
                    objectNames.add(iterator.next().getKey());
                }
            }
        } catch (AmazonS3Exception e) {
            objectNames.add("AmazonS3Exception");
        }
        return objectNames;
    }

    public void setOns3DownloadDone(IDownloadS3Interface s3DownloadInterface) {
        this.s3DownloadInterface = s3DownloadInterface;
    }

}
