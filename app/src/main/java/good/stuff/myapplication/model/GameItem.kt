package good.stuff.myapplication.model

data class GameItem(
    val id: Int,
    val title: String,
    val coverImageUrl: String,
    val platforms: List<String>,
    val releaseDate: String,
    val genres: List<String>,
    val rating: Double?
)

