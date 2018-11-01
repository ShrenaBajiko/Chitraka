package com.example.dell.chitraka;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FcmInstanceIdService extends FirebaseInstanceIdService
{
    @Override
    public void onTokenRefresh() {
        String fcm_token=FirebaseInstanceId.getInstance().getToken();
        Log.d("FCM_TOKEN",fcm_token);
    }
}
