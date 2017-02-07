package com.youniversals.playupgo.main

import android.app.Dialog
import android.content.DialogInterface.OnShowListener
import android.os.Bundle
import android.support.design.widget.BottomSheetBehavior
import android.support.design.widget.BottomSheetDialog
import android.support.design.widget.BottomSheetDialogFragment
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.youniversals.playupgo.PlayUpApplication
import com.youniversals.playupgo.R
import com.youniversals.playupgo.flux.store.MatchStore
import com.youniversals.playupgo.matchdetail.MatchDetailsActivity
import com.youniversals.playupgo.newmatch.AddNewMatchActivity
import kotlinx.android.synthetic.main.match_picker_bottom_sheet_dialog_fragment.*
import javax.inject.Inject


class MatchPickerBottomSheetDialogFragment : BottomSheetDialogFragment() {

    @Inject lateinit var matchStore: MatchStore
    var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>? = null
    var onDismissCallback: BottomSheetDismissCallback? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PlayUpApplication.fluxComponent.inject(this)
        isCancelable = false
    }

    override fun setupDialog(dialog: Dialog?, style: Int) {
        super.setupDialog(dialog, style)
        setupDialogBackground()
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val v = inflater!!.inflate(R.layout.match_picker_bottom_sheet_dialog_fragment, container, false)
        return v
    }

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        closeDialogImageView.setOnClickListener {
            onDismissCallback?.dismiss()
            dismiss()
        }
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        val matchAdapter = MatchListAdapter(View.OnClickListener { v ->
            if (v is MatchView) {
                Log.d("MATCH_DETAILS", v.data.toString())
                MatchDetailsActivity.startActivity(activity, v.data!!, v.matchCard!!)
            } else if (v is AddMatchView) {
                AddNewMatchActivity.startActivity(activity)
            }
        })

        matchAdapter.addMatches(matchStore.matches)

        matchesRecyclerView?.layoutManager = layoutManager
        matchesRecyclerView?.adapter = matchAdapter
    }

    private fun setupDialogBackground() {
        dialog.setOnShowListener(OnShowListener { dialog ->
            val d = dialog as BottomSheetDialog
            val bottomSheet = d.findViewById(R.id.design_bottom_sheet) as FrameLayout? ?: return@OnShowListener
            bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
            bottomSheet.background = null
        })
    }

    companion object {
        private val ARG_MATCHES: String = "ARG_MATCHES"

        internal fun newInstance(): MatchPickerBottomSheetDialogFragment {
            return MatchPickerBottomSheetDialogFragment()
        }
    }

    interface BottomSheetDismissCallback {
        fun dismiss()
    }
}