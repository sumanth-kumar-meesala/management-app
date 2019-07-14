package au.com.management.retrofit

import okhttp3.Interceptor
import okhttp3.Response

class TokenHttpInterceptor : Interceptor {

    var token: String? = null

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()

        // Request customization: add request headers
        val requestBuilder = original.newBuilder()

        if (token != null) {
            requestBuilder.addHeader("x-access-token", "$token")
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}