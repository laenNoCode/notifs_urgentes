package com.laen.notif_unlocker.SoundUnlocker;

import android.content.Context;
import android.media.AudioManager;

import com.laen.notif_unlocker.MainActivity;

public class SoundUnlocker {
    public static void unlock() {
        AudioManager am = MainActivity.mMainActivity.am;
        am.setStreamMute(AudioManager.STREAM_NOTIFICATION, false);
        am.setStreamVolume(AudioManager.STREAM_NOTIFICATION, am.getStreamMaxVolume(AudioManager.STREAM_NOTIFICATION), 0);
    }
}
