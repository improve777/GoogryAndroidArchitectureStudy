package dev.daeyeon.gaasproject.ui.main

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.tabs.TabLayoutMediator
import dev.daeyeon.gaasproject.R
import dev.daeyeon.gaasproject.databinding.ActivityMainBinding
import dev.daeyeon.gaasproject.ui.search.TickerSearchDialogFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.inflate(layoutInflater, R.layout.activity_main, null, false)
        setContentView(binding.root)

        binding.run {
            vm = viewModel
            lifecycleOwner = this@MainActivity

            vpMain.apply {
                adapter = MainPagerAdapter(supportFragmentManager, lifecycle)
                offscreenPageLimit = 1
            }

            TabLayoutMediator(tlMain, vpMain, true) { tab, position ->
                tab.text = (vpMain.adapter as? MainPagerAdapter)?.getPageTitle(position) ?: ""
                vpMain.setCurrentItem(tab.position, true)
            }.attach()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        MenuInflater(this@MainActivity).inflate(R.menu.menu_ticker_fragment, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // 검색
            R.id.action_search -> {
                showTickerSearchDialog()
                return true
            }
        }

        return false
    }

    /**
     * ticker 검색 다이얼로그
     */
    private fun showTickerSearchDialog() {
        TickerSearchDialogFragment.newInstance().show(supportFragmentManager, null)
    }
}
