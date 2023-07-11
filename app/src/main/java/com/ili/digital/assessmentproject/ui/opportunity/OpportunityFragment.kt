package com.ili.digital.assessmentproject.ui.opportunity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ili.digital.assessmentproject.R
import com.ili.digital.assessmentproject.adapters.GridAdapter
import com.ili.digital.assessmentproject.data.model.MarsPhoto
import com.ili.digital.assessmentproject.databinding.FragmentCuriosityBinding
import com.ili.digital.assessmentproject.databinding.FragmentOpportunityBinding
import com.ili.digital.assessmentproject.ui.PhotoInfoDialogFragment
import com.ili.digital.assessmentproject.ui.curiosity.CuriosityFragmentViewModel
import com.ili.digital.assessmentproject.util.Constants
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class OpportunityFragment : Fragment() , MenuProvider {

    private var endOfPage = false
    private var isLoading = false
    private var searchMode = false

    private val binding get() = _binding!!
    private var _binding: FragmentOpportunityBinding? = null
    private var cameraList: HashSet<String>? = null
    private var photoList: MutableList<MarsPhoto> = mutableListOf()
    private val viewModel: OpportunityFragmentViewModel by viewModels()
    private val adapter = GridAdapter(GridAdapter.OnClickListener {
        /* Handle item click */
        PhotoInfoDialogFragment(it).show(childFragmentManager, PhotoInfoDialogFragment.TAG)
    })


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOpportunityBinding.inflate(inflater, container, false)
        initRecyclerView()
        observers()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun initRecyclerView() {
        binding.rvPhotos.adapter = adapter
    }

    /**
     * Observes the changes in the response of live state.
     * Updates the UI based on the screen state.
     */
    private fun observers() {
        viewLifecycleOwner.lifecycleScope.launch {
            getOpportunity()
            viewModel.opportunityState.collect { screenState ->
                if (screenState.isLoading) {
                    binding.progress.visibility = View.VISIBLE
                    binding.rvPhotos.visibility = View.GONE
                } else if (screenState.photoList?.isNotEmpty() == true) {
                    binding.progress.visibility = View.GONE
                    binding.rvPhotos.visibility = View.VISIBLE

                    updateRecyclerView(screenState.photoList)
                    cameraList = screenState.cameraList!!
                    addNewMarsPhotos(photoList, screenState.photoList.toMutableList())
                } else {
                    endOfPage = true
                    binding.progress.visibility = View.GONE
                    binding.rvPhotos.visibility = View.VISIBLE
                    Toast.makeText(context, "No Data Found", Toast.LENGTH_SHORT).show()
                }
            }
        }
        setupPager()
    }

    private fun getOpportunity() {
        searchMode = false
        viewLifecycleOwner.lifecycleScope.launch { viewModel.getOpportunity() }
    }

    private fun setupPager() {
        binding.rvPhotos.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as GridLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()
                val isLastItemVisible =
                    (visibleItemCount + firstVisibleItemPosition) >= totalItemCount

                if (isLastItemVisible && !isLoading && !endOfPage && !searchMode) {
                    isLoading = true
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.loadMoreOpportunity()
                        isLoading = false
                    }
                }
            }
        })
    }

    /**
     * Updates the RecyclerView with the given photoList.
     */
    private fun updateRecyclerView(photoList: List<MarsPhoto>) {
        adapter.updateData(photoList)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    override fun onPrepareMenu(menu: Menu) {
        super.onPrepareMenu(menu)
        menu.clear()
        if (cameraList.isNullOrEmpty()) {
            menu.add("all")
        } else {
            for (camera in cameraList!!) {
                menu.add(camera)
            }
            menu.add(Constants.CLEAR_FILTER)
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.camera_menu, menu)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        val filter = menuItem.title
        searchMode = true
        adapter.filterByCamera(filter.toString())
        if (filter.toString().equals(Constants.CLEAR_FILTER)) {
            getOpportunity()
        }
        return true
    }

    /**
     * Adds new Mars photos to the firstList if they are not already present.
     */
    private fun addNewMarsPhotos(
        firstList: MutableList<MarsPhoto>,
        secondList: MutableList<MarsPhoto>
    ) {
        for (photo in secondList) {
            if (!firstList.contains(photo)) {
                firstList.add(photo)
            }
        }
    }




    companion object {

        fun newInstance(): OpportunityFragment {
            return OpportunityFragment()
        }
    }
}