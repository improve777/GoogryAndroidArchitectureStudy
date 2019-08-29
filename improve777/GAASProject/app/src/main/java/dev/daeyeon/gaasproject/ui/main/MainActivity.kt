package dev.daeyeon.gaasproject.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import dev.daeyeon.gaasproject.R
import dev.daeyeon.gaasproject.databinding.ActivityMainBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_main, null, false)
        setContentView(binding.root)

        binding.run {
            vpMain.adapter = MainPagerAdapter(supportFragmentManager, lifecycle)
            TabLayoutMediator(tlMain, vpMain, true) { _, _ -> }.attach()
        }

/*        supportFragmentManager.commit {
            replace(
                R.id.container,
                supportFragmentManager.findFragmentByTag(TickerFragment.TAG) ?: TickerFragment.newInstance(),
                TickerFragment.TAG
            )
        }*/
    }
}
