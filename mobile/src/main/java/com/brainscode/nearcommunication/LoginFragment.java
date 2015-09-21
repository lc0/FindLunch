package com.brainscode.nearcommunication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

/**
 * Created by platerosanchezm on 18/09/15.
 */

public class LoginFragment extends Fragment {
    LoginButton loginButton;
    CallbackManager callbackManager;

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Intent myIntent = new Intent(getActivity(), PlacesActivity.class);
        getActivity().startActivity(myIntent);

        View view = inflater.inflate(R.layout.activity_find_lunch, container, false);
        return view;

//        loginButton = (LoginButton) view.findViewById(R.id.login_button);
//        loginButton.setReadPermissions("user_friends");
//        // If using in a fragment
//        loginButton.setFragment(this);
//        // Other app specific specialization
//
//        // Callback registration
//        callbackManager = CallbackManager.Factory.create();
//        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
//            @Override
//            public void onSuccess(LoginResult loginResult) {
//                // App code
//                // App code
//                Log.i("WOW", "CONNECTED!!!");
//                Intent myIntent = new Intent(getActivity(), PlacesActivity.class);
//                getActivity().startActivity(myIntent);
//            }
//
//            @Override
//            public void onCancel() {
//                Log.i("WOW", "onCancel!!!");
//                // App code
//            }
//
//            @Override
//            public void onError(FacebookException exception) {
//                Log.i("WOW", "onError!!!");
//                // App code
//            }
//        });
//        return view;
    }
}
