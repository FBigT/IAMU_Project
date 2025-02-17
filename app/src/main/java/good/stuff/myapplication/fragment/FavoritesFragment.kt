package good.stuff.myapplication.fragment

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import good.stuff.myapplication.adapter.FavoritesAdapter
import good.stuff.myapplication.dao.GameSqlHelper
import good.stuff.myapplication.databinding.FragmentFavoritesBinding
import good.stuff.myapplication.model.GameItem

class FavoritesFragment : Fragment() {
    private lateinit var binding: FragmentFavoritesBinding
    private lateinit var dbHelper: GameSqlHelper
    private lateinit var adapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbHelper = GameSqlHelper(requireContext())
        adapter = FavoritesAdapter(loadFavoriteGames(), requireContext())

        binding.rvFavorites.layoutManager = LinearLayoutManager(requireContext())
        binding.rvFavorites.adapter = adapter
    }

    private fun loadFavoriteGames(): List<GameItem> {
        val cursor: Cursor = dbHelper.getFavoriteGames()
        val games = mutableListOf<GameItem>()

        if (cursor.moveToFirst()) {
            do {
                val game = GameItem(
                    id = cursor.getInt(cursor.getColumnIndexOrThrow("id")),
                    title = cursor.getString(cursor.getColumnIndexOrThrow("title")),
                    coverImageUrl = cursor.getString(cursor.getColumnIndexOrThrow("coverImageUrl")),
                    platforms = cursor.getString(cursor.getColumnIndexOrThrow("platforms")).split(","),
                    releaseDate = cursor.getString(cursor.getColumnIndexOrThrow("releaseDate")),
                    genres = cursor.getString(cursor.getColumnIndexOrThrow("genres")).split(","),
                    rating = cursor.getDouble(cursor.getColumnIndexOrThrow("rating"))
                )
                games.add(game)
            } while (cursor.moveToNext())
        }
        cursor.close()
        return games
    }
}
