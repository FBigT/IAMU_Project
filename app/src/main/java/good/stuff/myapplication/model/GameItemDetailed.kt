package good.stuff.myapplication.model

data class GameItemDetailed(
    val id: Int,
    val title: String,
    val coverImageUrl: String,
    val releaseDate: String,
    val genres: List<String>,
    val rating: Double?,
    val ratingCritic: Double?,
    val followers: Int?,
    val reviewsCount: Int?,
    val summary: String?,
    val developer: List<String>,
    val publisher: List<String>
)

fun GameItemDetailed.toGameItem(): GameItem {
    return GameItem(
        id = this.id,
        title = this.title,
        coverImageUrl = this.coverImageUrl,
        platforms = listOf("N/A"),
        releaseDate = this.releaseDate,
        genres = this.genres,
        rating = this.rating
    )
}
