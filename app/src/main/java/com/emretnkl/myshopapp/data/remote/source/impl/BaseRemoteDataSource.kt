package com.emretnkl.myshopapp.data.remote.source.impl
import com.emretnkl.myshopapp.data.model.ApiError
import com.emretnkl.myshopapp.data.remote.utils.DataState
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import retrofit2.Response

open class BaseRemoteDataSource {
    suspend fun <T> getResult(call: suspend () -> Response<T>): Flow<DataState<T>> {
        return flow<DataState<T>> {
            val response = call()

            if (response.isSuccessful) {
                val body = response.body()

                if (body != null) emit(DataState.Success(body))
                else {
                    val apiError : ApiError = Gson().fromJson(response.errorBody()?.charStream(), ApiError::class.java)
                    emit(DataState.Error(apiError))
                }
            } else {
                val apiError: ApiError = Gson().fromJson(response.errorBody()?.charStream(), ApiError::class.java)
                emit(DataState.Error(apiError))
            }
        }.catch { e ->
            emit(DataState.Error(ApiError(-1,e.message ?: "Unknown Error")))
        }.onStart { emit(DataState.Loading()) }
            .flowOn(Dispatchers.IO)
    }
}