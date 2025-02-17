package good.stuff.myapplication.api

import android.content.Context
import android.util.Log
import androidx.work.Worker
import androidx.work.WorkerParameters
import good.stuff.myapplication.handler.GameDataHolder
import java.util.concurrent.CountDownLatch

class IGDBWorkerDetailed(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {

    override fun doWork(): Result {
        val gameId = inputData.getInt("gameId", -1)
        if (gameId == -1) {
            return Result.failure()
        }

        val query = """
            fields 
            name, 
            cover.image_id, 
            involved_companies.company.name, 
            involved_companies.developer, 
            involved_companies.publisher, 
            first_release_date, 
            genres.name, rating, 
            rating_count, 
            aggregated_rating, 
            aggregated_rating_count, 
            follows, 
            summary; 
            where id = $gameId;
        """.trimIndent().replace("\n", " ")

        val fetcher = IGDBFetcher(context)
        val latch = CountDownLatch(1)

        var result: Result = Result.failure()

        fetcher.fetchGameDetails(query) { game ->
            if (game != null) {
                GameDataHolder.currentGame = game
                Log.d("IGDBWorkerDetailed", "Game details fetched and saved: ${game.title}")
                result = Result.success()
            } else {
                Log.e("IGDBWorkerDetailed", "Failed to fetch game details.")
                result = Result.failure()
            }
            latch.countDown()
        }

        try {
            latch.await()
        } catch (e: InterruptedException) {
            Log.e("IGDBWorkerDetailed", "Work interrupted", e)
            return Result.failure()
        }

        return result
    }
}
