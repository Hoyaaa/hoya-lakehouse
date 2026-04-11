package kr.ac.nsu.hakbokgs.main.store.taesan.main.kimchijjim

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kr.ac.nsu.hakbokgs.databinding.FragmentTaesanMenuChooseBinding
import kr.ac.nsu.hakbokgs.main.store.taesan.db.Menu

class KimchiJjimMenuChooseFragment : DialogFragment() {
    private val TAG = "KimchiJjimMenuChooseFragment"
    private var _binding: FragmentTaesanMenuChooseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        _binding = FragmentTaesanMenuChooseBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        // 다이얼로그 화면 조정
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setDimAmount(0.7f)
        isCancelable = true
        dialog.setCancelable(true)

        // Menu 객체 직접 받기
        val menu = arguments?.getParcelable<Menu>("menu")
        if (menu == null) {
            Log.w(TAG, "전달된 메뉴 객체 없음")
            dismiss()
            return dialog
        }

        Log.i(TAG, "받은 메뉴 ID: ${menu.documentId}, 이름: ${menu.id}")
        updateUI(menu)

        Log.d("test",menu.toString())
        dialog.show()

        // 👇 백반 선택
        binding.btnBaekban.setOnClickListener {
            var bundle = Bundle().apply { putParcelable("menu", menu) }
            val fragment = KimchiJjimMenuBaekbanFragment().apply { arguments = bundle }
            fragment.show(parentFragmentManager, "${menu.id}_baekban_dialog")
        }

        // 👇 정식 선택
        binding.btnJeongsik.setOnClickListener {
            val bundle = Bundle().apply { putParcelable("menu", menu) }
            val fragment = KimchiJjimMenuJeongsikFragment().apply { arguments = bundle }
            fragment.show(parentFragmentManager, "${menu.id}_jeongsik_dialog")
        }

        return dialog
    }

    private fun updateUI(menu: Menu) {
        binding.kimchijjimName.text = menu.id
        binding.kimchijjimDescription.text = menu.description
        binding.kimchijjimIngredient.text = menu.ingredient?.joinToString(",") ?: ""

        // 가격 처리
        val baekbanPrice = menu.size?.baekban?.price
        Log.i("test",baekbanPrice.toString())
        val jeongsikPrice = menu.size?.jeongsik?.price
        binding.kimchijjimPrice.text =
            if (!baekbanPrice.isNullOrEmpty() && !jeongsikPrice.isNullOrEmpty()) {
                "백반 : ${baekbanPrice}원\n정식 : ${jeongsikPrice}원"
            } else {
                "가격 정보 없음"
            }

        // 이미지 로드
        menu.imagePath?.let { path ->
            val ref = FirebaseStorage.getInstance().reference.child(path)
            Glide.with(requireContext())
                .load(ref)
                .into(binding.kimchijjimImage)
        }
    }
}