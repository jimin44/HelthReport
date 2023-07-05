package com.example.healthreport.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.DialogFragment;

import com.example.healthreport.LoginActivity;
import com.example.healthreport.MainActivity;
import com.example.healthreport.R;
import com.google.firebase.database.DatabaseReference;

public class DialogFragmentWithdraw extends DialogFragment {
    Button ok;
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View myView = inflater.inflate(R.layout.dialogfragment_withdraw, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ok = myView.findViewById(R.id.button_user_withdraw_ok);

        databaseReference = ((MainActivity)getActivity()).databaseReference;
        sharedPreferences = getActivity().getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        SharedPreferences.Editor editor  = sharedPreferences.edit();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                databaseReference.removeValue();
                editor.clear();
                editor.commit();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                MainActivity.mainActivity.finish();
                startActivity(intent);
                dismiss();
            }
        });

        return myView;
    }
}
