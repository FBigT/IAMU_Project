package good.stuff.myapplication.handler

import good.stuff.myapplication.model.GameItem
import good.stuff.myapplication.model.GameItemDetailed

object GameDataHolder {
    var games: List<GameItem> = emptyList()
    var currentGame: GameItemDetailed? = null

    fun resetCurrentGame() {
        currentGame = null
    }
}
