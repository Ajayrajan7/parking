package com.example.parkingfinderprojectfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ParSignUp extends AppCompatActivity {
    Button pSub;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL =MainActivity.u;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        setContentView(R.layout.activity_par_sign_up);
        pSub=findViewById(R.id.sign_up);
        Thread t1 =  new Thread() {
            @Override
            public void run() {
                pSub.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick (View view){
                        createParker();
                    }
                });
            }
        };
        t1.start();
    }

    private void createParker(){
        final EditText uname = findViewById(R.id.uname);
        final EditText email = findViewById(R.id.email);
        final EditText pname = findViewById(R.id.par_name);
        final EditText nSlots = findViewById(R.id.slots);
        final EditText loc = findViewById(R.id.loc);
        final  EditText mno = findViewById(R.id.num);
        final EditText pass = findViewById(R.id.edt_password);
        final EditText price = findViewById(R.id.price);

        HashMap<String,String> hp = new HashMap<String, String>();
        hp.put("uname",uname.getText().toString());
        hp.put("email",email.getText().toString());
        hp.put("pname",pname.getText().toString());
        hp.put("slots",nSlots.getText().toString());
        hp.put("loc",loc.getText().toString());
        hp.put("mno",mno.getText().toString());
        hp.put("pass",pass.getText().toString());
        hp.put("price",price.getText().toString());
        Call<Void> call = retrofitInterface.executeParSignup(hp);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if(response.code()==200){
                    Toast.makeText(ParSignUp.this,
                            "Signed up successfully", Toast.LENGTH_LONG).show();
                    Intent home = new Intent(ParSignUp.this,dummyactivity.class);
                    startActivity(home);
                    finish();
                }
                else if (response.code() == 400) {
                    Toast.makeText(ParSignUp.this,
                            "Already registered", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(ParSignUp.this, t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });



    }

}
