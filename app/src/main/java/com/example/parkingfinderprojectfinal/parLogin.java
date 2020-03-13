package com.example.parkingfinderprojectfinal;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class parLogin extends AppCompatActivity {
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL =MainActivity.u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_par_login);
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
    }
    private void handleLogin(){
        final EditText emailEdit = findViewById(R.id.edt_email);
        final EditText passwordEdit = findViewById(R.id.edt_password);

        HashMap<String, String> map = new HashMap<>();
        map.put("email", emailEdit.getText().toString());
        map.put("password", passwordEdit.getText().toString());

        Thread t1 =new Thread(){
            @Override
            public void run() {
                Call<LoginparResult> call = retrofitInterface.executeparLogin(map);
                call.enqueue(new Callback<LoginparResult>() {
                    @Override
                    public void onResponse(Call<LoginparResult> call, Response<LoginparResult> response) {
                        if(response.code()==200){
                            LoginparResult result=response.body();
                            MainActivity.id=""+result.getId();
                            MainActivity.token=""+"par";
                            Intent home = new Intent(parLogin.this,dummyactivity.class);
                            startActivity(home);
                            finish();
                        }else if (response.code() == 404) {
                            Toast.makeText(parLogin.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<LoginparResult> call, Throwable t) {
                        Toast.makeText(parLogin.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();

                    }
                });
            }
        };
        t1.start();
    }
}
