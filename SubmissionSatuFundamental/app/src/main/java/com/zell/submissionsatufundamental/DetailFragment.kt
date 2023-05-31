package com.zell.submissionsatufundamental

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import com.zell.submissionsatufundamental.databinding.FragmentDetailBinding

class DetailFragment : Fragment() {

    private val detailViewModel by viewModels<DetailViewModel>()
    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val args: DetailFragmentArgs by navArgs()
    private lateinit var favoriteEditViewModel: FavoriteEditViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(inflater, container, false)

        val tabAdapter = FollowAdapter(this, args.username)
        binding.viewPager.adapter = tabAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager) {tab, position ->
            tab.text = TAB_TITLES[position]
        }.attach()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        favoriteEditViewModel = obtainViewModel()

        detailViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        detailViewModel.isGetFailure.observe(viewLifecycleOwner) {
            showFailure(it)
        }

        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                findNavController().navigate(R.id.homeFragment)
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        setDetailUser()
    }

    private fun setDetailUser() {
        detailViewModel.getDetailUser(args.username)
        detailViewModel.userDetail.observe(viewLifecycleOwner) { detailData ->
            with(binding) {
                ivDetailAvatar.loadImage(detailData.avatarUrl)
                tvDetailUsername.text = detailData.login
                tvDetailName.text = detailData.name
                tvCountFollowers.text = detailData.followers.toString()
                tvCountFollowing.text = detailData.following.toString()
            }


            detailData.login?.let {
                favoriteEditViewModel.getFavoriteByUsername(it).observe(viewLifecycleOwner) { favoriteUser ->
                    if (favoriteUser != null) {
                        setImageFav(true)
                        binding.ivFavorite.setOnClickListener {
                            favoriteEditViewModel.delete(favoriteUser)
                            setImageFav(false)
                        }
                    } else {
                        val dataInsert = FavoriteUser(detailData.login, detailData.avatarUrl)
                        setImageFav(false)
                        binding.ivFavorite.setOnClickListener {
                            favoriteEditViewModel.insert(dataInsert)
                            setImageFav(true)
                        }
                    }
                }
            }
        }
    }
    private fun setImageFav(isFavorite: Boolean) {
        if(isFavorite) {
            binding.ivFavorite.setImageDrawable(ContextCompat.getDrawable(binding.ivFavorite.context, R.drawable.ic_favorite))
        } else
            binding.ivFavorite.setImageDrawable(ContextCompat.getDrawable(binding.ivFavorite.context, R.drawable.ic_unfavorite))
    }

    private fun showLoading (isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun showFailure (isGetFailure: Boolean) {
        if (isGetFailure) {
            Snackbar.make(requireView(), getString(R.string.failed_get_data), Snackbar.LENGTH_SHORT).show()
        } else {
            Log.d("HomeFragment", "Get data success!")
        }
    }

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .into(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun obtainViewModel(): FavoriteEditViewModel {
        val factory = FavoriteModelFactory.getInstance(requireActivity().application)
        return ViewModelProvider(this, factory)[FavoriteEditViewModel::class.java]
    }

    companion object {
        private val TAB_TITLES = arrayOf(
            "Followers",
            "Following"
        )

    }
}