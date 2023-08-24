package com.affinityclick.mvvm.movie.credits

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.affinityclick.mvvm.R
import com.affinityclick.mvvm.network.FetchResource
import com.affinityclick.mvvm.network.models.Credits
import javax.inject.Inject

class MovieCreditsFragment : Fragment() {

  @JvmField @Inject
  var viewModelFactory: ViewModelProvider.Factory? = null
  private lateinit var viewModel: MovieCreditsViewModel

  override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
    val root = inflater.inflate(R.layout.credits_fragment, container, false)
    viewModel = ViewModelProvider(this, viewModelFactory!!).get(MovieCreditsViewModel::class.java)

    val castRecyclerView = root.findViewById<RecyclerView>(R.id.castRecyclerView)
    val crewRecyclerView = root.findViewById<RecyclerView>(R.id.crewRecyclerView)

    castRecyclerView.layoutManager = LinearLayoutManager(context)
    crewRecyclerView.layoutManager = LinearLayoutManager(context)

    val castAdapter = CastAdapter()
    val crewAdapter = CrewAdapter()
    castRecyclerView.adapter = castAdapter
    crewRecyclerView.adapter = crewAdapter

    viewModel.movieCreditsLiveData.observe(viewLifecycleOwner, { response ->
      if (response is FetchResource.Success) {
        response.value.cast?.let { castAdapter.setData(it) }
        response.value.crew?.let { crewAdapter.setData(it) }
      }
    })

    val movieId = arguments?.getInt("MOVIE_ID") ?: -1
    viewModel.getMovieCredits(movieId)

    return root
  }
}
