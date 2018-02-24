package com.microsoft.CognitiveServicesExample;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by rishavg on 6/11/17.
 */

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {
    private static final String TAG="MyFirebaseIIDService";

    @Override
    public void onTokenRefresh() {
        //getting registration token
        String refreshedToken= FirebaseInstanceId.getInstance().getToken();
        //Displaing token on logcat
        Log.d(TAG,"Refreshed token:"+refreshedToken);
    }
    private void sendRegistrationToServer(String token)
    {
        //you can implement this method to store the token on server
        //Not req for current project
    }
}
