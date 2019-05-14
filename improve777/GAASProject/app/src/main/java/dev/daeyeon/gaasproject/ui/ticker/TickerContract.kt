package dev.daeyeon.gaasproject.ui.ticker

import dev.daeyeon.gaasproject.data.Ticker

interface TickerContract {
    interface View {
        var presenter: Presenter

        fun showProgress()

        fun hideProgress()

        /**
         * ticker 에러 메시지 처리
         * @param msg
         */
        fun toastTickerFailMsg(msg: String)

        /**
         * tickerList 아이템 초기화
         * @param tickerList
         */
        fun replaceTickerList(tickerList: List<Ticker>)
    }

    interface Presenter {

        fun start()

        /**
         * 뷰가 제거되면 api 중지
         */
        fun cancelApi()

        fun loadUpbitTicker()
    }
}