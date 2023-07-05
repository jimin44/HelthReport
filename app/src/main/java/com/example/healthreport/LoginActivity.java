package com.example.healthreport;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kakao.sdk.user.UserApiClient;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.widget.ImageButton;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {
    DatabaseReference databaseReference;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ImageButton kakao_login_button;
    TextView textView;
    long backpressedTime = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();

        databaseReference = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = getSharedPreferences("autoLogin", Activity.MODE_PRIVATE);
        editor  = sharedPreferences.edit();

        String id = sharedPreferences.getString("userId", "null");
        //String id = "null";
        kakao_login_button = findViewById(R.id.imageButton_kakao);
        textView = findViewById(R.id.textView_login);
        kakao_login_button.setVisibility(View.INVISIBLE);

        //Save Earth Live Healthy 글자색 변경
        String content = textView.getText().toString();
        SpannableString spannableString = new SpannableString(content);
        String word = "\"Save Earth Live Healthy\"";
        int start = content.indexOf(word);
        int end = start + word.length();
        spannableString.setSpan(new ForegroundColorSpan(Color.parseColor("#FF009688")), 59, 85, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);

        //텍스트효과
        YoYo.with(Techniques.FadeIn).duration(2500).playOn(textView);

        new Handler().postDelayed(new Runnable()
        {
            @Override
            public void run()
            {
                if(id.equals("null")){
                    kakao_login_button.setVisibility(View.VISIBLE);
                            kakao_login_button.setOnClickListener(new View.OnClickListener(){
                                @Override
                                public void onClick(View v) {
                                    if(UserApiClient.getInstance().isKakaoTalkLoginAvailable(LoginActivity.this)){
                                        login();
                                    }
                                    else{
                                        accountLogin();
                                    }

                                }
                            });
                        }
                        else{

                            databaseReference.child(id).child("name").get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                                @Override
                                public void onSuccess(DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.exists()){
                                        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                                        intent.putExtra("id", id);
                                        startActivity(intent);
                                        finish();
                                    }
                                    else{
                                        Intent intent = new Intent(getApplicationContext(),PutProfileActivity.class);
                                        intent.putExtra("id", id);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });

                        }
            }
        }, 4000);// 딜레이를 준 후 시작




    }

    public void login(){
        String TAG = "login()";
        UserApiClient.getInstance().loginWithKakaoTalk(LoginActivity.this,(oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                getUserInfo();
            }
            return null;
        });
    }

    public void accountLogin(){
        String TAG = "accountLogin()";
        UserApiClient.getInstance().loginWithKakaoAccount(LoginActivity.this,(oAuthToken, error) -> {
            if (error != null) {
                Log.e(TAG, "로그인 실패", error);
            } else if (oAuthToken != null) {
                Log.i(TAG, "로그인 성공(토큰) : " + oAuthToken.getAccessToken());
                getUserInfo();
            }
            return null;
        });
    }

    public void getUserInfo(){
        String TAG = "getUserInfo()";
        UserApiClient.getInstance().me((user, meError) -> {
            if (meError != null) {
                Log.e(TAG, "사용자 정보 요청 실패", meError);
            } else {
                System.out.println("로그인 완료");
                Log.i(TAG, user.toString());
                {
                    Log.i(TAG, "사용자 정보 요청 성공" + "\n회원번호: "+user.getId());
                }
                String mail = user.getKakaoAccount().getEmail().replace('.', ',');
                System.out.println("사용자 계정 " + mail);

                editor.putString("userId", mail);
                editor.commit();

                databaseReference.child(mail).get().addOnSuccessListener(new OnSuccessListener<DataSnapshot>() {
                    @Override
                    public void onSuccess(DataSnapshot dataSnapshot) {
                        //db에 정보가 있다면 메인으로 이동

                        if(dataSnapshot.child("name").exists()){
                            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                            intent.putExtra("id", mail);
                            startActivity(intent);
                            finish();
                        }
                        else{
                            databaseReference.child(mail).child("userNum").setValue(String.valueOf(user.getId()));
                            Intent intent = new Intent(getApplicationContext(),PutProfileActivity.class);
                            intent.putExtra("id", mail);
                            startActivity(intent);
                            finish();
                        }
                    }
                });

            }
            return null;
        });
    }

    private void getHashKey(){
        PackageInfo packageInfo = null;
        try {
            packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        if (packageInfo == null)
            Log.e("KeyHash", "KeyHash:null");

        for (Signature signature : packageInfo.signatures) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            } catch (NoSuchAlgorithmException e) {
                Log.e("KeyHash", "Unable to get MessageDigest. signature=" + signature, e);
            }
        }
    }

    public void onBackPressed() {

        if (System.currentTimeMillis() > backpressedTime + 2000) {
            backpressedTime = System.currentTimeMillis();
            Toast.makeText(this, "\'뒤로\' 버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT).show();
        } else if (System.currentTimeMillis() <= backpressedTime + 2000) {
            finish();
        }

    }

}