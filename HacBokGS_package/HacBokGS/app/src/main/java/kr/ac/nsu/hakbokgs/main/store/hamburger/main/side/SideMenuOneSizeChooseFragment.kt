package kr.ac.nsu.hakbokgs.main.store.hamburger.main.side;

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import kr.ac.nsu.hakbokgs.R
import kr.ac.nsu.hakbokgs.main.store.hamburger.db.Menu
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kr.ac.nsu.hakbokgs.databinding.FragmentMenuChooseNoOptionBinding
import kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu
import kr.ac.nsu.hakbokgs.main.store.hamburger.main.BurgerListActivity

class SideMenuOneSizeChooseFragment : DialogFragment() {
    private val TAG = "SideMenuOneSizeChooseFragment"
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
        updateUI(menu) // ✅ 누락된 핵심 호출

        dialog.show()

        val userId = FirebaseAuth.getInstance().currentUser?.email ?: ""

        // 전달받은 Menu 객체
        val storeName = "버거운버거"
        val menuName = menu.id
        val menuPrice = 0
        var amount = 1

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

        // 👇 장바구니 추가
        binding.btnAddCart.setOnClickListener {
            val basketItem = BasketMenu(
                store = storeName,
                menu = menuName.toString(),
                menuPrice = menuPrice,
                amount = amount,
            )

            // 📁 Firestore에 데이터 추가
            FirebaseFirestore.getInstance()
                .collection("users").document(userId)
                .collection("basket")
                .add(basketItem)
                .addOnSuccessListener {
                    Toast.makeText(context, "장바구니에 담았습니다.", Toast.LENGTH_SHORT).show()

                    // 장바구니 담기 후 이동
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
