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

public class OrderView extends AppCompatActivity {
    public static String ARG_PLACE_NAME = "ARG_CATEGORY_NAME";

    TextView name,loc,slots,mno,ipname,iloc,price;
    Button book,cancel;
    RelativeLayout mBack;
    private String n;
    MainActivity s = new MainActivity();
    EditText et;
    String id;
    public static String[] coordinates= new String[2];
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = MainActivity.u;
    private Context mContext =OrderView.this;
    private static final String TAG = "orderview";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_view);
        name =  (TextView)(findViewById(R.id.s));
        Intent i = getIntent();
        n = i.getStringExtra(ARG_PLACE_NAME);
        mBack = findViewById(R.id.back);
        book=findViewById(R.id.book);
        cancel=findViewById(R.id.cancel);
        MainActivity m = new MainActivity();
        id=m.id;
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        PlaceActivity b = new PlaceActivity();
        Thread t1 =new Thread(){
            @Override
            public void run() {
                HashMap<String,String> hp = new HashMap<>();
                hp.put("pname",n);
                hp.put("id",id);
                Call<Fetchorderdetails> call = retrofitInterface.executeparorder(hp);
                call.enqueue(new Callback<Fetchorderdetails>() {
                    @Override
                    public void onResponse(Call<Fetchorderdetails> call, Response<Fetchorderdetails> response) {
                        if(response.code()==200){
                            Fetchorderdetails result = response.body();
                            name.setText(result.getpName());
                            ipname=(TextView)(findViewById(R.id.ipname));
                            ipname.setText(result.getpName());
                            loc =(TextView)(findViewById(R.id.s1));
                            loc.setText(result.getLoc());
                            iloc=(TextView)(findViewById(R.id.iloc));
                            iloc.setText(result.getLoc());
                            slots=(TextView)(findViewById(R.id.s2));
                            slots.setText(result.getHours());
                            mno =(TextView)(findViewById(R.id.s3));
                            mno.setText(result.getMno());
                            price =(TextView)(findViewById(R.id.price));
                            price.setText(result.getPrice());
                            coordinates[0]=result.getCoordinate1();
                            coordinates[1]=result.getCoordinate2();
                            b.coordinates=coordinates;
                        }else if(response.code()==404){
                            Toast.makeText(OrderView.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Fetchorderdetails> call, Throwable t) {
                        Toast.makeText(OrderView.this, t.getMessage(),
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

                Intent i = new Intent(OrderView.this,par_map.class);
                startActivity(i);
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                make_cancel();
            }
        });

    }
    private  void make_cancel(){
        Thread t2 = new Thread(){
            @Override
            public void run() {
                HashMap<String,String> map2 = new HashMap<>();
                map2.put("pname",n);
                map2.put("id",id);
                Call<Void> call = retrofitInterface.executecancel(map2);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if(response.code()==200){
                            Toast.makeText(OrderView.this,"Your booking has been cancelled successfully",
                                    Toast.LENGTH_LONG).show();
                            Intent i = new Intent(OrderView.this,order.class);
                            startActivity(i);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        Toast.makeText(OrderView.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        t2.start();
    }

}
