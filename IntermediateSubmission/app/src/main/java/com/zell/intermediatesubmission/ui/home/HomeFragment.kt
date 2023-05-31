package com.zell.intermediatesubmission.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zell.intermediatesubmission.databinding.FragmentHomeBinding
import com.zell.intermediatesubmission.helper.bearerGenerate
import com.zell.intermediatesubmission.helper.isLoading
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by viewModels()
    private val adapter: ListStoryAdapter by lazy {
        ListStoryAdapter { storyId ->
            val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(storyId)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        binding.rvStory.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStory.adapter = adapter
        adapter.withLoadStateFooter(
            footer = LoadingStateAdapter {
                adapter.retry()
            }
        )


        binding.btnProfile.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToProfileFragment())
        }

        binding.btnAddPhoto.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToUploadFragment())
        }

        binding.btnMaps.setOnClickListener {
            findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToMapsFragment())
        }

        setListStory()
    }

    private fun setListStory() {
        isLoading(true, binding.progressBar)
        homeViewModel.getToken("token").observe(viewLifecycleOwner) {
            val token = bearerGenerate(it)
            homeViewModel.getStories(token).observe(viewLifecycleOwner) { response ->
                adapter.submitData(lifecycle, response)
                isLoading(false, binding.progressBar)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        setListStory()
        binding.rvStory.smoothScrollToPosition(0)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}