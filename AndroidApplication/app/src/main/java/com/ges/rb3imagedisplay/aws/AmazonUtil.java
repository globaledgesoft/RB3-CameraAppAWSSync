package com.ges.rb3imagedisplay.aws;

import android.content.Context;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.mobileconnectors.s3.transferutility.TransferUtility;
import com.amazonaws.regions.Region;
import com.amazonaws.services.s3.AmazonS3Client;
import com.ges.rb3imagedisplay.Utility.Logger;
import com.ges.rb3imagedisplay.Utility.Util;

/**
 * This class helps in getting BasicAWSCredentials provider instance
 * and TransferUtility instance
 */
public class AmazonUtil {

    private static AmazonS3Client sS3Client;
    private static TransferUtility sTransferUtility;
    private static BasicAWSCredentials sAWSCredentials;
    private static final String TAG = AmazonUtil.class.getSimpleName();

    /**
     * Gets an instance of BasicAWSCredentials which is
     * constructed using the given Context.
     *
     * @param context
     * @param accessKey
     * @param secreteKey
     * @return BasicAWSCredentials instance
     */
    protected static BasicAWSCredentials getCredProvider(Context context, String accessKey, String secreteKey) {
        if (sAWSCredentials == null) {
            sAWSCredentials = new BasicAWSCredentials(accessKey, secreteKey);
        }
        return sAWSCredentials;
    }

    /**
     * Gets an instance of a S3 client which is constructed using the given
     * Context.
     *
     * @param context An Context instance.
     * @return A default S3 client.
     */
    public static AmazonS3Client getS3Client(Context context) {
        String[] keys = Util.getKeyFromSharedPreference(context);
        sS3Client = new AmazonS3Client(getCredProvider(context, keys[0], keys[1]));
        sS3Client.setRegion(Region.getRegion(keys[2]));
        Logger.d(TAG, "REGION GET" + keys[2]);
        return sS3Client;
    }

    /**
     * Gets an instance of the TransferUtility which is constructed using the
     * given Context
     *
     * @param context
     * @param s3Client
     * @return TransferUtility instance
     */
    public static TransferUtility getTransferUtility(Context context, AmazonS3Client s3Client) {
        sTransferUtility = new TransferUtility(s3Client, context);
        return sTransferUtility;
    }

}

