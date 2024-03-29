package com.laen.notif_unlocker.TokenManager;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.common.AccountPicker;
import com.laen.notif_unlocker.MainActivity;
import com.laen.notif_unlocker.R;
import com.laen.notif_unlocker.RequestSender;

import java.time.Instant;

class TokenGranter extends AsyncTask<Void, Void, Void>{
    tokenRequiredTask task;
    Context ctx;
    public TokenGranter(tokenRequiredTask task, Context ctx){
       this.task = task;
       this.ctx = ctx;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        long now = Instant.now().getEpochSecond();
        if (DriveToken.hasToken && now - DriveToken.lastTimestamp > MainActivity.mMainActivity.res.getInteger(R.integer.refreshTokenTime))
        {
            task.callback(DriveToken.token);
            return null;
        }

        Account[] accounts = AccountManager.get(ctx).getAccountsByType("com.google");
        if (accounts.length == 0) {
            Intent account_intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"}, false, null, null, null, null);
            MainActivity.mMainActivity.startActivityForResult(account_intent, MainActivity.mMainActivity.res.getInteger(R.integer.GETTOKENRESULTINTENT));
            throw new RuntimeException("No account picked");
        }
        Account account = accounts[0];
        Log.i("TAG", "doInBackground: started" +ctx.toString() +  " " + account.toString());
        try {
            String s = GoogleAuthUtil.getToken(ctx, account, "oauth2:https://www.googleapis.com/auth/drive.file", new Bundle());
            Log.i("TAG", "doInBackground: " + s);
            RequestSender sender = new RequestSender(s, "https://www.googleapis.com/drive/v3/files");
            DriveToken.hasToken = true;
            DriveToken.token = s;
            DriveToken.lastTimestamp =  Instant.now().getEpochSecond();
            task.callback(s);
        } catch (UserRecoverableAuthException e) {
            MainActivity.mMainActivity.taskToCallback = task;
            Log.i("TAG", "doInBackground: e");
            MainActivity.mMainActivity.startActivityForResult(e.getIntent(), 2);
        } catch (Exception e) {
            Log.e("TAG", "onActivityResult: ", e);
        }
        return null;
    }
}
public class DriveToken {
    public static String token;
    public static boolean hasToken;
    public static long lastTimestamp;

    public static void grantToken(tokenRequiredTask toGrantTo, Context ctx){
        TokenGranter tg = new TokenGranter(toGrantTo, ctx);
        tg.execute();
    }
}
