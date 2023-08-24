package com.affinityclick.mvvm.movie.credits

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.affinityclick.mvvm.network.FetchResource
import com.affinityclick.mvvm.network.TMDBRepository
import com.affinityclick.mvvm.network.models.Credits
import javax.inject.Inject

class MovieCreditsViewModel @Inject constructor(private val tmdbRepository: TMDBRepository) : ViewModel() {
  private val getMovieCreditsTrigger = MutableLiveData<Int>()
  val movieCreditsLiveData: LiveData<FetchResource<Credits>> =
    Transformations.switchMap(getMovieCreditsTrigger) { id -> tmdbRepository.getMovieCredits(id) }

  fun getMovieCredits(id: Int) {
    getMovieCreditsTrigger.value = id
  }
}
