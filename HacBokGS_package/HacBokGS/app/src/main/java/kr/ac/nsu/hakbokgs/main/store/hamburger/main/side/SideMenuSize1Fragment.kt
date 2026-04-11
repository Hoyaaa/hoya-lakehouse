package kr.ac.nsu.hakbokgs.main.store.hamburger.main.side

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kr.ac.nsu.hakbokgs.databinding.FragmentMenuChooseNoOptionBinding
import kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu
import kr.ac.nsu.hakbokgs.main.store.hamburger.db.Menu
import kr.ac.nsu.hakbokgs.main.store.hamburger.main.BurgerListActivity
import kr.ac.nsu.hakbokgs.main.store.kkuihankki.main.KkuiListActivity
import kotlin.toString

class SideMenuSize1Fragment : DialogFragment() {
    private val TAG = "SideMenuSize1Fragment"
    private var _binding: FragmentMenuChooseNoOptionBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        _binding = FragmentMenuChooseNoOptionBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setDimAmount(0.7f)
        isCancelable = true

        // ✅ menu 객체 받기
        val menu = arguments?.getSerializable("menu") as? Menu ?: return dialog
        val storeName = "버거운버거"
        val menuName = menu.id

        // 가격 처리
        var menuPrice = 0
        var value = ""
        if (menu.size?.contains("small") == true) {
            menuPrice =
                (menu.size?.get("small") as? Map<*, *>)?.get("price")?.toString()?.toIntOrNull()
                    ?: 0
            value = "소"
        } else if (menu.size?.contains("4pieces") == true) {
            menuPrice =
                (menu.size?.get("4pieces") as? Map<*, *>)?.get("price")?.toString()?.toIntOrNull()
                    ?: 0
            value = "4조각"
        } else {
            menuPrice = 0
        }

        updateUI(menu, menuPrice, value) // ✅ menu 정보 UI에 반영

        var amount = 0

        val userId = FirebaseAuth.getInstance().currentUser?.email ?: "testId"

        val amountText = binding.amountText
        amountText.text = "$amount 개"

        // 👇 수량 증가
        binding.btnPlus.setOnClickListener {
            amount++
            binding.amountText.text = "${amount}개"
        }

        // 👇 수량 감소
        binding.btnMinus.setOnClickListener {
            if (amount > 1) {
                amount--
                binding.amountText.text = "${amount}개"
            } else {
                Toast.makeText(context, "수량은 1개 이상 가능합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        // 장바구니 추가
        binding.btnAddCart.setOnClickListener {
            val basketItem = BasketMenu(
                store=storeName,
                menu=menuName.toString()+ "(${value})",
                menuPrice = menuPrice,
                amount = amount,
            )

            FirebaseFirestore.getInstance()
                .collection("users").document(userId)
                .collection("basket")
                .add(basketItem)
                .addOnSuccessListener {
                    Toast.makeText(context, "장바구니에 담았습니다.", Toast.LENGTH_SHORT).show()

                    // ✅ 장바구니 담기 후 이동
                    val intent = Intent(requireContext(), BurgerListActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                    dismiss()  // 팝업 종료
                }
                .addOnFailureListener {
                    Log.e(tag, "Firestore 저장 실패", it)
                    Toast.makeText(context, "추가 실패", Toast.LENGTH_SHORT).show()
                }
        }
        return dialog
    }

    private fun updateUI(menu: Menu, price: Int, value: String) {
        binding.menuName.text = menu.id + "(${value})"
        binding.menuDescription.text = menu.description ?: "설명 없음"
        binding.menuIngredient.text = menu.ingredient?.joinToString(", ") ?: "재료 정보 없음"
        binding.menuPrice.text = "${String.format("%,d", price)}원"

        // 이미지 처리
        menu.imagePath?.let { path ->
            val storageRef =
                com.google.firebase.storage.FirebaseStorage.getInstance().reference.child(path)
            com.bumptech.glide.Glide.with(requireContext())
                .load(storageRef)
                .placeholder(kr.ac.nsu.hakbokgs.R.drawable.burger_image)
                .into(binding.menuImage)
        }
    }
}