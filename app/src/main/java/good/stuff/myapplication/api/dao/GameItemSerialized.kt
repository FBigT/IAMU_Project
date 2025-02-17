package good.stuff.myapplication.api.dao

import com.google.gson.annotations.SerializedName

data class GameItemSerialized(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val title: String,

    @SerializedName("cover")
    val cover: CoverSerialized?,

    @SerializedName("platforms")
    val platforms: List<PlatformSerialized>?,

    @SerializedName("first_release_date")
    val releaseDate: Long?,

    @SerializedName("genres")
    val genres: List<GenreSerialized>?,

    @SerializedName("total_rating")
    val rating: Double?
){
    val coverImageUrl: String
        get() = cover?.url?.replace("//", "https://") ?: ""

    val formattedReleaseDate: String
        get() = releaseDate?.let { java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Date(it * 1000)) } ?: "Unknown"

    val genreNames: List<String>
        get() = genres?.map { it.name } ?: emptyList()

    val platformNames: List<String>
        get() = platforms?.map { it.name } ?: emptyList()
}