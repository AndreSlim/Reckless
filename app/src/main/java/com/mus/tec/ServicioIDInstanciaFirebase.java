package com.mus.tec;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by andres on 22/09/17.
 */

public class ServicioIDInstanciaFirebase extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String token = FirebaseInstanceId.getInstance().getToken();

        Log.i("FIrebase", "El token es:"+ token);

    }
}
