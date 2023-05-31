package com.zell.intermediatesubmission.ui.detail

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.zell.intermediatesubmission.R
import com.zell.intermediatesubmission.databinding.FragmentDetailBinding
import com.zell.intermediatesubmission.helper.bearerGenerate
import com.zell.intermediatesubmission.helper.formatDate
import com.zell.intermediatesubmission.helper.isLoading
import com.zell.intermediatesubmission.utils.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val detailViewModel: DetailViewModel by viewModels()
    private val args: DetailFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener {
            findNavController().navigate(R.id.homeFragment)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        setDetailData()
    }

    private fun setDetailData() {

        detailViewModel.getToken("token").observe(viewLifecycleOwner) {
            val token = bearerGenerate(it)
            detailViewModel.getDetailStory(args.storyId, token).observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Result.Loading -> isLoading(true, binding.progressBar)

                    is Result.Success -> {
                        isLoading(false, binding.progressBar)
                        with(binding) {
                            tvDetailName.text = response.data.story?.name
                            tvDetailDate.text = formatDate(response.data.story?.createdAt.toString())
                            tvItemDescription.text = response.data.story?.description

                            Glide.with(requireContext())
                                .load(response.data.story?.photoUrl)
                                .centerCrop()
                                .into(ivDetailPhoto)
                        }
                    }

                    is Result.Error -> {
                        isLoading(false, binding.progressBar)
                        Toast.makeText(requireContext(), response.error, Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
    }

}