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
import com.adapter.ClientListAdapter;
import com.adapter.OrderAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.model.ClientList;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class clientList extends AppCompatActivity implements ClientListAdapter.OnClientClickListener {
    private Retrofit retrofit;
    public static  String[] ClientNames;
    public static String[] ClientHrs;
    public static String[] ClientNos;
    public static String[] ClientPrices;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = MainActivity.u;
    private static final int ACTIVITY_NUM = 2;
    RecyclerView mClientRecylcer;
    private Context mContext =clientList.this;
    private ClientListAdapter mClientAdapter;
    private static final String TAG = "clientList";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_list);

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
                Call<FetchClients> call =retrofitInterface.executeAllClients(map);
                call.enqueue(new Callback<FetchClients>() {
                    @Override
                    public void onResponse(Call<FetchClients> call, Response<FetchClients> response) {
                        if(response.code()==200){
                            FetchClients result = response.body();
                            ClientNames=result.getCname();
                            ClientHrs=result.getChrs();
                            ClientNos=result.getCno();
                            ClientPrices=result.getCprice();
                            setupWidgets();
                        }
                    }

                    @Override
                    public void onFailure(Call<FetchClients> call, Throwable t) {
                        Toast.makeText(clientList.this, t.getMessage(),
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
        mClientRecylcer=findViewById(R.id.order_recycler_view);
    }

    private void setupWidgets(){

        LinearLayoutManager llmPlace = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mClientRecylcer.setLayoutManager(llmPlace);
        mClientAdapter = new ClientListAdapter(this,ClientNames);
        mClientRecylcer.setAdapter(mClientAdapter);
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
    public void onClientClickListener(ClientList c) {
        Intent i = new Intent(clientList.this, ClientView.class);
        i.putExtra(ClientView.ARG_CLIENT_NAME, c.getClientName());
        startActivity(i);
    }

    @Override
    public void onClientFavoriteClick(ClientList c) {

    }
}
