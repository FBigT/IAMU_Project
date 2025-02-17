package good.stuff.myapplication.api

import good.stuff.myapplication.api.dao.GameItemDetailedSerialized
import good.stuff.myapplication.api.dao.GameItemSerialized
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface IGDBService {
    @Headers(
        "Client-ID: rwcnv31cyhjcykdc48f5fzy77dh7g5",
        "Authorization: Bearer 5d724w6p6iu7b5z9je414imkdm5w0m",
        "Accept: application/json"
    )
    @POST("games")
    fun getGames(@Body query: RequestBody): Call<List<GameItemSerialized>>
    @Headers(
        "Client-ID: rwcnv31cyhjcykdc48f5fzy77dh7g5",
        "Authorization: Bearer 5d724w6p6iu7b5z9je414imkdm5w0m",
        "Accept: application/json"
    )
    @POST("games")
    fun getDetails(@Body query: RequestBody): Call<List<GameItemDetailedSerialized>>
}