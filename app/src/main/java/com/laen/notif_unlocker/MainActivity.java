package com.laen.notif_unlocker;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.media.AudioManager;
import android.os.Bundle;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.common.AccountPicker;
import com.laen.notif_unlocker.EventHandlers.FileCreateButtonHandler;
import com.laen.notif_unlocker.TokenManager.DriveToken;
import com.laen.notif_unlocker.TokenManager.tokenRequiredTask;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private GoogleSignInClient mGoogleSignInClient;
    private static final int REQUEST_CODE_SIGN_IN = 0;
    private static final int REQUEST_CODE_CAPTURE_IMAGE = 1;
    private static final int REQUEST_CODE_CREATOR = 2;
    public static MainActivity mMainActivity;
    public AccountManager mAccountManager;
    public Resources res;
    public tokenRequiredTask taskToCallback;
    public Context con;
    public AudioManager am;
    public FileCreateButtonHandler fileCreateButtonHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        res = getResources();
        con = this;
        mAccountManager = AccountManager.get(this);
        mMainActivity = this;
        taskToCallback = null;
        fileCreateButtonHandler = new FileCreateButtonHandler();
        Intent account_intent = AccountPicker.newChooseAccountIntent(null, null, new String[]{"com.google"}, false, null, null,null, null);
        MainActivity.mMainActivity.startActivityForResult(account_intent, MainActivity.mMainActivity.res.getInteger(R.integer.GETTOKENRESULTINTENT));
        am = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        FileCreateButtonHandler.ScheduleJob();
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ((Button) findViewById(R.id.create_file)).setOnClickListener(fileCreateButtonHandler);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == R.integer.GETTOKENRESULTINTENT)
        {
            if (taskToCallback != null)
            {
                DriveToken.grantToken(taskToCallback, this);
            }
        }
    }
    public void OnButtonClick(){}
}