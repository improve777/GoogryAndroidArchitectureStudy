package dev.daeyeon.gaasproject.data

/**
 * A generic class that holds a value with its loading status.
 * @param <T>
 */
sealed class NetResult<out R> {

    data class Success<out T>(val data: T) : NetResult<T>()
    data class Error(val exception: Exception) : NetResult<Nothing>()
    object Loading : NetResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }

    companion object {
        fun <T> success(data: T) = Success(data)

        fun error(e: Exception) = Error(e)

        fun loading() = Loading
    }
}