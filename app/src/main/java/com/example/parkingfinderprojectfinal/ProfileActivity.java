package com.example.parkingfinderprojectfinal;
import androidx.appcompat.app.AppCompatActivity;
//import com.example.parkingfinderprojectfinal.MainActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import com.Utils.BottomNavigationViewHelper;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import java.util.HashMap;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ProfileActivity extends AppCompatActivity {
    TextView uname,email;
    private Retrofit retrofit;
    private RetrofitInterface retrofitInterface;
    private String BASE_URL = MainActivity.u;
    private static final int ACTIVITY_NUM = 3;
    private Context mContext =ProfileActivity.this;
    private static final String TAG = "profileactivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        retrofitInterface = retrofit.create(RetrofitInterface.class);
        MainActivity m = new MainActivity();
        HashMap<String, String> map = new HashMap<>();
        map.put("id", m.id);
        map.put("token",MainActivity.token);

        Thread t1 = new Thread(){
            @Override
            public void run() {
                Call<FetchResult> call = retrofitInterface.executeFetch(map);

                call.enqueue(new Callback<FetchResult>() {
                    @Override
                    public void onResponse(Call<FetchResult> call, Response<FetchResult> response) {
                        if (response.code() == 200) {
                            FetchResult result = response.body();
                            uname=(TextView)findViewById(R.id.search_text);
                            uname.setText(result.getName());
                            email=(TextView)findViewById(R.id.profile_email);

                            Handler handler = new Handler();
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    email.setText(result.getEmail());
                                }
                            });
                        } else if (response.code() == 404) {
                            Toast.makeText(ProfileActivity.this, "Wrong Credentials",
                                    Toast.LENGTH_LONG).show();
                        }
                    }
                    @Override
                    public void onFailure(Call<FetchResult> call, Throwable t) {
                        Toast.makeText(ProfileActivity.this, t.getMessage(),
                                Toast.LENGTH_LONG).show();
                    }
                });

            }
        };
         t1.start();
        setupBottomNavigationView();
    }
    private void setupBottomNavigationView(){
        BottomNavigationViewEx bottomNavigationViewEx = findViewById(R.id.bottomNavViewBar);
        BottomNavigationViewHelper.setupBottomNavigationView(bottomNavigationViewEx);
        BottomNavigationViewHelper.enableNavigation(mContext, this, bottomNavigationViewEx);
        Menu menu = bottomNavigationViewEx.getMenu();
        MenuItem menuItem = menu.getItem(ACTIVITY_NUM);
        menuItem.setChecked(true);
    }
}
