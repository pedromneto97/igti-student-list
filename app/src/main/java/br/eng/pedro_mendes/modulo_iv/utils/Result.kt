package br.eng.pedro_mendes.modulo_iv.utils

data class Result<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T?): Result<T> {
            return Result(Status.Success, data, null)
        }

        fun <T> error(msg: String, data: T?): Result<T> {
            return Result(Status.Error, data, msg)
        }

        fun <T> loading(data: T?): Result<T> {
            return Result(Status.Loading, data, null)
        }
    }
}
