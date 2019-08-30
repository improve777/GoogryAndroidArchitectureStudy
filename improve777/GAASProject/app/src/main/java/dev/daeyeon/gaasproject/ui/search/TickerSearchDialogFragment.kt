package dev.daeyeon.gaasproject.ui.search

import android.os.Bundle
import android.view.View
import dev.daeyeon.gaasproject.R
import dev.daeyeon.gaasproject.base.BaseDialogFragment
import dev.daeyeon.gaasproject.databinding.DialogFragmentTickerSearchBinding
import dev.daeyeon.gaasproject.ui.main.MainViewModel
import dev.daeyeon.gaasproject.util.EventObserver
import org.koin.androidx.viewmodel.ext.android.sharedViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class TickerSearchDialogFragment : BaseDialogFragment<DialogFragmentTickerSearchBinding>(
    R.layout.dialog_fragment_ticker_search
) {
    private val mainViewModel: MainViewModel by sharedViewModel()

    private val searchViewModel: TickerSearchViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bind {
            viewModel = mainViewModel
            searchViewModel = this@TickerSearchDialogFragment.searchViewModel
            lifecycleOwner = this@TickerSearchDialogFragment.viewLifecycleOwner
        }

        subscribeCompleteEvent()
    }

    /**
     * 완료 이벤트 구독
     */
    private fun subscribeCompleteEvent() {
        searchViewModel.completeEvent.observe(viewLifecycleOwner, EventObserver {
            this@TickerSearchDialogFragment.dismiss()
        })
    }

    companion object {
        fun newInstance() = TickerSearchDialogFragment()
    }
}