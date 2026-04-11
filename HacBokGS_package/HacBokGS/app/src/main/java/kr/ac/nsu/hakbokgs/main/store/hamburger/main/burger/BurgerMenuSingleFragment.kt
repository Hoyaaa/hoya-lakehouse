package kr.ac.nsu.hakbokgs.main.store.hamburger.main.burger

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
import kr.ac.nsu.hakbokgs.databinding.FragmentBurgerMenuSingleBinding
import kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu
import kr.ac.nsu.hakbokgs.main.store.cart.data.Option
import kr.ac.nsu.hakbokgs.main.store.hamburger.db.Menu
import kr.ac.nsu.hakbokgs.main.store.hamburger.main.MenuListActivity

class BurgerMenuSingleFragment : DialogFragment() {
    private val tag = "BurgerMenuSingleFragment"
    private var _binding: FragmentBurgerMenuSingleBinding? = null
    private val binding get() = _binding!!
    private var amount = 1

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        _binding = FragmentBurgerMenuSingleBinding.inflate(LayoutInflater.from(context))
        dialog.setContentView(binding.root)

        dialog.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        dialog.window?.setDimAmount(0.7f)
        isCancelable = true

        // ✅ 전달받은 Menu 객체
        val menu = arguments?.getSerializable("menu") as? Menu ?: return dialog
        updateUI(menu)
        val storeName = "버거운버거"
        val menuName = menu.id
        val singleMap = menu.size?.get("single") as? Map<*, *>
        val priceStr = singleMap?.get("price") as? String
        val menuPrice = priceStr?.toIntOrNull() ?: 0

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

        binding.btnAddCart.setOnClickListener {
            val optionList = mutableListOf<Option>()

            if (binding.checkCheese.isChecked) optionList.add(Option("치즈 추가", 500))
            if (binding.checkUnion.isChecked) optionList.add(Option("양파 제거", 0))
            if (binding.checkLettuce.isChecked) optionList.add(Option("양상추 제거", 0))
            if (binding.checkPickle.isChecked) optionList.add(Option("피클 제거", 0))
            if (binding.checkTomato.isChecked) optionList.add(Option("토마토 제거", 0))

            val basketItem = BasketMenu(
                store = storeName,
                menu = menuName,
                menuPrice = menuPrice,
                amount = amount,
                optionList = optionList
            )

            FirebaseFirestore.getInstance()
                .collection("users").document(userId)
                .collection("basket")
                .add(basketItem)
                .addOnSuccessListener {
                    Toast.makeText(context, "장바구니에 담았습니다.", Toast.LENGTH_SHORT).show()
                    dismiss()
                    // ✅ 장바구니 담기 후 BurgerListActivity로 이동
                    val intent = Intent(requireContext(), MenuListActivity::class.java)
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
        binding.burgerName.text = menu.id
        binding.burgerDescription.text = menu.description ?: "설명 없음"
        binding.burgerIngredient.text = menu.ingredient?.joinToString(", ") ?: "재료 정보 없음"

        val price = ((menu.size?.get("single") as? Map<*, *>)?.get("price") as? String)?.toIntOrNull() ?: 0
        binding.burgerPrice.text = "${String.format("%,d", price)}원"

        menu.imagePath?.let { path ->
            val storageRef = com.google.firebase.storage.FirebaseStorage.getInstance().reference.child(path)
            com.bumptech.glide.Glide.with(requireContext())
                .load(storageRef)
                .placeholder(kr.ac.nsu.hakbokgs.R.drawable.burger_image)
                .into(binding.burgerImage)
        }
    }}
