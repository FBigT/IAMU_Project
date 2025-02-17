package good.stuff.myapplication.adapter

import android.graphics.Color
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import good.stuff.myapplication.R
import good.stuff.myapplication.model.GameItem
import com.bumptech.glide.Glide
import good.stuff.myapplication.GamePagerActivity
import good.stuff.myapplication.model.PlatformType
import android.content.Context
import good.stuff.myapplication.GAME_ID
import good.stuff.myapplication.framework.startActivity

class GameAdapter(private val context: Context,
                  private val games: List<GameItem>) :
    RecyclerView.Adapter<GameAdapter.GameViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GameViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_game, parent, false)
        return GameViewHolder(view)
    }

    override fun onBindViewHolder(holder: GameViewHolder, position: Int) {
        val game = games[position]
        holder.bind(game)

        holder.itemView.setOnClickListener {
            context.startActivity<GamePagerActivity>(GAME_ID, game.id)
        }
    }

    override fun getItemCount() = games.size

    inner class GameViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivGameCover: ImageView = itemView.findViewById(R.id.ivGameCover)
        private val tvGameTitle: TextView = itemView.findViewById(R.id.tvGameTitle)
        private val tvRating: TextView = itemView.findViewById(R.id.tvRating)
        private val tvGenres: TextView = itemView.findViewById(R.id.tvGenres)
        private val tvReleaseDate: TextView = itemView.findViewById(R.id.tvReleaseDate)
        private val platformIconsContainer: ViewGroup =
            itemView.findViewById(R.id.platformIconsContainer)

        fun bind(game: GameItem) {
            tvGameTitle.text = game.title
            tvRating.text = game.rating?.toInt()?.toString() ?: "N/A"
            tvGenres.text = game.genres.joinToString(", ")
            tvReleaseDate.text = game.releaseDate

            Glide.with(itemView.context)
                .load(game.coverImageUrl)
                .placeholder(R.drawable.debug_image)
                .into(ivGameCover)

            platformIconsContainer.removeAllViews()

            val uniquePlatforms = HashSet<String>()

            game.platforms.forEach { platform ->
                if (uniquePlatforms.add(platform)) {
                    val iconRes = PlatformType.fromString(platform).iconResId
                    val platformIcon = ImageView(itemView.context).apply {
                        layoutParams = ViewGroup.LayoutParams(48, 48).apply {
                            val margin = TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, 4f, itemView.context.resources.displayMetrics
                            ).toInt()
                        }
                        setImageResource(iconRes)
                        setColorFilter(Color.WHITE)
                    }
                    platformIconsContainer.addView(platformIcon)
                }
            }
        }

    }
}
