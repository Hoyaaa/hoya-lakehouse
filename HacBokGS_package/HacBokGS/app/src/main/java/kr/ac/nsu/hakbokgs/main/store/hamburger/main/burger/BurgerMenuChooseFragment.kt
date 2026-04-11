package kr.ac.nsu.hakbokgs.main.store.hamburger.main.burger

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import kr.ac.nsu.hakbokgs.R
import kr.ac.nsu.hakbokgs.databinding.FragmentBurgerMenuChooseBinding
import kr.ac.nsu.hakbokgs.main.store.hamburger.db.Menu
import com.google.firebase.storage.FirebaseStorage

class BurgerMenuChooseFragment : DialogFragment() {
    private val Tag = "Burger Menu Choose"
    private var _binding: FragmentBurgerMenuChooseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        _binding = FragmentBurgerMenuChooseBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setDimAmount(0.7f)
        isCancelable = true
        dialog.setCancelable(true)
        binding.pinImage.bringToFront()

        // ✅ Menu 객체 직접 받기
        val menu = arguments?.getSerializable("menu") as? Menu
        if (menu == null) {
            Log.w(Tag, "전달된 메뉴 객체 없음")
            dismiss()
            return dialog
        }

        Log.i(Tag, "받은 메뉴 ID: ${menu.documentId}, 이름: ${menu.id}")
        updateUI(menu) // ✅ 누락된 핵심 호출

        binding.btnMenuSingle.setOnClickListener {
            val bundle = Bundle().apply { putSerializable("menu", menu) }
            val fragment = BurgerMenuSingleFragment().apply { arguments = bundle }
            fragment.show(parentFragmentManager, "${menu.id}_single_dialog")
        }

        binding.btnMenuSet.setOnClickListener {
            val bundle = Bundle().apply { putSerializable("menu", menu) }
            val fragment = BurgerMenuSetFragment().apply { arguments = bundle }
            fragment.show(parentFragmentManager, "${menu.id}_set_dialog")
        }

        dialog.show()
        return dialog
    }

    private fun updateUI(menu: Menu) {
        binding.burgerName.text = menu.id
        binding.burgerDescription.text = menu.description
        binding.burgerIngredient.text = menu.ingredient?.joinToString(", ")

        // 가격 처리 (유연하게 파싱)
        val priceInfo = StringBuilder()
        menu.size?.forEach { (key, value) ->
            when (value) {
                is Map<*, *> -> {
                    val price = value["price"] ?: "?"
                    priceInfo.append("${key}: ${price}원\n")
                }
                is String -> {
                    priceInfo.append("${key}: ${value}원\n")
                }
            }
        }
        binding.burgerPrice.text = priceInfo.toString().trim()

        // 이미지 처리
        menu.imagePath?.let { path ->
            val storageRef = FirebaseStorage.getInstance().reference.child(path)
            Glide.with(requireContext())
                .load(storageRef)
                .placeholder(R.drawable.burger_store_image)
                .into(binding.burgerImage)
        }
    }
}
