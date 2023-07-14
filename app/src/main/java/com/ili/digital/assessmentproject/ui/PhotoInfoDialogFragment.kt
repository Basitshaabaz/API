package com.ili.digital.assessmentproject.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ili.digital.assessmentproject.databinding.FragmentPhotoInfoDialogBinding
import com.ili.digital.assessmentproject.data.model.MarsPhoto
import com.ili.digital.assessmentproject.util.loadUrl

class PhotoInfoDialogFragment(
    private val photo: MarsPhoto
) :
    DialogFragment() {
    private lateinit var binding: FragmentPhotoInfoDialogBinding

    companion object {
        const val TAG = "PhotoInfoDialog"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPhotoInfoDialogBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photo.img_src.let { binding.ivPhoto.loadUrl(it) }
        binding.txtDate.text = "Date: ${photo.earth_date}"
        binding.txtRoverName.text = "Name: ${photo.rover?.name!!}"
        binding.txtCamName.text = "Camera: ${photo.camera?.name!!}"
        binding.txtStatus.text = "Status: ${photo.rover?.status!!}"
        binding.txtLaunchDate.text = "Launch Date: ${photo.rover?.launch_date!!}"
        binding.txtLandingDate.text = "Landing Date: ${photo.rover?.landing_date!!}"
    }
}