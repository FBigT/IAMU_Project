package good.stuff.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import good.stuff.myapplication.R
import good.stuff.myapplication.model.GameItem

class FavoritesAdapter(
    private val games: List<GameItem>,
    private val context: Context
) : RecyclerView.Adapter<FavoritesAdapter.GameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_favorite_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.title.text = game.title
        Glide.with(context).load(game.coverImageUrl).into(holder.coverImage)
    }

    override fun getItemCount() = games.size

    class GameViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView = view.findViewById(R.id.gameTitle)
        val coverImage: ImageView = view.findViewById(R.id.gameCover)
    }
}
