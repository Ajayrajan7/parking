package com.example.parkingfinderprojectfinal;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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

public class foodActivity extends AppCompatActivity {
    public static String ARG_PLACE_NAME = "ARG_CATEGORY_NAME";
    TextView name,loc,slots,mno,ipname,iloc,price;
    Button book;
    RelativeLayout mBack;
    private String n;
    MainActivity s = new MainActivity();
    EditText et;
    public static String[] coordinates= new String[2];
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = MainActivity.u;
    private Context mContext =foodActivity.this;
    private static final String TAG = "foodActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food);

        name =  (TextView)(findViewById(R.id.s));
        Intent i = getIntent();
        n = i.getStringExtra(ARG_PLACE_NAME);
        mBack = findViewById(R.id.back);
        book=findViewById(R.id.book);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        PlaceActivity b = new PlaceActivity();

        Thread t1 = new Thread(){
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                map.put("fname", n);
                Call<FetchFoodDetails> call = retrofitInterface.executeFoodDetail(map);
                call.enqueue(new Callback<FetchFoodDetails>() {
                    @Override
                    public void onResponse(Call<FetchFoodDetails> call, Response<FetchFoodDetails> response) {
                        if(response.code()==200){
                            FetchFoodDetails result =response.body();
                            name.setText(result.getpName());
                            ipname=(TextView)(findViewById(R.id.ipname));
                            ipname.setText(result.getpName());
                            loc =(TextView)(findViewById(R.id.s1));
                            loc.setText(result.getLoc());
                            iloc=(TextView)(findViewById(R.id.iloc));
                            iloc.setText(result.getLoc());
                            mno =(TextView)(findViewById(R.id.s3));
                            mno.setText(result.getMno());
                            coordinates[0]=result.getCoordinate1();
                            coordinates[1]=result.getCoordinate2();
                            b.coordinates=coordinates;
                        }else if(response.code()==404){
                            Toast.makeText(foodActivity.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchFoodDetails> call, Throwable t) {
                        Toast.makeText(foodActivity.this, t.getMessage(),
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



        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(foodActivity.this,par_map.class);
                startActivity(i);
            }
        });
    }
}
