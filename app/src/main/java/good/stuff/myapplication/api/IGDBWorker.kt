package good.stuff.myapplication.api

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class IGDBWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val query = (inputData.getString("query") ?: """
            fields name, total_rating, total_rating_count, cover.url, first_release_date, platforms.name, genres.name, summary;
            sort total_rating desc;
            where total_rating > 70 & total_rating_count > 50 & first_release_date != null;
            limit 20;
        """.trimIndent()).replace("\n", " ")

        IGDBFetcher(context).fetchGames(query)
        return Result.success()
    }
}
