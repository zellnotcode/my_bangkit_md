package com.zell.intermediatesubmission.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.android.gms.maps.model.MarkerOptions
import com.zell.intermediatesubmission.R
import com.zell.intermediatesubmission.databinding.FragmentMapsBinding
import com.zell.intermediatesubmission.helper.bearerGenerate
import com.zell.intermediatesubmission.utils.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MapsFragment : Fragment(), OnMapReadyCallback {

    private var _binding : FragmentMapsBinding? = null
    private val binding get() = _binding!!
    private lateinit var mMap: GoogleMap
    private val mapsViewModel: MapsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMapsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnBack.setOnClickListener { findNavController().popBackStack() }
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)


        addStoryMarker()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.map_style))
        with( mMap.uiSettings) {
            isZoomControlsEnabled = true
            setAllGesturesEnabled(true)
        }
    }

    private fun addStoryMarker() {
        mapsViewModel.getToken("token").observe(viewLifecycleOwner) { tokenResult ->
            val token = bearerGenerate(tokenResult)
            mapsViewModel.getAllStories(token).observe(viewLifecycleOwner) { response ->
                when (response) {
                    is Result.Loading -> {}
                    is Result.Success -> {
                        response.data.listStory?.forEach {
                            val latLng = if (it?.lat != null && it.lon != null) {
                                LatLng(it.lat, it.lon)
                            } else {
                                LatLng (-1.0 , -1.0)
                            }

                            if (latLng.latitude != -1.0 && latLng.longitude != 1.0) {
                                mMap.addMarker(
                                    MarkerOptions()
                                    .position(latLng)
                                        .title(it?.name)
                                        .snippet(it?.description))
                            }
                        }
                    }
                    is Result.Error -> {
                        Toast.makeText(requireContext(), response.error, Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}