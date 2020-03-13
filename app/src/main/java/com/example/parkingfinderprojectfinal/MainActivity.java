package com.example.parkingfinderprojectfinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static String id="";
    public static  String token="";
    public static String u ="https://c604faf2.ngrok.io";
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL =u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        findViewById(R.id.btn_sign).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLogin();
            }
        });
        findViewById(R.id.login_create_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate2signup();
            }
        });
        findViewById(R.id.other_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate2Login();
            }
        });
    }
    private void handleLogin(){
//       Button loginBtn = findViewById(R.id.btn_sign);
        final EditText emailEdit = findViewById(R.id.edt_email);
        final EditText passwordEdit = findViewById(R.id.edt_password);

        HashMap<String, String> map = new HashMap<>();
        map.put("email", emailEdit.getText().toString());
        map.put("password", passwordEdit.getText().toString());

        Thread t1 = new Thread(){
                    @Override
                    public void run() {

                        Call<LoginResult> call = retrofitInterface.executeLogin(map);
                        call.enqueue(new Callback<LoginResult>() {
                            @Override
                            public void onResponse(Call<LoginResult> call, Response<LoginResult> response) {
                                if (response.code() == 200) {
                                    LoginResult result = response.body();
//                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
//                            builder1.setTitle(result.getName());
//                            builder1.setMessage(result.getId());
//                            builder1.show();
                                    id=id+result.getId();
                                    token="user";
                                    Intent home = new Intent(MainActivity.this,dummyactivity.class);
                                    startActivity(home);
                                    finish();

                                } else if (response.code() == 404) {
                                    Toast.makeText(MainActivity.this, "Wrong Credentials",
                                            Toast.LENGTH_LONG).show();
                                }

                            }

                            @Override
                            public void onFailure(Call<LoginResult> call, Throwable t) {

                                Toast.makeText(MainActivity.this, t.getMessage(),
                                        Toast.LENGTH_LONG).show();

                            }
                        });
                    }
                };
                t1.start();

}

    private void navigate2signup(){
//        Intent home = new Intent(MainActivity.this,SignUp.class);
//        startActivity(home);
//        finish();
        Dialog dialog;
//        View view = getLayoutInflater().inflate(R.layout.login_dialog, null);
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//
//        builder.setView(view).show();
        dialog=new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.button_dialog);
        Button uBtn = dialog.findViewById(R.id.login);

        Button pBtn = dialog.findViewById(R.id.par_login);

        Button wBtn = dialog.findViewById(R.id.ws_login);
        Button hBtn = dialog.findViewById(R.id.hos_login);
        Button fBtn = dialog.findViewById(R.id.food_login);
        ImageView close =(ImageView)(dialog.findViewById(R.id.close));

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        uBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(MainActivity.this,SignUp.class);
                startActivity(home);
                finish();
            }
        });
        pBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(MainActivity.this,ParSignUp.class);
                startActivity(home);
                finish();
            }
        });
        wBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(MainActivity.this,wsSignup.class);
                startActivity(home);
                finish();
            }
        });
        hBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(MainActivity.this,hosSignUp.class);
                startActivity(home);
                finish();
            }
        });

        fBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(MainActivity.this,foodSignUp.class);
                startActivity(home);
                finish();
            }
        });
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }
    private void navigate2Login(){
        View view = getLayoutInflater().inflate(R.layout.login_option_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setView(view).show();
        Button pBtn = view.findViewById(R.id.par_login);
        pBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(MainActivity.this,parLogin.class);
                startActivity(home);
                finish();
            }
        });
    }
}
