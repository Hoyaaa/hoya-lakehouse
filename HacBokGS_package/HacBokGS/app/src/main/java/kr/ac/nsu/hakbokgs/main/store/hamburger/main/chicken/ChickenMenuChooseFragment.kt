package kr.ac.nsu.hakbokgs.main.store.hamburger.main.chicken;

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import kr.ac.nsu.hakbokgs.R
import kr.ac.nsu.hakbokgs.databinding.ChickenMenuChooseBinding
import kr.ac.nsu.hakbokgs.main.store.hamburger.db.Menu
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kr.ac.nsu.hakbokgs.main.store.hamburger.main.MENU_ID

class ChickenMenuChooseFragment : DialogFragment() {
    private val TAG = "ChickenMenuChoose"
    private var _binding: ChickenMenuChooseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        _binding = ChickenMenuChooseBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        // 다이얼로그 화면 조정
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setDimAmount(0.7f)
        isCancelable = true
        dialog.setCancelable(true)
        binding.pinImage.bringToFront()

        // Menu 객체 직접 받기
        val menu = arguments?.getSerializable("menu") as? Menu
        if (menu == null) {
            Log.w(TAG, "전달된 메뉴 ID 없음")
            dismiss()
            return dialog
        }

        Log.i(TAG, "받은 메뉴 ID: ${menu.documentId}, 이름: ${menu.id}")
        updateUI(menu)

        binding.btnMenuSmall.setOnClickListener {
            var bundle = Bundle().apply { putSerializable("menu", menu) }
            val fragment = ChickenMenuSmallFragment().apply { arguments = bundle }
            fragment.show(parentFragmentManager, "${menu.id}_small_dialog")
        }

        binding.btnMenuMedium.setOnClickListener {
            var bundle = Bundle().apply { putSerializable("menu", menu) }
            val fragment = ChickenMenuMediumFragment().apply { arguments = bundle }
            fragment.show(parentFragmentManager, "${menu.id}_medium_dialog")
        }

        dialog.show()
        return dialog
    }

    private fun updateUI(menu: Menu) {
        binding.chickenName.text = menu.id
        binding.chickenDescription.text = menu.description
        binding.chickenIngredient.text = menu.ingredient?.joinToString(", ")

        // 가격 정보 파싱
        val priceInfo = StringBuilder()
        menu.size?.forEach { (key, value) ->
            when (value) {
//                is String -> priceText.append("$key: ${value}원\n")
                is Map<*, *> -> {
                    val price = value["price"] ?: "?"
//                    if (price is String) priceText.append("$key: ${price}원\n")
                    priceInfo.append("${key}: ${price}원\n")
                }

                is String -> {
                    priceInfo.append("${key}: ${value}원\n")
                }
            }
        }
        binding.chickenPrice.text = priceInfo.toString().trim()

        // 이미지 로딩
        menu.imagePath?.let { path ->
            val storageRef = FirebaseStorage.getInstance().reference.child(path)
            Glide.with(requireContext())
                .load(storageRef)
                .placeholder(R.drawable.chicken_image)
                .into(binding.sideImage)
        }
    }
}
