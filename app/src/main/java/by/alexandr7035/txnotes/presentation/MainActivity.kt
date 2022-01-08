package by.alexandr7035.txnotes.presentation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import by.alexandr7035.txnotes.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}