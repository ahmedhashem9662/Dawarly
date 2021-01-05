package com.dawarly.sub

import android.app.Dialog
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import com.dawarly.observer.OnShowMarkerDialog
import com.example.dawarly.R
import com.example.dawarly.databinding.PopupShowMarkerDialogBinding


class PopupShowMarkerDialog : DialogFragment() {

    lateinit var mListener: OnShowMarkerDialog
    lateinit var binding: PopupShowMarkerDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.popup_show_marker_dialog,
            container,
            false

        )
        return binding.root
    }

    fun setmListener(mListener: OnShowMarkerDialog) {
        this.mListener = mListener
    }


    lateinit internal var dialog: Dialog

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        dialog = Dialog(requireActivity())
        dialog = Dialog(requireActivity(), R.style.FullWidthDialogTheme)
        dialog.window!!.setBackgroundDrawableResource(R.color.transparent)
        dialog.window!!.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE
        )
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        dialog.window!!.setGravity(Gravity.CENTER_HORIZONTAL or Gravity.CENTER_VERTICAL)
        dialog.setCanceledOnTouchOutside(true)
        return dialog
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initViews()
        setListener()

    }

    var title = ""
    var message = ""
    var negativeButtonText = ""
    var positiveButtonText = ""
    var isShowPositiveButton = false
    var isShowNegativeButton = false

    fun initViews() {
        title = requireArguments().getString("title", "")!!
        message = requireArguments().getString("message", "")!!
        negativeButtonText = requireArguments().getString("negativeButtonText", "")!!
        positiveButtonText = requireArguments().getString("positiveButtonText", "")!!
        isShowPositiveButton = requireArguments().getBoolean("isShowPositiveButton", false)
        isShowNegativeButton = requireArguments().getBoolean("isShowNegativeButton", false)

        binding.tvTitle.text = title
        binding.tvMessage.text = message

        binding.placeDetails.text = positiveButtonText
        binding.drawPath.text = negativeButtonText

        binding.placeDetails.visibility = if (isShowPositiveButton) View.VISIBLE else View.GONE
        binding.drawPath.visibility = if (isShowNegativeButton) View.VISIBLE else View.GONE

    }

    fun setListener() {
        binding.placeDetails.setOnClickListener {
            mListener.onPositiveClicked()
            dismissAllowingStateLoss()
        }

        binding.drawPath.setOnClickListener {
            mListener.onNegativeClicked()
            dismissAllowingStateLoss()
        }
    }
}



