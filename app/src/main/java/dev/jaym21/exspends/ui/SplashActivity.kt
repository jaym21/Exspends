package dev.jaym21.exspends.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.asLiveData
import dev.jaym21.exspends.data.datastore.DataStoreManager

class SplashActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DataStoreManager(this).isFirstStartUp.asLiveData().observe(this) { isFirstStartUp ->
            Log.d("TAGYOYO", "SplashActivity: $isFirstStartUp")
        }
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}