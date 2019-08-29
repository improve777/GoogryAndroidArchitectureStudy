package dev.daeyeon.gaasproject.data.entity

import android.os.Parcel
import android.os.Parcelable

data class Market(
    val currency: String,
    val searchWord: String
) : Parcelable {
    constructor(source: Parcel) : this(
        source.readString(),
        source.readString()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeString(currency)
        writeString(searchWord)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Market> = object : Parcelable.Creator<Market> {
            override fun createFromParcel(source: Parcel): Market = Market(source)
            override fun newArray(size: Int): Array<Market?> = arrayOfNulls(size)
        }
    }
}