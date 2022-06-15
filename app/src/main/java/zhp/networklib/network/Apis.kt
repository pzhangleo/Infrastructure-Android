package zhp.networklib.network

import retrofit2.http.GET
import zhp.network.lib.adapter.NetworkResponse

interface Apis {

    @GET("https://jsonplaceholder.typicode.com/posts/1")
    suspend fun getPost1(): NetworkResponse<Post, ErrorResponse>

}