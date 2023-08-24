package com.affinityclick.mvvm.network

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.liveData
import androidx.viewbinding.BuildConfig
import com.affinityclick.mvvm.network.models.Credits
import com.affinityclick.mvvm.network.models.Movie
import com.affinityclick.mvvm.network.models.PageResult
import com.affinityclick.mvvm.util.CoroutineDispatchersProvider
import java.io.IOException
import java.util.Properties
import javax.inject.Inject

/**
 * Repository responsible for making all web calls to the movie database.
 */
class TMDBRepository @Inject constructor(
  private val coroutineDispatcher: CoroutineDispatchersProvider,
  private val tmdbApi: TMDBApi
) {

  fun getMovieCredits(movieId: Int): LiveData<FetchResource<Credits>> {
    val data = MediatorLiveData<FetchResource<Credits>>()

    data.value = FetchResource.Loading() //notify that loading has started

    // Getting the API Key from the properties file
    val properties = Properties().apply {
      val inputStream = this::class.java.classLoader?.getResourceAsStream("key.properties")
      load(inputStream)
    }
    val apiKey = properties["tmdbApiKey"] as String

    apiService.getMovieCredits(movieId, apiKey).enqueue(object : Callback<Credits> {
      override fun onResponse(call: Call<Credits>, response: Response<Credits>) {
        if (response.isSuccessful) {
          data.value = FetchResource.Success(response.body()!!)
        } else {
          data.value = FetchResource.Error(response.message()) // Handle error responses
        }
      }

      override fun onFailure(call: Call<Credits>, t: Throwable) {
        data.value = FetchResource.Error(t.message ?: "Unknown Error") // Handle failures like no internet
      }
    })

    return data
  }

  /**
   * @param page Page of top rated movies to show.
   * @return Value to be observed on to return the result
   */
  fun getTopRatedMovies(page: Int): LiveData<FetchResource<PageResult<Movie?>>> = liveData {
    emit(FetchResource.Loading)
    emit(apiCall(call = {tmdbApi.getTopRatedMovies(page, BuildConfig.TMDB_API_KEY)}))
  }

  /**
   *
   * @param id Movie id to lookup
   * @return A live data to be observed on for the results of the call to get the movie
   */
  fun getMovie(id: Int): LiveData<FetchResource<Movie>> = liveData {
    emit(FetchResource.Loading)
    emit(apiCall(call = {tmdbApi.getMovie(id, BuildConfig.TMDB_API_KEY)}))
  }

  /**
   * This method wraps api calls to provide some generic handling and converts the response to a
   * FetchResource wrapper
   *
   * @Param call A suspendable api call that is invoked and wrapped in some generic error handling
   *
   * @return A wrapper around the calls return saying whether it was a Success or Error
   */
  private suspend fun <T : Any> apiCall(call: suspend  () -> Response<T>): FetchResource<T> {
    try {
      val response = call.invoke()
      if (response.isSuccessful) {
        response.body()?.let {
          return FetchResource.Success(it)
        }
      }
    } catch (e: IOException) {
      e.printStackTrace()
    }
    //TODO: return some error context
    return FetchResource.Error
  }
}