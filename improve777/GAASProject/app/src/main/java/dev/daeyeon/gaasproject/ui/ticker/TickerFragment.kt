package dev.daeyeon.gaasproject.ui.ticker

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.core.os.bundleOf
import androidx.lifecycle.Observer
import dev.daeyeon.gaasproject.R
import dev.daeyeon.gaasproject.base.BaseFragment
import dev.daeyeon.gaasproject.data.entity.Market
import dev.daeyeon.gaasproject.data.remote.response.ResponseCode
import dev.daeyeon.gaasproject.databinding.FragmentTickerBinding
import dev.daeyeon.gaasproject.ext.popContent
import dev.daeyeon.gaasproject.ui.ticker.search.TickerSearchDialogFragment
import dev.daeyeon.gaasproject.util.Event
import org.jetbrains.anko.toast
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class TickerFragment : BaseFragment<FragmentTickerBinding>(
    R.layout.fragment_ticker
) {
    private val tickerViewModel: TickerViewModel by viewModel { parametersOf(markets) }

    private lateinit var markets: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        markets = arguments?.getParcelable<Market>(EXTRA_MARKET)?.searchWord ?: ""
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        bind {
            rvTicker.adapter = TickerAdapter()
            viewModel = tickerViewModel
            lifecycleOwner = this@TickerFragment.viewLifecycleOwner
        }

        setHasOptionsMenu(true)
        swipeInit()

        subscribeToFailMsg()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.menu_ticker_fragment, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            // 검색
            R.id.action_search -> {
                showTickerSearchDialog()
            }
        }

        return false
    }

    /**
     * ticker 검색 다이얼로그
     */
    private fun showTickerSearchDialog() {
        TickerSearchDialogFragment.newInstance().show(childFragmentManager, null)
    }

    /**
     * swipeRefreshLayout 버튼 색, 이벤트 설정
     */
    private fun swipeInit() {
        binding.srlTicker.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_green_light,
            android.R.color.holo_orange_light,
            android.R.color.holo_red_light
        )
    }

    /**
     * failMsgEvent 구독
     */
    private fun subscribeToFailMsg() {
        tickerViewModel.failMsgEvent.observe(this, Observer<Event<String>> { event ->
            event.popContent {
                toastTickerFailMsg(it)
            }
        })
    }

    private fun toastTickerFailMsg(msg: String) {
        when (msg) {
            ResponseCode.CODE_NULL_SUCCESS,
            ResponseCode.CODE_NULL_FAIL_MSG -> activity!!.toast(R.string.all_network_error)
            ResponseCode.CODE_EMPTY_SUCCESS -> activity!!.toast(R.string.ticker_fragment_empty)
            else -> activity!!.toast(msg)
        }
    }

    companion object {
        private const val EXTRA_MARKET = "EXTRA_MARKET"

        fun newInstance(market: Market): TickerFragment = TickerFragment().apply {
            arguments = bundleOf(EXTRA_MARKET to market)
        }
    }
}
