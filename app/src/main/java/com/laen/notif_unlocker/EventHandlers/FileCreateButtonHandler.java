package com.laen.notif_unlocker.EventHandlers;

import android.annotation.SuppressLint;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.ComponentName;
import android.net.NetworkRequest;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.laen.notif_unlocker.MainActivity;
import com.laen.notif_unlocker.R;
import com.laen.notif_unlocker.RequestSender;
import com.laen.notif_unlocker.TokenManager.DriveToken;
import com.laen.notif_unlocker.TokenManager.tokenRequiredTask;
import com.laen.notif_unlocker.storage.AppData;

import java.io.IOException;

public class FileCreateButtonHandler implements View.OnClickListener, tokenRequiredTask {
    public static void ScheduleJob(){
        JobInfo.Builder builder = new JobInfo.Builder(42, new ComponentName(MainActivity.mMainActivity.con, DrivePoll.class));
        builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
        builder.setPeriodic((long) 1000 * MainActivity.mMainActivity.res.getInteger(R.integer.actualisationTimeSec));
        JobScheduler scheduler = (JobScheduler) MainActivity.mMainActivity.getSystemService(MainActivity.JOB_SCHEDULER_SERVICE);
        scheduler.schedule(builder.build());
    }
    private String fileName;
    private String secret;
    @Override
    public void onClick(View v) {
        fileName = ((TextInputEditText) MainActivity.mMainActivity.findViewById(R.id.fileName)).getText().toString();
        secret = ((TextInputEditText) MainActivity.mMainActivity.findViewById(R.id.secret)).getText().toString();
        DriveToken.grantToken(this, MainActivity.mMainActivity.con);
    }

    @Override
    public void callback(String token) {

        Log.i("TAG", "onClick: " + token);
        String drive_uri = MainActivity.mMainActivity.res.getString(R.string.drive_file_uri);
        Log.i("TAG", "onClick: " + drive_uri);
        RequestSender sender = new RequestSender(token, drive_uri);
        String fileGoogleId = "";
        String[] data;
        try {
            fileGoogleId = sender.CreateFile(fileName);
            Log.i("e", "onClick: create");
            AppData.storeData(fileGoogleId,secret, MainActivity.mMainActivity.con);
            data = AppData.getStoredData(MainActivity.mMainActivity.con);
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        Log.i("tag", "onClick: " + data[0] + ";" + data[1]);
        ScheduleJob();
    }
}
