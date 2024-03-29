package com.laen.notif_unlocker.TokenManager;

import android.accounts.AccountManagerCallback;
import android.accounts.AccountManagerFuture;
import android.os.Bundle;
import android.util.Log;

public class OnTokenAcquired implements AccountManagerCallback<Bundle> {
    @Override
    public void run(AccountManagerFuture<Bundle> result){
        try {
            Bundle b = result.getResult();
        }
        catch (Exception e){
            Log.e("NOTIF_UNLOCKER", e.getMessage());
        }
    }
}