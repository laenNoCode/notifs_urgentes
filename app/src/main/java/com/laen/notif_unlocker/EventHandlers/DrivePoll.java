package com.laen.notif_unlocker.EventHandlers;

import android.app.Notification;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.util.Log;

import com.laen.notif_unlocker.MainActivity;
import com.laen.notif_unlocker.R;
import com.laen.notif_unlocker.RequestSender;
import com.laen.notif_unlocker.SoundUnlocker.SoundUnlocker;
import com.laen.notif_unlocker.TokenManager.DriveToken;
import com.laen.notif_unlocker.TokenManager.tokenRequiredTask;
import com.laen.notif_unlocker.storage.AppData;

import java.io.File;
import java.io.IOException;

public class DrivePoll extends JobService implements tokenRequiredTask {
    private String driveName;
    private String secret;
    @Override
    public boolean onStartJob(JobParameters params) {
        Log.i("TAG", "onStartJob: ");
        try{
            String[] data = AppData.getStoredData(this);
            if (data[0].isEmpty() || data[1].isEmpty())
                return false;
            Log.i("TAG", "onStartJob: data" + data[0] + " : " + data[1]);
            this.driveName = data[0];
            this.secret = data[1];
            DriveToken.grantToken(this, this);
        }
        catch (IOException e){
            Log.e("TAG", "onStartJob: ", e);
            return false;
        }
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters params) {
        return false;
    }

    @Override
    public void callback(String token) {
        RequestSender sender = new RequestSender(token, getResources().getString(R.string.drive_file_get_uri));
        try {
            String data = sender.getFileData(driveName);
            Log.i("TAG", "callback: " + data);
            if (data.compareTo(this.secret) == 0){
                SoundUnlocker.unlock();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
