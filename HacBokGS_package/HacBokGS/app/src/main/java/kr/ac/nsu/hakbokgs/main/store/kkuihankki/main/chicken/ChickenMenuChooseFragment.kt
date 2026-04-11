package kr.ac.nsu.hakbokgs.main.store.kkuihankki.main.chicken

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
import kr.ac.nsu.hakbokgs.databinding.FragmentKkuiChickenMenuChooseBinding
import kr.ac.nsu.hakbokgs.main.store.kkuihankki.db.Menu
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kr.ac.nsu.hakbokgs.databinding.ActivityKkuiListBinding
import kr.ac.nsu.hakbokgs.databinding.FragmentMenuChooseNoOptionBinding
import kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu
import kr.ac.nsu.hakbokgs.main.store.kkuihankki.main.KkuiListActivity
import kr.ac.nsu.hakbokgs.main.store.kkuihankki.main.MenuListActivity

class ChickenMenuChooseFragment : DialogFragment() {
    private val tag = "KkuiChickenMenuChoose"
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

        // ✅ Menu 객체 직접 받기
        val menu = arguments?.getParcelable<Menu>("menu")
        if (menu == null) {
            Log.w(tag, "전달된 메뉴 객체 없음")
            dismiss()
            return dialog
        }

        Log.i(tag, "받은 메뉴 ID: ${menu.documentId}, 이름: ${menu.id}")
        updateUI(menu) // ✅ 누락된 핵심 호출

        dialog.show()

        // ✅ 전달받은 Menu 객체
        val storeName = "꾸이한끼"
        val menuName = menu.id
        val menuPrice = menu.size?.basic?.price?.toIntOrNull() ?: 0
        var amount = 1

        val userId = FirebaseAuth.getInstance().currentUser?.email ?: "testId"

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
                store = storeName,
                menu = menuName.toString(),
                menuPrice = menuPrice,
                amount = amount,
            )

            FirebaseFirestore.getInstance()
                .collection("users").document(userId)
                .collection("basket")
                .add(basketItem)
                .addOnSuccessListener {
                    Toast.makeText(context, "장바구니에 담았습니다.", Toast.LENGTH_SHORT).show()

                    // ✅ 장바구니 담기 후 KkuiListActivity로 이동
                    val intent = Intent(requireContext(), KkuiListActivity::class.java)
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
        binding.menuIngredient.text = menu.ingredient?.joinToString(", ") ?: ""

        // ✅ 클래스 기반 접근
        val basicPrice = menu.size?.basic?.price
        binding.menuPrice.text = if (!basicPrice.isNullOrEmpty()) {
            "기본: ${basicPrice}원"
        } else {
            "가격 정보 없음"
        }

        // ✅ 이미지 로드
        menu.imagePath?.let { path ->
            val ref = FirebaseStorage.getInstance().reference.child(path)
            Glide.with(requireContext())
                .load(ref)
                .into(binding.menuImage)
        }
    }
}
