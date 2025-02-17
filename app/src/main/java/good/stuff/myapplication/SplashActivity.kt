package good.stuff.myapplication

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import good.stuff.myapplication.api.IGDBWorker
import good.stuff.myapplication.databinding.ActivitySplashBinding
import good.stuff.myapplication.framework.AnimSequence
import good.stuff.myapplication.framework.isOnline

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        startAnimation()
        redirect()
    }

    private fun startAnimation() {
        AnimSequence()
            .append(binding.ivRod, "translationY", 190f, 20f, 1300, OvershootInterpolator())
            .append(binding.ivRod, "scaleY", 0.5f, 1f, 600)
            .append(binding.ivRod, "alpha", 0f, 1f, 400)
            .append(binding.ivBase, "translationY", -5f, 0f, 1200)
            .append(binding.ivBase, "scaleY", 0.9f, 1f, 750)
            .append(binding.ivBase, "alpha", 0f, 1f, 300)
            .append(binding.tvAppName, "alpha", 0f, 1f, 500, 200)
            .append(binding.dividerLine,"alpha", 0f, 1f, 500, 200)
            .append(binding.dividerLine,"scaleX", 0f, 1f, 500, 200, DecelerateInterpolator())
            .play()

        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }, 3000)
    }

    private fun redirect() {
        if (isOnline()) {
            WorkManager.getInstance(this).apply {
                enqueueUniqueWork(
                    "unique_name",
                    ExistingWorkPolicy.KEEP,
                    OneTimeWorkRequest.from(IGDBWorker::class.java)
                )
            }
        } else {
            showNoInternetDialog()
        }
    }

    private fun showNoInternetDialog() {
    }
}
