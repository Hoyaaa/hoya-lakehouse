package kr.ac.nsu.hakbokgs.main.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import kr.ac.nsu.hakbokgs.R

class GenericDialogFragment : DialogFragment() {

    companion object {
        private const val ARG_LAYOUT_ID = "layout_id"

        fun newInstance(layoutId: Int): GenericDialogFragment {
            val fragment = GenericDialogFragment()
            val args = Bundle().apply {
                putInt(ARG_LAYOUT_ID, layoutId)
            }
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val layoutId = arguments?.getInt(ARG_LAYOUT_ID) ?: 0
        val rootView = inflater.inflate(layoutId, container, false)

        // ✅ 닫기 버튼 클릭 시 다이얼로그 종료
        rootView.findViewById<ImageView>(R.id.map_closeButton)?.setOnClickListener {
            dismiss()
        }

        return rootView
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }
}
