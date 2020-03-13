package com.example.parkingfinderprojectfinal;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.Utils.BottomNavigationViewHelper;
import com.adapter.OrderAdapter;
import com.adapter.PlaceAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.mapbox.mapboxsdk.offline.OfflineRegionDefinition;
import com.model.Order;
import com.model.Place;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import  static  com.example.parkingfinderprojectfinal.OrderView.ARG_PLACE_NAME;
public class order extends AppCompatActivity implements OrderAdapter.OnOrderClickListener {
    private Retrofit retrofit;
    public static  String[] orderNames;
    public static String[] orderHrs;
    public static String[] orderLocs;
    public static String[] orderPrices;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = MainActivity.u;
    private static final int ACTIVITY_NUM = 2;
    RecyclerView mOrderRecylcer;
    private Context mContext =order.this;
    private OrderAdapter mOrderAdapter;
    private static final String TAG = "order";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        HashMap<String, String> map = new HashMap<>();
        MainActivity m = new MainActivity();
        map.put("id", m.id);

        Thread t1 = new Thread(){
            @Override
            public void run() {
                Call<Fetchorder> call = retrofitInterface.executeAllBook(map);
                call.enqueue(new Callback<Fetchorder>() {
                    @Override
                    public void onResponse(Call<Fetchorder> call, Response<Fetchorder> response) {
                        if(response.code()==200){
                            Fetchorder result = response.body();
                            orderNames=result.getName();
                            orderHrs= result.getHrs();
                            orderLocs = result.getLoc();
                            orderPrices = result.getPrice();
                            setupWidgets();
                        }else if(response.code()==400){

                        }
                    }

                    @Override
                    public void onFailure(Call<Fetchorder> call, Throwable t) {
                        Toast.makeText(order.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        };
        t1.start();


        setupBottomNavigationView();
        initComponents();

    }

    private void initComponents(){
        mOrderRecylcer=findViewById(R.id.order_recycler_view);
    }

    private void setupWidgets(){

        LinearLayoutManager llmPlace = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mOrderRecylcer.setLayoutManager(llmPlace);
        mOrderAdapter = new OrderAdapter(this,orderNames);
        mOrderRecylcer.setAdapter(mOrderAdapter);
    }

    //    BottomNavigationView setup
    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }

    @Override
    public void onOrderClickListener(Order order) {
        Intent i = new Intent(order.this, OrderView.class);
        i.putExtra(OrderView.ARG_PLACE_NAME, order.getPlaceName());
        startActivity(i);
    }

    @Override
    public void onOrderFavoriteClick(Order order) {

    }
}