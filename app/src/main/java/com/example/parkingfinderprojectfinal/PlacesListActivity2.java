package com.example.parkingfinderprojectfinal;
//For food list
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.PlaceFoodAdapter;

import com.model.Place;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.parkingfinderprojectfinal.foodActivity.ARG_PLACE_NAME;

public class PlacesListActivity2 extends AppCompatActivity implements PlaceFoodAdapter.OnPlaceFoodClickListener{
    private RelativeLayout mBack;
    private RecyclerView mRecyclerView;
    private TextView mTitle;

    public static String[] name;
    public static String[] locs;
    public static String ARG_CATEGORY_NAME = "ARG_CATEGORY_NAME";


    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = MainActivity.u;

    private Context mContext = PlacesListActivity2.this;

    private static final String TAG = "placeslistactivity2";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list2);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        initialize();
        HashMap<String, String> map = new HashMap<>();
        MainActivity m = new MainActivity();
        map.put("id", m.id);

        Thread t2 = new Thread(){
            @Override
            public void run() {
                Call<FetchFood> call = retrofitInterface.executeFood(map);
                call.enqueue(new Callback<FetchFood>() {
                    @Override
                    public void onResponse(Call<FetchFood> call, Response<FetchFood> response) {
                        if(response.code()==200){
                            FetchFood result =response.body();
                            name=result.getObjs();
                            locs=result.getLoc();
                            setupWidgets();
                        }else if(response.code()==400){

                        }
                    }

                    @Override
                    public void onFailure(Call<FetchFood> call, Throwable t) {
                        Toast.makeText(PlacesListActivity2.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });
            }
        };
        t2.start();
    }
    private void initialize() {
        mBack = findViewById(R.id.back);
        mRecyclerView = findViewById(R.id.recycler_view);
        mTitle = findViewById(R.id.toolbar_title);
    }
    private void setupWidgets() {
        //change status bar color to transparent
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.headerColor));
        }

        mBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //setup product recycler view
//        GridLayoutManager llmProduct = new GridLayoutManager(this, 2);
        LinearLayoutManager llmPlace = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(llmPlace);
        PlaceFoodAdapter mPlaceFoodAdapter = new PlaceFoodAdapter(this);
        mRecyclerView.setAdapter(mPlaceFoodAdapter);
    }


    @Override
    public void onPlaceFoodClickListener(Place place) {
        Intent i = new Intent(PlacesListActivity2.this,foodActivity.class);
        i.putExtra(ARG_PLACE_NAME, place.getPlaceName());
        startActivity(i);
    }

    @Override
    public void onPlaceFoodFavoriteClick(Place place) {

    }
}
