package good.stuff.myapplication.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import good.stuff.myapplication.R
import good.stuff.myapplication.dao.GameSqlHelper
import good.stuff.myapplication.model.GameItem
import good.stuff.myapplication.model.GameItemDetailed
import good.stuff.myapplication.model.toGameItem

class GamePagerAdapter(
    private val context: Context,
    private val game: GameItemDetailed // Single detailed game item
) : RecyclerView.Adapter<GamePagerAdapter.GameViewHolder>() {

    class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Views in the layout
        private val ivCover: ImageView = itemView.findViewById(R.id.ivGameCover)
        private val tvTitle: TextView = itemView.findViewById(R.id.tvGameTitle)
        private val tvReleaseDate: TextView = itemView.findViewById(R.id.tvReleaseDate)
        private val tvDeveloper: TextView = itemView.findViewById(R.id.tvDeveloper)
        private val tvPublisher: TextView = itemView.findViewById(R.id.tvPublisher)
        private val tvDescription: TextView = itemView.findViewById(R.id.tvGameDescription)
        private val tvGenres: TextView = itemView.findViewById(R.id.tvGenres)
        private val tvUserScore: TextView = itemView.findViewById(R.id.tvUserScore)
        private val tvCriticScore: TextView = itemView.findViewById(R.id.tvCriticScore)
        private val tvFollows: TextView = itemView.findViewById(R.id.tvFollows)
        private val tvReviewCount: TextView = itemView.findViewById(R.id.tvReviewCount)
        private val btnAddToFavorites: ImageButton = itemView.findViewById(R.id.btnAddToFavorites)

        fun bind(game: GameItemDetailed, dbHelper: GameSqlHelper) {
            // Set the data from GameItemDetailed to views
            Glide.with(itemView.context)
                .load(game.coverImageUrl)
                .placeholder(R.drawable.debug_image)
                .error(R.drawable.debug_image)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(ivCover)

            tvTitle.text = game.title
            tvReleaseDate.text = "Release Date: ${game.releaseDate}"
            tvDeveloper.text = "Developer: ${game.developer}"
            tvPublisher.text = "Publisher: ${game.publisher}"
            tvDescription.text = game.summary
            tvGenres.text = game.genres.joinToString(", ")
            tvUserScore.text = "User Score: ${game.rating ?: "N/A"}"
            tvCriticScore.text = "Critic Score: ${game.ratingCritic ?: "N/A"}"
            tvFollows.text = "Followers: ${game.followers}K"
            tvReviewCount.text = "Reviews: ${game.reviewsCount}"

            btnAddToFavorites.setOnClickListener {
                val dbHelper = GameSqlHelper(itemView.context) // Initialize database helper
                val result = dbHelper.addGameToFavorites(game.toGameItem()) // Convert to GameItem
                if (result != -1L) {
                    Toast.makeText(itemView.context, "Added to Favorites!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(itemView.context, "Failed to add to Favorites!", Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_game_pager, parent, false)
        return GameViewHolder(view)
    }

    override fun getItemCount(): Int = 1

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val dbHelper = GameSqlHelper(holder.itemView.context)
        holder.bind(game, dbHelper)
    }
}
