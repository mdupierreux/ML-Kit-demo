package be.maximedupierreux.mlkitdemo.android

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navController = findNavController(R.id.mainNavFragment)
        setupActionBarWithNavController(navController)
        bottomNavigationView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp() = findNavController(R.id.mainNavFragment).navigateUp()


}
