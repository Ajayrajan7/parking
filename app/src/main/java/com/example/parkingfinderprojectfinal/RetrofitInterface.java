package com.example.parkingfinderprojectfinal;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface RetrofitInterface {

    @POST("/login")
    Call<LoginResult> executeLogin(@Body HashMap<String, String> map);

    @POST("/par_login")
    Call<LoginparResult> executeparLogin(@Body HashMap<String,String> map);

    @POST("/signup")
    Call<Void> executeSignup (@Body HashMap<String, String> map);

    @POST("/par_signup")
    Call<Void> executeParSignup(@Body HashMap<String,String> map);

    @POST("/fetch")
    Call<FetchResult> executeFetch(@Body HashMap<String,String> map);

    @POST("/getAllPar")
    Call<Fetchpar> executeFetchpar(@Body HashMap<String,String> map);

    @POST("/getAllLocs")
    Call<LocFetch> executegetAllLocs(@Body HashMap<String,String> map);

    @POST("/getADetails")
    Call<FetchDetails> executegetADetails(@Body HashMap<String,String> map);

    @POST("/bookAParking")
    Call<Void> executeBook(@Body HashMap<String,String> map);

    @POST("/getAllBook")
    Call<Fetchorder> executeAllBook(@Body HashMap<String,String>map);

    @POST("/getAllClients")
    Call<FetchClients> executeAllClients(@Body HashMap<String,String> map);

    @POST("/getparorderdet")
    Call<Fetchorderdetails> executeparorder(@Body HashMap<String,String> map);

    @POST("/getClientDetails")
    Call<FetchClientDetails> executeparclient(@Body HashMap<String,String>map);

    @POST("/cancelbook")
    Call<Void> executecancel(@Body HashMap<String,String> map);

    @POST("/force_cancel_book")
    Call<Void> executeclientcancel(@Body HashMap<String,String>map);

    @POST("/ws_signup")
    Call<Void> executeWSSignup(@Body HashMap<String,String> map);

    @POST("/hos_signup")
    Call<Void> executehosSignUp(@Body HashMap<String,String> map);

    @POST("/food_signup")
    Call<Void> executefoodSignUp(@Body HashMap<String,String> map);

    @POST("/fetchWs")
    Call<FetchWs> executeWs(@Body HashMap<String,String>map);

    @POST("/fetchFood")
    Call<FetchFood> executeFood(@Body HashMap<String,String>map);

    @POST("/fetchHos")
    Call<FetchHos> executeHos(@Body HashMap<String,String>map);

    @POST("/fetchWSDetail")
    Call<FetchWSDetails> executeWSDetail(@Body HashMap<String,String>map);


    @POST("/fetchFoodDetail")
    Call<FetchFoodDetails> executeFoodDetail(@Body HashMap<String,String>map);


    @POST("/fetchHosDetail")
    Call<FetchHosDetail> executeHosDetail(@Body HashMap<String,String>map);
}
