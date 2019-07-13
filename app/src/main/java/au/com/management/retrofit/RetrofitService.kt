package au.com.management.retrofit

import au.com.management.models.BaseResponse
import au.com.management.models.LoginResponse
import au.com.management.models.RegisterRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService {
    @POST("/user/register")
    fun register(@Body body: RegisterRequest): Call<BaseResponse<Any>>

    @POST("/user/login")
    fun login(@Body body: RegisterRequest): Call<BaseResponse<LoginResponse>>
}