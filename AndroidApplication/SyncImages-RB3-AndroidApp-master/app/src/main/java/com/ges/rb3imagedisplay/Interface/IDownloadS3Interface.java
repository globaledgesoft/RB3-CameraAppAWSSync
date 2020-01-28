package com.ges.rb3imagedisplay.Interface;

public interface IDownloadS3Interface {
    void onDownloadSuccess(String response, int count);

    void onDownloadError(String response);

}

