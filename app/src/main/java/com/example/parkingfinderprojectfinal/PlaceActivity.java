package com.example.parkingfinderprojectfinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
//viewing the particular clicked parking area
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

public class PlaceActivity extends AppCompatActivity {
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
    private Context mContext =PlaceActivity.this;
    private static final String TAG = "placeactivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place);

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

        Thread t1 = new Thread(){
            @Override
            public void run() {
                HashMap<String, String> map = new HashMap<>();
                map.put("pname", n);
                Call<FetchDetails> call = retrofitInterface.executegetADetails(map);
                call.enqueue(new Callback<FetchDetails>() {
                    @Override
                    public void onResponse(Call<FetchDetails> call, Response<FetchDetails> response) {
                        if(response.code()==200){
                            FetchDetails result = response.body();
                            name.setText(result.getpName());
                            ipname=(TextView)(findViewById(R.id.ipname));
                            ipname.setText(result.getpName());
                            loc =(TextView)(findViewById(R.id.s1));
                            loc.setText(result.getLoc());
                            iloc=(TextView)(findViewById(R.id.iloc));
                            iloc.setText(result.getLoc());
                            slots=(TextView)(findViewById(R.id.s2));
                            slots.setText(result.getSlots());
                            mno =(TextView)(findViewById(R.id.s3));
                            mno.setText(result.getMno());
                            price =(TextView)(findViewById(R.id.price));
                            price.setText(result.getPrice());
                            coordinates[0]=result.getCoordinate1();
                            coordinates[1]=result.getCoordinate2();
                        }else if(response.code()==404){
                            Toast.makeText(PlaceActivity.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchDetails> call, Throwable t) {
                        Toast.makeText(PlaceActivity.this, t.getMessage(),
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
                handleEvent();
//                Intent i = new Intent(PlaceActivity.this,par_map.class);
//                startActivity(i);
            }
        });
    }
    private  void handleEvent(){
        View view = getLayoutInflater().inflate(R.layout.book_dialog, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(view).show();
        Button pBtn = view.findViewById(R.id.book);
         et = (EditText)(view.findViewById(R.id.hours));
        pBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                book();
            }
        });
    }
    private void book(){
        Thread t1 = new Thread(){
            @Override
            public void run() {
                HashMap<String,String> map2 = new HashMap<>();

                map2.put("id",s.id);
                map2.put("hours",et.getText().toString());
                map2.put("pname",n);
                Call<Void> call = retrofitInterface.executeBook(map2);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()==200){
                            Toast.makeText(PlaceActivity.this,
                                    "You have booked successfully", Toast.LENGTH_LONG).show();
                            Intent home = new Intent(PlaceActivity.this,par_map.class);
                            startActivity(home);
                        }
                    }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Toast.makeText(PlaceActivity.this, t.getMessage(),
                            Toast.LENGTH_LONG).show();
                }
            });
            }
        };
        t1.start();
    }

}
