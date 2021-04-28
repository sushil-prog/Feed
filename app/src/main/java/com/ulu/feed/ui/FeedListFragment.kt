package com.ulu.feed.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.os.SystemClock
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import androidx.recyclerview.widget.RecyclerView
import com.aiegroup.todo.utils.Utility
import com.ulu.feed.adapters.RowItemAdapter
import com.ulu.feed.viewmodels.FeedsViewModel
import com.ulu.feed.R
import com.ulu.feed.databinding.FragmentFeedListBinding
import com.ulu.feed.models.Feed
import com.ulu.feed.utils.PageIndexKey
import com.ulu.feed.utils.Resource
import com.ulu.feed.utils.Sort
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel
import java.util.*


/**
 * FeedList Fragment for showing the list of task list.
 */
class FeedListFragment : Fragment() {


    private val feedsViewModel by viewModel<FeedsViewModel>()

    private val sharedPreferences: SharedPreferences by inject()

    private val sharedPreferencesEditor: SharedPreferences.Editor by inject()

    private lateinit var rowItemAdapter: RowItemAdapter

    private lateinit var binding: FragmentFeedListBinding
    var isScrolling = false

    companion object {
        const val KEY_PAGE_INDEX = "key_page_index"
    }

    /**
     * RecyclerView Adapter for showing list of task items
     */


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentFeedListBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        rowItemAdapter = RowItemAdapter(object : TaskItemClick {
            override fun click(feed: Feed) {
                openBottomSheet(feed)
            }
        })
        var view = binding.root
        val dividerItemDecoration = DividerItemDecoration(
            binding.recycleView?.context,
            VERTICAL
        )
        binding.recycleView?.addItemDecoration(dividerItemDecoration)
        binding.recycleView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = rowItemAdapter
        }
        binding.recycleView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    loadMore(recyclerView)
                }
            }

            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }


        })

        binding.sortImageView.setOnClickListener { v ->
            showSortingMenu(v)
            //feedsViewModel.sortBy(Sort.SORT_NONE)
        }
        binding.retryButton.setOnClickListener { v ->
            feedsViewModel.fetchFeedItems(PageIndexKey.pageMap[0].toString())
        }
        return view
    }

    private fun loadMore(recyclerView: RecyclerView) {
        isScrolling = true
        var layoutManager = (recyclerView.layoutManager as LinearLayoutManager)
        var pageIndex = sharedPreferences.getInt(KEY_PAGE_INDEX, 0)
        var currentItems =
            layoutManager.childCount;
        var totalItems =
            layoutManager.itemCount;
        var scrollOutItems =
            layoutManager.findFirstVisibleItemPosition();
        if (isScrolling && (currentItems + scrollOutItems == totalItems) && !isLastPage(pageIndex)) {
            feedsViewModel.fetchFeedItems(PageIndexKey.pageMap[pageIndex].toString())

        }
    }

    fun isLastPage(pageIndex: Int): Boolean {
        return PageIndexKey.pageMap[pageIndex].isNullOrEmpty()
    }

    private var mLastClickTime: Long = 0

    lateinit var detailViewFragment: FeedDetailViewFragment
    fun openBottomSheet(feed: Feed?) {
        // check for last click event to avoid open multiple bottom sheet dialog immediately
        if (SystemClock.elapsedRealtime() - mLastClickTime < 1000) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();
        detailViewFragment = FeedDetailViewFragment.newInstance(
            feed
        )
        activity?.let { it1 ->
            // show bottom sheet dialog for create and eoit task
            detailViewFragment.show(
                it1.supportFragmentManager,
                detailViewFragment.tag
            )

        }

    }

    /**
     * Called immediately after onCreateView() has returned, and fragment's
     * view hierarchy has been created. It can be used to do final
     * initialization once these pieces are in place, such as retrieving
     * views or restoring state.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        feedsViewModel.feedsData.observe(viewLifecycleOwner, Observer {
            when (it.status) {
                Resource.Status.SUCCESS -> {
                    isScrolling = false
                    binding.progressBar.visibility = View.GONE
                    if (!it.data?.posts.isNullOrEmpty()) {
                        if (binding.errorLayout.isVisible) {
                            binding.errorLayout.visibility = View.GONE
                        }
                        it.data?.page?.let {
                            sharedPreferencesEditor.putInt(KEY_PAGE_INDEX, it).commit()
                        }
                        rowItemAdapter.addItems(ArrayList(it.data?.posts))
                        binding.sortImageView.visibility = View.VISIBLE
                    }
                }
                Resource.Status.SORT -> {
                    if (!it.data?.posts.isNullOrEmpty()) {
                        rowItemAdapter.setItems(ArrayList(it.data?.posts))
                    }
                }
                Resource.Status.ERROR -> {
                    binding.progressBar.visibility = View.GONE
                    if (binding.errorLayout.isVisible) {
                        binding.errorLayout.visibility = View.GONE
                    }
                    isScrolling = false
                    var mesage = it.message
                    activity?.let {
                        if (!Utility.isNetworkConnected(requireActivity()) && rowItemAdapter.itemCount <= 0) {
                            binding.errorLayout.visibility = View.VISIBLE
                            binding.errortextView.text = getString(R.string.network_error)
                            if (rowItemAdapter.itemCount <= 0) {
                                binding.errorLayout.visibility = View.VISIBLE
                                binding.errortextView.text = getString(R.string.network_error)
                            } else {
                                if (binding.errorLayout.isVisible) {
                                    binding.errorLayout.visibility = View.GONE
                                }
                            }
                        } else if (Utility.isNetworkConnected(requireActivity()) && rowItemAdapter.itemCount <= 0) {
                            binding.errorLayout.visibility = View.VISIBLE
                            binding.errortextView.text = mesage

                        } else {
                            if (binding.errorLayout.isVisible) {
                                binding.errorLayout.visibility = View.GONE
                            }
                            Toast.makeText(requireContext(), mesage, Toast.LENGTH_SHORT).show()
                        }
                    }
                }

                Resource.Status.LOADING -> {

                    binding.progressBar.visibility = View.VISIBLE
                }
            }
        })
    }

    private lateinit var popupMenu: PopupMenu
    private var pop_menu_index = 0
    fun showSortingMenu(v: View?) {
        popupMenu = PopupMenu(context, v)
        val inflater1: MenuInflater = popupMenu.menuInflater
        inflater1.inflate(R.menu.sort_menu, popupMenu.menu)
        popupMenu.menu.getItem(pop_menu_index).isChecked = true
        popupMenu.setOnMenuItemClickListener { item ->
            val id = item.itemId
            when (id) {
                R.id.short_by_date -> {
                    pop_menu_index = 4
                    feedsViewModel.sortBy(Sort.SORT_DSC_DATE)
                    true
                }
                R.id.short_by_views -> {
                    pop_menu_index = 3
                    feedsViewModel.sortBy(Sort.SORT_DSC_VIEW)
                    true
                }
                R.id.sort_by_share -> {
                    feedsViewModel.sortBy(Sort.SORT_DSC_SHARE)
                    pop_menu_index = 2
                    true
                }


                R.id.sort_by_like -> {
                    feedsViewModel.sortBy(Sort.SORT_DSC_LIKE)
                    pop_menu_index = 1
                    true
                }
                R.id.none -> {
                    feedsViewModel.sortBy(Sort.SORT_NONE)
                    pop_menu_index = 0
                    true
                }

                else -> false
            }
        }
        popupMenu.show()
    }


    override fun onDestroyView() {
        super.onDestroyView()
    }

    /**
    interface for edit and delete task
     */
    interface TaskItemClick {
        fun click(feed: Feed);
    }

}