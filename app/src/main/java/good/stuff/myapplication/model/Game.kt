package good.stuff.myapplication.model

data class Game(
    val id: Int,
    val name: String,
    val summary: String?,
    val cover: Cover?
)
