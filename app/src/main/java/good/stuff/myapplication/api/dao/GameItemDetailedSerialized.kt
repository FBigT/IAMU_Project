package good.stuff.myapplication.api.dao

import com.google.gson.annotations.SerializedName

data class GameItemDetailedSerialized(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val title: String,

    @SerializedName("cover") val cover: CoverSerialized?,
    @SerializedName("first_release_date") val releaseDate: Long?,
    @SerializedName("genres") val genres: List<GenreSerialized>?,

    @SerializedName("rating") val rating: Double?,
    @SerializedName("rating_count") val ratingCount: Int?,
    @SerializedName("followers") val followers: Int?,
    @SerializedName("reviews") val reviews: Int?,
    @SerializedName("summary") val summary: String?,

    @SerializedName("involved_companies") val involvedCompanies: List<InvolvedCompanySerialized>?
) {
    val coverImageUrl: String
        get() = cover?.url ?: ""

    val formattedReleaseDate: String
        get() = releaseDate?.let {
            java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Date(it * 1000))
        } ?: "Unknown"

    val genreNames: List<String>
        get() = genres?.map { it.name } ?: emptyList()

    val formattedRating: String
        get() = rating?.let { "%.1f".format(it) } ?: "N/A"

    val developers: List<String>
        get() = involvedCompanies?.filter { it.isDeveloper }?.map { it.company.name } ?: emptyList()

    val publishers: List<String>
        get() = involvedCompanies?.filter { it.isPublisher }?.map { it.company.name } ?: emptyList()
}