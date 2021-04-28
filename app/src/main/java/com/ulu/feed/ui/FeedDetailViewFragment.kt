package com.ulu.feed.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.ulu.feed.models.Feed
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ulu.feed.R
import com.ulu.feed.databinding.FragementFeedDetailBottomSheetBinding

class FeedDetailViewFragment : BottomSheetDialogFragment(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetFixedStyle)

    }

    lateinit var binding: FragementFeedDetailBottomSheetBinding


    companion object {
        const val KEY_FEED_ITEM = "key_feed_item"
        fun newInstance(
            feed: Feed?,
        ) = FeedDetailViewFragment().apply {
            arguments = Bundle().apply {
                putParcelable(KEY_FEED_ITEM, feed)
            }
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragementFeedDetailBottomSheetBinding.inflate(inflater, container, false)
        binding.lifecycleOwner = this
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dialog?.setCanceledOnTouchOutside(false)
        var doDoItem =
            arguments?.getParcelable<Feed>(KEY_FEED_ITEM)
        binding.item = doDoItem

    }

    override fun onClick(v: View?) {

    }
}