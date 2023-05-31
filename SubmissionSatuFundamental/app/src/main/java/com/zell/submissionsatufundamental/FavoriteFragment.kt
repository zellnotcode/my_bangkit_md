package com.zell.submissionsatufundamental

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.zell.submissionsatufundamental.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding
    private lateinit var favoriteEditViewModel: FavoriteEditViewModel
    private val adapter: ListFavoriteAdapter by lazy {
        ListFavoriteAdapter { username ->
            val action = FavoriteFragmentDirections.actionFavoriteFragmentToDetailFragment(username)
            findNavController().navigate(action)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteEditViewModel = obtainViewModel()

        val layoutManager = LinearLayoutManager(requireContext())
        binding?.rvListFavorite?.layoutManager = layoutManager

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.homeFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        setListFavorite()
    }

    private fun setListFavorite() {
        favoriteEditViewModel.getAllFavorite().observe(viewLifecycleOwner) { listFavorite ->
            adapter.setData(listFavorite)
            binding?.rvListFavorite?.adapter = adapter
        }
    }

    private fun obtainViewModel(): FavoriteEditViewModel {
        val factory = FavoriteModelFactory.getInstance(requireActivity().application)
        return ViewModelProvider(this, factory)[FavoriteEditViewModel::class.java]
    }
}