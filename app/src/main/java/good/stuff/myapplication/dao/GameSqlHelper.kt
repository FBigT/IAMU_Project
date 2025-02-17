package good.stuff.myapplication.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import good.stuff.myapplication.model.GameItem

private const val DB_NAME = "games.db"
private const val DB_VERSION = 1
private const val TABLE_NAME = "games"

// Create table query
private val CREATE_TABLE = "CREATE TABLE $TABLE_NAME( " +
        "${GameItem::id.name} INTEGER PRIMARY KEY AUTOINCREMENT, " +
        "${GameItem::title.name} TEXT NOT NULL, " +
        "${GameItem::coverImageUrl.name} TEXT NOT NULL, " +
        "${GameItem::platforms.name} TEXT NOT NULL, " +  // Store as CSV
        "${GameItem::releaseDate.name} TEXT NOT NULL, " +
        "${GameItem::genres.name} TEXT NOT NULL, " +  // Store as CSV
        "${GameItem::rating.name} REAL" +
        ")"

private const val DROP_TABLE = "DROP TABLE IF EXISTS $TABLE_NAME"

class GameSqlHelper(context: Context?) : SQLiteOpenHelper(
    context,
    DB_NAME,
    null,
    DB_VERSION
), GameRepository {

    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(CREATE_TABLE)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL(DROP_TABLE)
        onCreate(db)
    }

    override fun delete(selection: String?, selectionArgs: Array<String>?): Int {
        return writableDatabase.delete(TABLE_NAME, selection, selectionArgs)
    }

    override fun insert(values: ContentValues?): Long {
        return writableDatabase.insert(TABLE_NAME, null, values)
    }

    override fun query(
        projection: Array<String>?,
        selection: String?,
        selectionArgs: Array<String>?,
        sortOrder: String?
    ): Cursor {
        return readableDatabase.query(TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder)
    }

    override fun update(values: ContentValues?, selection: String?, selectionArgs: Array<String>?): Int {
        return writableDatabase.update(TABLE_NAME, values, selection, selectionArgs)
    }

    fun insertGame(game: GameItem): Long {
        val values = ContentValues().apply {
            put(GameItem::title.name, game.title)
            put(GameItem::coverImageUrl.name, game.coverImageUrl)
            put(GameItem::platforms.name, game.platforms.joinToString(","))  // Convert List to String
            put(GameItem::releaseDate.name, game.releaseDate)
            put(GameItem::genres.name, game.genres.joinToString(","))  // Convert List to String
            put(GameItem::rating.name, game.rating)
        }
        return insert(values)
    }

    fun queryGameById(id: Int): GameItem? {
        val cursor = query(
            null,
            "${GameItem::id.name} = ?",
            arrayOf(id.toString()),
            null
        )
        cursor.use {
            if (it.moveToFirst()) {
                return cursorToGameItem(it)
            }
        }
        return null
    }

    fun queryAllGames(): List<GameItem> {
        val gameList = mutableListOf<GameItem>()
        val cursor = query(null, null, null, null)
        cursor.use {
            while (it.moveToNext()) {
                gameList.add(cursorToGameItem(it))
            }
        }
        return gameList
    }

    fun updateGame(game: GameItem): Int {
        val values = ContentValues().apply {
            put(GameItem::title.name, game.title)
            put(GameItem::coverImageUrl.name, game.coverImageUrl)
            put(GameItem::platforms.name, game.platforms.joinToString(","))  // Convert List to String
            put(GameItem::releaseDate.name, game.releaseDate)
            put(GameItem::genres.name, game.genres.joinToString(","))  // Convert List to String
            put(GameItem::rating.name, game.rating)
        }
        return update(values, "${GameItem::id.name} = ?", arrayOf(game.id.toString()))
    }

    fun deleteGame(id: Int): Int {
        return delete("${GameItem::id.name} = ?", arrayOf(id.toString()))
    }

    private fun cursorToGameItem(cursor: Cursor): GameItem {
        return GameItem(
            id = cursor.getInt(cursor.getColumnIndexOrThrow(GameItem::id.name)),
            title = cursor.getString(cursor.getColumnIndexOrThrow(GameItem::title.name)),
            coverImageUrl = cursor.getString(cursor.getColumnIndexOrThrow(GameItem::coverImageUrl.name)),
            platforms = cursor.getString(cursor.getColumnIndexOrThrow(GameItem::platforms.name)).split(","),
            releaseDate = cursor.getString(cursor.getColumnIndexOrThrow(GameItem::releaseDate.name)),
            genres = cursor.getString(cursor.getColumnIndexOrThrow(GameItem::genres.name)).split(","),
            rating = if (!cursor.isNull(cursor.getColumnIndexOrThrow(GameItem::rating.name))) {
                cursor.getDouble(cursor.getColumnIndexOrThrow(GameItem::rating.name))
            } else null
        )
    }

    fun addGameToFavorites(game: GameItem): Long {
        return insertGame(game)
    }

    fun getFavoriteGames(): Cursor {
        return query(null, null, null, null)
    }
}
