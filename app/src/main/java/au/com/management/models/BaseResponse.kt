package au.com.management.models

class BaseResponse<T> {
    var success: Boolean = false
    var message: String? = null
    var data: T? = null
}