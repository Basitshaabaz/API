package com.ili.digital.assessmentproject.ui.curiosity

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.PopupWindow
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ili.digital.assessmentproject.R
import com.ili.digital.assessmentproject.adapters.GridAdapter
import com.ili.digital.assessmentproject.databinding.FragmentCuriosityBinding
import com.ili.digital.assessmentproject.data.model.RoverType
import com.ili.digital.assessmentproject.ui.PhotoInfoDialogFragment
import com.ili.digital.assessmentproject.util.Constants.CLEAR_FILTER
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CuriosityFragment : Fragment() {


    private lateinit var roverType: RoverType
    private val binding get() = _binding!!
    private var _binding: FragmentCuriosityBinding? = null


    // Lazily initializing the adapter.
    private val adapter by lazy {
        GridAdapter(GridAdapter.OnClickListener {
            /* Handle item click */
            PhotoInfoDialogFragment(it).show(childFragmentManager, PhotoInfoDialogFragment.TAG)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCuriosityBinding.inflate(inflater, container, false)
        initRecyclerView()
        roverType = arguments?.getSerializable(ARG_ROVER_TYPE) as? RoverType
            ?: throw IllegalArgumentException("RoverType must be provided.")

        return binding.root
    }


    /**
     * responsible for any view click listener
     */
    private fun listeners(viewModel: CuriosityFragmentViewModel) {
        binding.filter.setOnClickListener {
            showPopupWindow(binding.filter,viewModel)
        }
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val viewModel: CuriosityFragmentViewModel by viewModels()
        viewModel.updateRoverType(getRoverType())
        observers(viewModel)
        listeners(viewModel)
    }

    override fun onResume() {
        super.onResume()


    }

    private fun initRecyclerView() {
        binding.rvPhotos.adapter = adapter
    }

    /**
     * Observes the changes in the response of live state.
     * Updates the UI based on the screen state.
     */
    private fun observers(viewModel: CuriosityFragmentViewModel) {


        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.marsPhotoFlow.collect { pagingData ->
                adapter.submitData(pagingData)
            }

        }


    }


    /**
     * Retrieves the RoverType from the fragment's arguments.
     */
    private fun getRoverType(): RoverType {
        return arguments?.getSerializable(ARG_ROVER_TYPE) as? RoverType
            ?: throw IllegalArgumentException("RoverType must be provided.")
    }

    private fun showPopupWindow(anchorView: View, viewModel: CuriosityFragmentViewModel) {
        val popupView = layoutInflater.inflate(R.layout.context_menu, null)
        val popupWindow = PopupWindow(
            popupView,
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            true
        )

        val recyclerView = popupView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        val popAdapter = PopupWindowAdapter(popupWindow,viewModel)
        recyclerView.adapter = popAdapter


        // Set an elevation value if desired
        popupWindow.elevation = 10f

        val list = viewModel.getCameraList(RoverType.CURIOSITY)
        if (list.isEmpty()) {
            list.add("all")
        } else {
            list.add(CLEAR_FILTER)
        }
        popAdapter.submitList(list)
        // Show the popup window
        popupWindow.showAsDropDown(anchorView)

    }


    private inner class PopupWindowAdapter(
        val popupWindow: PopupWindow,
        val viewModel: CuriosityFragmentViewModel
    ) :
        RecyclerView.Adapter<PopupWindowAdapter.ViewHolder>() {

        // Define your data source
        private val itemList = mutableListOf<String>()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val itemView = layoutInflater.inflate(R.layout.item_text_view, parent, false)
            return ViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val item = itemList[position]
            holder.textView.text = item

            // Set click listener for the item view
            viewModel.currentFilter.value?.let {
                holder.textView.setTextColor(
                    ContextCompat.getColor(
                        requireContext(),
                        if (it == item) R.color.dark_red else R.color.white
                    )
                )
            }

            holder.itemView.setOnClickListener {
                // Handle click on the item
                if (item == CLEAR_FILTER || item == "all") {
                    viewModel.clearFilter()
                } else {
                    viewModel.updateFilter(item)
                }
                popupWindow.dismiss()
            }
        }

        override fun getItemCount(): Int {
            return itemList.size
        }

        fun submitList(set: HashSet<String>) {
            val list = set.toMutableList()
            itemList.addAll(list)

        }

        inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            val textView: TextView = itemView as TextView
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {

        private const val ARG_ROVER_TYPE = "rover_type"

        fun newInstance(roverType: RoverType): CuriosityFragment {
            return CuriosityFragment().apply {
                arguments = Bundle().apply {
                    putSerializable(ARG_ROVER_TYPE, roverType)
                }
            }
        }
    }


}