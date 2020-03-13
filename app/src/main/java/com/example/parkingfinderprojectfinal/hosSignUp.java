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

public class hosSignUp extends AppCompatActivity {

    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = MainActivity.u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hos_sign_up);


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        findViewById(R.id.login_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigate2login();
            }
        });

        findViewById(R.id.sign_up).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleSignUp();
            }
        });
    }
    private void navigate2login(){
        Intent home = new Intent(hosSignUp.this,MainActivity.class);
        startActivity(home);
        finish();
    }

    private void handleSignUp(){
        Thread t2 = new Thread(){
            @Override
            public void run() {
                final EditText uname = findViewById(R.id.uname);
                final EditText email = findViewById(R.id.email);
                final EditText pname = findViewById(R.id.par_name);
                final EditText loc = findViewById(R.id.loc);
                final  EditText mno = findViewById(R.id.num);
                final EditText pass = findViewById(R.id.edt_password);

                HashMap<String,String> hp = new HashMap<String, String>();
                hp.put("uname",uname.getText().toString());
                hp.put("email",email.getText().toString());
                hp.put("pname",pname.getText().toString());
                hp.put("loc",loc.getText().toString());
                hp.put("mno",mno.getText().toString());
                hp.put("pass",pass.getText().toString());

                Call<Void> call = retrofitInterface.executehosSignUp(hp);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()==200){
                            Toast.makeText(hosSignUp.this,
                                    "Signed up successfully", Toast.LENGTH_LONG).show();
                            Intent home = new Intent(hosSignUp.this,dummyactivity.class);
                            startActivity(home);
                            finish();
                        }else if (response.code() == 400) {
                            Toast.makeText(hosSignUp.this,
                                    "Already registered", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(hosSignUp.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        t2.start();
    }

}
