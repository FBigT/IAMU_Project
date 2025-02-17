package good.stuff.myapplication

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import androidx.work.workDataOf
import good.stuff.myapplication.adapter.GamePagerAdapter
import good.stuff.myapplication.api.IGDBWorkerDetailed
import good.stuff.myapplication.databinding.ActivityGamePagerBinding
import good.stuff.myapplication.handler.GameDataHolder
import good.stuff.myapplication.model.GameItemDetailed

const val GAME_ID = "GAME_ID"

class GamePagerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityGamePagerBinding
    private lateinit var game: GameItemDetailed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGamePagerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val gameId = intent.getIntExtra(GAME_ID, -1)

        if (gameId != -1) {
            fetchGameDetails(gameId)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        GameDataHolder.resetCurrentGame()
    }

    private fun fetchGameDetails(gameId: Int) {
        // Show the loading spinner
        binding.progressBar.visibility = View.VISIBLE
        binding.viewGamePager.visibility = View.GONE  // Hide the game pager initially

        val inputData = workDataOf("gameId" to gameId)

        // Create the worker request
        val gameDetailsRequest = OneTimeWorkRequest.Builder(IGDBWorkerDetailed::class.java)
            .setInputData(inputData)
            .build()

        // Observe the work status to get the result
        WorkManager.getInstance(this)
            .getWorkInfoByIdLiveData(gameDetailsRequest.id)
            .observe(this, Observer { workInfo ->
                workInfo?.let { info ->
                    when (info.state) {
                        WorkInfo.State.SUCCEEDED -> {
                            // When the work is successful, get the data from GameDataHolder
                            GameDataHolder.currentGame?.let { updatedGame ->
                                game = updatedGame

                                // Update the UI with the fetched game data
                                binding.viewGamePager.adapter = GamePagerAdapter(this, updatedGame)

                                // Hide the loading spinner and show the game pager
                                binding.progressBar.visibility = View.GONE
                                binding.viewGamePager.visibility = View.VISIBLE
                            }
                        }

                        WorkInfo.State.FAILED -> {
                            // Handle failure
                            Log.e("GamePagerActivity", "Failed to fetch game details.")

                            // Hide the loading spinner and show an error message (you can add a TextView or Toast here)
                            binding.progressBar.visibility = View.GONE
                        }

                        else -> {
                            // You can log or handle other states (e.g., RUNNING, ENQUEUED, etc.)
                        }
                    }
                }
            })

        // Enqueue the work request after setting up the observer
        WorkManager.getInstance(this).enqueue(gameDetailsRequest)
    }
}