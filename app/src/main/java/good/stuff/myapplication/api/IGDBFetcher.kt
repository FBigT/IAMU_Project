package good.stuff.myapplication.api

import android.content.Context
import android.util.Log
import good.stuff.myapplication.api.dao.GameItemDetailedSerialized
import good.stuff.myapplication.api.dao.GameItemSerialized
import good.stuff.myapplication.handler.GameDataHolder
import good.stuff.myapplication.model.GameItem
import good.stuff.myapplication.model.GameItemDetailed
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IGDBFetcher(private val context: Context) {

    fun fetchGames(query: String) {
        val requestBody = RequestBody.create(MediaType.get("text/plain; charset=utf-8"), query)
        val request = RetrofitClient.api.getGames(requestBody)

        request.enqueue(object : Callback<List<GameItemSerialized>> {
            override fun onResponse(call: Call<List<GameItemSerialized>>, response: Response<List<GameItemSerialized>>) {
                Log.d("IGDBFetcher", "Raw Response: ${response.body().toString()}")
                response.body()?.let { saveGames(it) }
            }

            override fun onFailure(call: Call<List<GameItemSerialized>>, t: Throwable) {
                Log.e("IGDBFetcher", "Failed to fetch games: ${t.message}")
            }
        })
    }

    fun fetchGameDetails(query: String, callback: (GameItemDetailed?) -> Unit) {
        val requestBody = RequestBody.create(MediaType.get("text/plain; charset=utf-8"), query)
        val request = RetrofitClient.api.getDetails(requestBody)

        request.enqueue(object : Callback<List<GameItemDetailedSerialized>> {
            override fun onResponse(
                call: Call<List<GameItemDetailedSerialized>>,
                response: Response<List<GameItemDetailedSerialized>>
            ) {
                Log.d("IGDBFetcher", "Raw Response: ${response.body()?.toString()}")

                response.body()?.let { serializedGames ->
                    val gameSerialized = serializedGames.firstOrNull()

                    if (gameSerialized != null) {
                        val gameDetailed = gameSerialized.toGameItemDetailed()
                        callback(gameDetailed)
                    } else {
                        callback(null)
                    }
                } ?: run {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<List<GameItemDetailedSerialized>>, t: Throwable) {
                Log.e("IGDBFetcher", "Failed to fetch game details: ${t.message}")
                callback(null)
            }
        })
    }


    fun GameItemDetailedSerialized.toGameItemDetailed(): GameItemDetailed {
        return GameItemDetailed(
            id = this.id,
            title = this.title,
            coverImageUrl = this.coverImageUrl,
            releaseDate = this.formattedReleaseDate,
            genres = this.genreNames,
            rating = this.rating,
            ratingCritic = this.ratingCount?.toDouble(),
            followers = this.followers,
            reviewsCount = this.reviews,
            summary = this.summary,
            developer = this.developers,
            publisher = this.publishers
        )
    }


    private fun saveGames(gameItems: List<GameItemSerialized>) {
        CoroutineScope(Dispatchers.IO).launch {
            val mappedGames = gameItems.map { serialized ->
                GameItem(
                    id = serialized.id,
                    title = serialized.title,
                    coverImageUrl = serialized.coverImageUrl,
                    platforms = serialized.platformNames,
                    releaseDate = serialized.formattedReleaseDate,
                    genres = serialized.genreNames,
                    rating = serialized.rating
                )
            }

            GameDataHolder.games = mappedGames
            Log.d("IGDBFetcher", "Games successfully saved: ${mappedGames.size}")
        }
    }
}
