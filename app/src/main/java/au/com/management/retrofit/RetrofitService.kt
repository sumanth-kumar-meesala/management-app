package au.com.management.retrofit

import au.com.management.models.*
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService {
    @POST("/user/register")
    fun register(@Body body: RegisterRequest): Call<BaseResponse<Any>>

    @POST("/user/login")
    fun login(@Body body: RegisterRequest): Call<BaseResponse<LoginResponse>>

    @POST("/question/add")
    fun addQuestion(@Body body: QuestionRequest): Call<BaseResponse<Any>>

    @POST("/question/list")
    fun listQuestion(): Call<BaseResponse<List<QuestionRequest>>>

    @POST("/question/completed")
    fun completed(@Body body: QuestionRequest): Call<BaseResponse<Any>>

    @POST("/user/addUser")
    fun addUser(@Body body: RegisterRequest): Call<BaseResponse<Any>>

    @POST("/user/listUser")
    fun listUsers(): Call<BaseResponse<List<User>>>

    @POST("/user/updatePassword")
    fun updatePassword(@Body body: UpdatePasswordRequest): Call<BaseResponse<Any>>
}