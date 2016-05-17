package com.example.sejol.secsys.Utilidades;

import android.content.Context;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.io.Serializable;

/**
 * Created by sejol on 7/5/2016.
 */
public class Firebase_Controller implements Serializable{
    String url = "https://secsys.firebaseio.com/BUS";
    Firebase myFirebaseRef;
    Context ctx;
    Storage data;

    public Firebase_Controller(Context ctx) {
        this.ctx = ctx;
        Firebase.setAndroidContext(ctx);
        myFirebaseRef = new Firebase(url);
        data = new Storage();
        busListener();
    }

    public void loadUser(){

    }

    public void busListener(){
        myFirebaseRef.child("lat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //data.setLatitud((String) snapshot.getValue());
            }
            @Override public void onCancelled(FirebaseError error) { }
        });
        myFirebaseRef.child("lng").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                //data.setLongitud((String)snapshot.getValue());
            }
            @Override public void onCancelled(FirebaseError error) { }
        });
    }

    public void writeLtd(double ltd){
        myFirebaseRef.child("lat").setValue(String.valueOf(ltd));
    }

    public void writeLng(double lng){
        myFirebaseRef.child("lng").setValue(String.valueOf(lng));
    }

    private class Storage implements Serializable{

    }
}
