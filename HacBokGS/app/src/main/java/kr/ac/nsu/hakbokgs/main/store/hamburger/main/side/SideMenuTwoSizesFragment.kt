package kr.ac.nsu.hakbokgs.main.store.hamburger.main.side


import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kr.ac.nsu.hakbokgs.R
import kr.ac.nsu.hakbokgs.databinding.FragmentBurgerSideTwoSizesChooseBinding
import kr.ac.nsu.hakbokgs.main.store.hamburger.db.Menu

class SideMenuTwoSizesFragment : DialogFragment() {
    private val TAG = "SideMenuTwoSizesFragment"
    private var _binding: FragmentBurgerSideTwoSizesChooseBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        _binding = FragmentBurgerSideTwoSizesChooseBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setDimAmount(0.7f)
        isCancelable = true
        dialog.setCancelable(true)
        binding.pinImage.bringToFront()

        // 객체 직접 받기
        val menu = arguments?.getSerializable("menu") as Menu
        if (menu == null) {
            Log.w(TAG, "전달된 메뉴 ID 없음")
            dismiss()
            return dialog
        }

        Log.i(TAG, "받은 메뉴 ID: ${menu.documentId}, 이름: ${menu.id}")
        updateUI(menu)


        // 👇 사이즈 1 버튼 _small, 4pieces
        binding.btnMenuSize1.text = if (menu.size?.contains("small") == true) {
            "소"
        } else if (menu.size?.contains("4pieces") == true) {
            "4조각"
        } else {
            "정보 없음"
        }
        binding.btnMenuSize1.setOnClickListener {
            val bundle = Bundle().apply { putSerializable("menu", menu) }
            val fragment = SideMenuSize1Fragment().apply { arguments = bundle }
            fragment.show(parentFragmentManager, "${menu.id}_size1_dialog")
        }

        // 👇 사이즈 2 버튼 _medium, 10pieces
        binding.btnMenuSize2.text = if (menu.size?.contains("medium") == true) {
            "중"
        } else if (menu.size?.contains("10pieces") == true) {
            "10조각"
        } else {
            "정보 없음"
        }
        binding.btnMenuSize2.setOnClickListener {
            val bundle = Bundle().apply { putSerializable("menu", menu) }
            val fragment = SideMenuSize2Fragment().apply { arguments = bundle }
            fragment.show(parentFragmentManager, "${menu.id}_size2_dialog")
        }

        dialog.show()
        return dialog
    }

    private fun updateUI(menu: Menu) {
        binding.menuName.text = menu.id
        binding.menuDescription.text = menu.description
        binding.menuIngredient.text = menu.ingredient?.joinToString(", ")


        // ✅ 사이드 메뉴 가격 구성 출력
        val priceText = StringBuilder()
        menu.size?.forEach { (key, value) ->
            when (value) {
                is String -> priceText.append("$key: ${value}원\n")
                is Map<*, *> -> {
                    val price = value["price"]
                    if (price is String) priceText.append("$key: ${price}원\n")
                }
            }
        }
        binding.menuPrice.text = priceText.toString().trim()

        // 이미지 로딩
        menu.imagePath?.let { path ->
            val storageRef = FirebaseStorage.getInstance().reference.child(path)
            Glide.with(requireContext())
                .load(storageRef)
                .placeholder(R.drawable.burger_side_image)
                .into(binding.menuImage)
        }
    }

}