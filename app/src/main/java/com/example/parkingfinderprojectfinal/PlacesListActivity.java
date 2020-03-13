package com.example.parkingfinderprojectfinal;
//workshop list activity
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.style.ReplacementSpan;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.adapter.PlaceAdapter;
import com.adapter.PlaceWSAdapter;
import com.model.Place;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.example.parkingfinderprojectfinal.wsActivity.ARG_PLACE_NAME;

public class PlacesListActivity extends AppCompatActivity implements PlaceWSAdapter.OnPlaceWSClickListener{
    private RelativeLayout mBack;
    private RecyclerView mRecyclerView;
    private TextView mTitle;
    public static String[] name;
    public static String[] locs;
    public static String ARG_CATEGORY_NAME = "ARG_CATEGORY_NAME";


    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = MainActivity.u;

    private Context mContext = PlacesListActivity.this;

    private static final String TAG = "placeslistactivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_places_list);
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
                Call<FetchWs> call =retrofitInterface.executeWs(map);
                call.enqueue(new Callback<FetchWs>() {
                    @Override
                    public void onResponse(Call<FetchWs> call, Response<FetchWs> response) {
                        if(response.code()==200){
                            FetchWs result = response.body();
                            name=result.getObjs();
                            locs=result.getLoc();
                            setupWidgets();
                        }else if(response.code()==400){

                        }
                    }

                    @Override
                    public void onFailure(Call<FetchWs> call, Throwable t) {
                        Toast.makeText(PlacesListActivity.this, t.getMessage(),
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
        PlaceWSAdapter mPlaceWSAdapter = new PlaceWSAdapter(this);
        mRecyclerView.setAdapter(mPlaceWSAdapter);
    }

    @Override
    public void onPlaceWSClickListener(Place place) {
        Intent i = new Intent(PlacesListActivity.this,wsActivity.class);
        i.putExtra(ARG_PLACE_NAME, place.getPlaceName());
        startActivity(i);
    }

    @Override
    public void onPlaceWSFavoriteClick(Place place) {

    }
}
