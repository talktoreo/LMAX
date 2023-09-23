package com.test.lmaxtest.views.splashPage

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.lifecycleScope
import com.test.lmaxtest.R
import com.test.lmaxtest.databinding.ActivityDetailBinding
import com.test.lmaxtest.databinding.ActivitySplashBinding
import com.test.lmaxtest.utils.goToNextPage
import com.test.lmaxtest.utils.isNetworkAvailable
import com.test.lmaxtest.utils.showNetworkErrorSnackbar
import com.test.lmaxtest.views.mainPage.MainActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (isNetworkAvailable(this)){
            lifecycleScope.launch {
                delay(500)
                goToNextPage(this@SplashActivity, MainActivity::class.java, true, true, null)
            }
        } else {
            showNetworkErrorSnackbar(binding.rootLay, this) {
                goToNextPage(this@SplashActivity, SplashActivity::class.java, true, true, null)
            }
        }
    }
}