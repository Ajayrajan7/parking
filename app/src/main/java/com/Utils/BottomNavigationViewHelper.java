package com.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.parkingfinderprojectfinal.MainActivity;
import com.example.parkingfinderprojectfinal.ProfileActivity;
import com.example.parkingfinderprojectfinal.R;
import com.example.parkingfinderprojectfinal.clientList;
import com.example.parkingfinderprojectfinal.home;
import com.example.parkingfinderprojectfinal.map;
import com.example.parkingfinderprojectfinal.order;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;

public class BottomNavigationViewHelper {
    private static final String TAG = "BottomNavigationViewHel";
    public static void setupBottomNavigationView(BottomNavigationViewEx bottomNavigationViewEx){
        Log.d(TAG, "setupBottomNavigationView: Setting up BottomNavigationView");
        bottomNavigationViewEx.enableAnimation(false);
        bottomNavigationViewEx.enableItemShiftingMode(true);
        bottomNavigationViewEx.enableShiftingMode(false);
        bottomNavigationViewEx.setTextSize(10);
    }

    public static void enableNavigation(final Context context, final Activity callingActivity, BottomNavigationViewEx view){
        view.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.action_home:
                        Intent intent1 = new Intent(context, home.class); //ACTIVITY_NUM = 0
                        context.startActivity(intent1);
                        callingActivity.overridePendingTransition(R.anim.fade, R.anim.fade);
                        break;

                    case R.id.action_discover:
                        Intent intent2 = new Intent(context, map.class); //ACTIVITY_NUM = 1
                        context.startActivity(intent2);
                        callingActivity.overridePendingTransition(R.anim.fade, R.anim.fade);

//                        Toast.makeText(context, "Discover", Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.action_favorites:
                        if((MainActivity.token).equals("user")) {
                            Intent intent3 = new Intent(context, order.class); //ACTIVITY_NUM = 2
                            context.startActivity(intent3);
                            callingActivity.overridePendingTransition(R.anim.fade, R.anim.fade);
                            break;
                        }else{
                            Intent intent3 = new Intent(context, clientList.class); //ACTIVITY_NUM = 2
                            context.startActivity(intent3);
                            callingActivity.overridePendingTransition(R.anim.fade, R.anim.fade);
                            break;
                        }
                    case R.id.action_profile:
                        Intent profileIntent = new Intent(context, ProfileActivity.class); //ACTIVITY_NUM = 3
                        context.startActivity(profileIntent);
                        callingActivity.overridePendingTransition(R.anim.fade, R.anim.fade);
                        break;
                }
                return false;
            }

        });
    }
}
