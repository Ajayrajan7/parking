package com.example.parkingfinderprojectfinal;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.adapter.PlaceAdapter;
import com.example.parkingfinderprojectfinal.R;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.Utils.BottomNavigationViewHelper;
import com.model.Category;

import com.adapter.HomeCategoriesAdapter;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.model.Place;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import  static  com.example.parkingfinderprojectfinal.PlaceActivity.ARG_PLACE_NAME;
import  static  com.example.parkingfinderprojectfinal.PlacesListActivity.ARG_CATEGORY_NAME;
public class home extends AppCompatActivity implements  HomeCategoriesAdapter.OnCategoryClickListener,
        PlaceAdapter.OnPlaceClickListener{
    public static  String[] placeNames;
    public static String[] p;
    public static String[] price;
    private RecyclerView mCategoryRecycler,mPlaceRecycler;
    private TextView mCategoriesList,mPlaceList;
    private HomeCategoriesAdapter mCategoriesAdapter;
    private PlaceAdapter mPlaceAdapter;


    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = MainActivity.u;

    private static final int ACTIVITY_NUM = 0;
    private Context mContext = home.this;

    private static final String TAG = "homeactivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);

        HashMap<String, String> map = new HashMap<>();
        MainActivity m = new MainActivity();
        map.put("id", m.id);

        Thread t2 = new Thread(){
            @Override
            public void run() {
                Call<Fetchpar> call = retrofitInterface.executeFetchpar(map);
                call.enqueue(new Callback<Fetchpar>() {
                    @Override
                    public void onResponse(Call<Fetchpar> call, Response<Fetchpar> response) {
                        if(response.code()==200){
                            Fetchpar result = response.body();
//                    AlertDialog.Builder builder1 = new AlertDialog.Builder(home.this);
//                    builder1.setTitle("tf");
                             p=result.getObjs();
                             placeNames = result.getLoc();
                             price = result.getPrice();

//                    builder1.setMessage(b[0]);
//                            p=new String[b.length];
//                            placeNames=new String[l.length];
//                            price= new String[c.length];
//                            for(int i=0;i<b.length;i++){
//                                p[i]=b[i];
//                                placeNames[i]=l[i];
////                                price[i]=c[i];
//                            }
//                    builder1.show();
                            setupWidgets();
                        }else if(response.code()==400){

                        }
                    }

                    @Override
                    public void onFailure(Call<Fetchpar> call, Throwable t) {
                        Toast.makeText(home.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();

                    }
                });

            }
        };
        t2.start();

        initComponents();
//        setupWidgets();
        setupBottomNavigationView();

    }

    private void initComponents() {
        mCategoryRecycler = findViewById(R.id.trending_recycler_view);
        mPlaceRecycler = findViewById(R.id.place_recycler_view);
        mPlaceList = findViewById(R.id.place_list);
        mCategoriesList = findViewById(R.id.categories_list);
    }

    private void setupWidgets() {

        LinearLayoutManager llmTrending = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mCategoryRecycler.setLayoutManager(llmTrending);
        mCategoriesAdapter = new HomeCategoriesAdapter(this);
        mCategoryRecycler.setAdapter(mCategoriesAdapter);


        LinearLayoutManager llmPlace = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mPlaceRecycler.setLayoutManager(llmPlace);
        mPlaceAdapter = new PlaceAdapter(this,placeNames);
        mPlaceRecycler.setAdapter(mPlaceAdapter);

        //view all parking places
        mPlaceList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(HomeActivity.this, PlacesListActivity.class));
            }
        });

        ///View all text in categories list
        mCategoriesList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(home.this, CategoriesListActivity.class));
            }
        });
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

    //Clicking on a particular category
    @Override
    public void onCategoryClickListener(Category category) {
        if((category.getCategoryName()).equals("Workshops")){
            Intent i = new Intent(home.this, PlacesListActivity.class);
        i.putExtra(ARG_CATEGORY_NAME, category.getCategoryName());
        startActivity(i);
        }
        if((category.getCategoryName()).equals("Food")){
            Intent i = new Intent(home.this, PlacesListActivity2.class);
            i.putExtra(ARG_CATEGORY_NAME, category.getCategoryName());
            startActivity(i);
        }
        if((category.getCategoryName()).equals("Hospital")){
            Intent i = new Intent(home.this, PlacesListActivity3.class);
            i.putExtra(ARG_CATEGORY_NAME, category.getCategoryName());
            startActivity(i);
        }
//
    }

//    clicking on a particular parking place
    @Override
    public void onPlaceClickListener(Place place) {
        Intent i = new Intent(home.this, PlaceActivity.class);
        i.putExtra(ARG_PLACE_NAME, place.getPlaceName());
        startActivity(i);
    }

    @Override
    public void onPlaceFavoriteClick(Place place) {
//        mPlaceAdapter.setFavorite(place.getPlaceId());
//        mPlaceAdapter.notifyDataSetChanged();
    }

}
