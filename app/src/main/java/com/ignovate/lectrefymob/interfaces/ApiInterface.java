package com.ignovate.lectrefymob.interfaces;




import com.ignovate.lectrefymob.model.Forget;
import com.ignovate.lectrefymob.model.GenderModel;
import com.ignovate.lectrefymob.model.LoginReqModel;
import com.ignovate.lectrefymob.model.LoginSuccess;
import com.ignovate.lectrefymob.model.Otp;
import com.ignovate.lectrefymob.model.RegisterReqModel;
import com.ignovate.lectrefymob.model.RoleModel;
import com.ignovate.lectrefymob.model.Success;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @Headers({"Content-Type: application/json"})
    @POST("lectrefy-api-service/api/v1.0/users")
    Call<RegisterReqModel> createUser(@Body RegisterReqModel model);


    @Headers({"Content-Type: application/json"})
    @GET("lectrefy-api-service/api/v1.0/codedatasets/")
    Call<List<GenderModel>> getSpinnerData();

    @Headers({"Content-Type: application/json"})
    @GET("lectrefy-api-service/api/v1.0/users")
    Call<ArrayList<RegisterReqModel>> getAllUser();


    @Headers({"Content-Type: application/json"})
    @GET("lectrefy-api-service/api/v1.0/users/{id}")
    Call<RegisterReqModel> getSingleUser(@Path("id") String id);

    @Headers({"Content-Type: application/json"})
    @GET("lectrefy-api-service/api/v1.0/roles")
    Call<ArrayList<RoleModel>> getRole();

    @Headers({"Content-Type: application/json"})
    @PUT("lectrefy-api-service/api/v1.0/users/{id}")
    Call<Success> getUserRoleUpdate(@Path("id") String id, @Body RegisterReqModel list);

    @Headers({"Content-Type: application/json"})
    @PUT("lectrefy-api-service/api/v1.0/users/{id}")
    Call<Success> getUserUpdate(@Path("id") String id);

    @Headers({"Content-Type: application/json"})
    @POST("lectrefy-api-service/api/v1.0/login")
    Call<LoginSuccess> loginUser(@Body LoginReqModel model);

    @Headers({"Content-Type: application/json"})
    @GET("lectrefy-api-service/api/v1.0/users/filter?")
    Call<List<RegisterReqModel>> roleUsers(@Query("email") String email);


    @Headers({"Content-Type: application/json"})
    @GET("lectrefy-api-service/api/v1.0/users/filter?")
    Call<List<RegisterReqModel>> phoneVerify(@Query("phone") String ph);

    @Headers({"Content-Type: application/json"})
    @GET("lectrefy-api-service/api/v1.0/users/filter?")
    Call<List<RegisterReqModel>> forgetUser(@Query("phone") String ph,@Query("email") String em);

    @Headers({"Content-Type: application/json"})
    @POST("lectrefy-api-service/api/v1.0/otp/generate")
    Call<Success> otpGenerate(@Body Forget fm);

    @Headers({"Content-Type: application/json"})
    @POST("lectrefy-api-service/api/v1.0/otp/validate")
    Call<Success> validateOTP(@Body Otp otp);


}