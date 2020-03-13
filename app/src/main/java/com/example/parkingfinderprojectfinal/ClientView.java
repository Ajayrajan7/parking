package com.example.parkingfinderprojectfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ClientView extends AppCompatActivity {
    public static String ARG_CLIENT_NAME = "ARG_CATEGORY_NAME";

    TextView cname,cno,slots,cpname,iloc,price,loc;
    Button cancel;
    RelativeLayout mBack;
    private String n;
    MainActivity s = new MainActivity();
    EditText et;
    String id;
    public static String[] coordinates= new String[2];
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = MainActivity.u;
    private Context mContext =ClientView.this;
    private static final String TAG = "clientView";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_view);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        Intent i = getIntent();
        n = i.getStringExtra(ARG_CLIENT_NAME);
        mBack = findViewById(R.id.back);
        cancel=findViewById(R.id.cancel);
        MainActivity m = new MainActivity();
        id=m.id;

        Thread t1 = new Thread(){
            @Override
            public void run() {
                HashMap<String,String> hp = new HashMap<>();
                hp.put("cname",n);
                hp.put("id",id);
                Call<FetchClientDetails> call = retrofitInterface.executeparclient(hp);
                call.enqueue(new Callback<FetchClientDetails>() {
                    @Override
                    public void onResponse(Call<FetchClientDetails> call, Response<FetchClientDetails> response) {
                        if(response.code()==200){
                            FetchClientDetails result = response.body();
                            cname=(TextView)(findViewById(R.id.cpname));
                            cname.setText(result.getcName());
                            cpname=(TextView)(findViewById(R.id.s));
                            cpname.setText(result.getpName());
                            loc =(TextView)(findViewById(R.id.s1));
                            loc.setText(result.getLoc());
                            iloc=(TextView)(findViewById(R.id.iloc));
                            iloc.setText(result.getLoc());
                            slots=(TextView)(findViewById(R.id.s2));
                            slots.setText(result.getHours());
                            cno =(TextView)(findViewById(R.id.s3));
                            cno.setText(result.getcMno());
                            price =(TextView)(findViewById(R.id.price));
                            price.setText(result.getPrice());
                        }else if(response.code()==404){
                            Toast.makeText(ClientView.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchClientDetails> call, Throwable t) {
                        Toast.makeText(ClientView.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        t1.start();
        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                make_cancel();
            }
        });

    }
    private void make_cancel(){
        Thread t2 = new Thread(){
            @Override
            public void run() {
                HashMap<String,String> map2 = new HashMap<>();
                map2.put("cname",n);
                map2.put("id",id);
                Call<Void> call =retrofitInterface.executeclientcancel(map2);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()==200){
                            Toast.makeText(ClientView.this,"You have successfully cancelled",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(ClientView.this,clientList.class);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {

                    }
                });
            }
        };
        t2.start();
    }
}
