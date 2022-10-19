package com.digipanther.cuteh.fragment

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import com.digipanther.cuteh.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class GalleryBottomSheet : BottomSheetDialogFragment, View.OnClickListener {

    private var clickListener: OnOptionCLickListener? = null
    private lateinit var btnCancel: TextView
    private lateinit var chooseCameraLayout: LinearLayout
    private lateinit var chooseGalleryLayout: LinearLayout
    var onDialogClosed: GalleryBottomSheet.OnOptionCLickListener? = null
    var mActivity = FragmentActivity()

    constructor() {
        // Required empty public constructor
    }

    constructor(
        clickListener: OnOptionCLickListener?,
    ) {
        // Required empty public constructor
        this.clickListener = clickListener
        this.onDialogClosed = clickListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.gallery_bottom_sheet, container, false)

        btnCancel = view.findViewById(R.id.btnCancel)
        btnCancel.setOnClickListener(this)

        chooseCameraLayout = view.findViewById(R.id.chooseCameraLayout)
        chooseCameraLayout.setOnClickListener(this)

        chooseGalleryLayout = view.findViewById(R.id.chooseGalleryLayout)
        chooseGalleryLayout.setOnClickListener(this)


        return view
    }


    interface OnOptionCLickListener {
        fun onManageClick(optionId: Int)
        fun onDialogOpened(isOpened: Boolean)
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDialogClosed?.onDialogOpened(false)
    }

    override
    fun onAttach(context: Context) {
        super.onAttach(context)

        mActivity = context as AppCompatActivity

    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btnCancel -> {
                dismiss()
            }
            R.id.chooseCameraLayout ->
            {
                clickListener!!.onManageClick(0)
                dismiss()
            }
            R.id.chooseGalleryLayout -> {
                clickListener!!.onManageClick(1)
                dismiss()
            }
        }
    }

}