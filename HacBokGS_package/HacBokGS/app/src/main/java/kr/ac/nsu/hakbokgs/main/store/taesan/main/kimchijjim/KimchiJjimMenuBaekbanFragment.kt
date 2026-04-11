package kr.ac.nsu.hakbokgs.main.store.taesan.main.kimchijjim

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kr.ac.nsu.hakbokgs.databinding.FragmentTaesanMenuChooseSizeBinding
import kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu
import kr.ac.nsu.hakbokgs.main.store.syongsyong.main.SsyongListActivity
import kr.ac.nsu.hakbokgs.main.store.taesan.db.Menu
import kr.ac.nsu.hakbokgs.main.store.taesan.main.TaesanListActivity

class KimchiJjimMenuBaekbanFragment : DialogFragment() {
    private val TAG = "TaesanMenuSizeChooseFragment"
    private var _binding: FragmentTaesanMenuChooseSizeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        _binding = FragmentTaesanMenuChooseSizeBinding.inflate(layoutInflater)
        dialog.setContentView(binding.root)

        // 다이얼로그 화면 조정
        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setDimAmount(0.7f)
        isCancelable = true
        dialog.setCancelable(true)

        // Menu 객체 받기
        val menu = arguments?.getParcelable<Menu>("menu")
        if (menu == null) {
            Log.w(TAG, "전달된 메뉴 객체 없음")
            dismiss()
            return dialog
        }

        Log.i(TAG, "받은 메뉴 ID: ${menu.documentId}, 이름: ${menu.id}")
        updateUI(menu)

        dialog.show()

        val userId = FirebaseAuth.getInstance().currentUser?.email ?: ""

        // 전달받은 Menu 객체
        val storeName = "태산김치찜"
        val menuName = menu.id
        val menuPrice = menu.size?.baekban?.price?.toIntOrNull() ?: 0
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
            }
            Toast.makeText(context, "수량은 1개 이상 가능합니다.", Toast.LENGTH_SHORT).show()
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

                    // 장바구니 담기 후 SsyongListActivity로 이동
                    val intent = Intent(requireContext(), TaesanListActivity::class.java)
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
        binding.kimchijjimName.text = menu.id
        binding.kimchijjimDescription.text = menu.description
        binding.kimchijjimIngredient.text = menu.ingredient?.joinToString(",") ?: ""


        // 가격 처리
        val baekbanPrice = menu.size?.baekban?.price
        binding.menuPrice.text = if (!baekbanPrice.isNullOrEmpty()) {
            "백반: ${baekbanPrice}원"
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