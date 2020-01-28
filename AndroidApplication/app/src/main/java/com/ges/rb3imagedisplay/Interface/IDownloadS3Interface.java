package com.ges.rb3imagedisplay.Interface;

/**
 * Interface for S3 download status
 */
public interface IDownloadS3Interface {
    void onDownloadSuccess(String response, int count);

    void onDownloadError(String response);

}

