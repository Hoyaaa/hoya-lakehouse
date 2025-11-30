/* ListAdapter : 효율 좋은 리사이클러 뷰 */
/* item_basket.xml의 RV 연결 */
package kr.ac.nsu.hakbokgs.main.store.cart

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kr.ac.nsu.hakbokgs.R
import kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu
import kr.ac.nsu.hakbokgs.main.store.cart.data.BasketViewModel
import java.text.DecimalFormat

class BasketStoreAdapter(
    private val userId: String,
    private val context: Context,
    private val basketViewModel: BasketViewModel,
) : ListAdapter<BasketMenu, BasketStoreAdapter.BasketStoreViewHolder>(BasketMenuDiffCallback()) {

    companion object {
        var totalPriceAll: Int = 0
    }

    inner class BasketStoreViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val menu: TextView = itemView.findViewById(R.id.menu_name_textview)
        val menuPrice: TextView = itemView.findViewById(R.id.menu_price_textview)
        val totalPrice: TextView = itemView.findViewById(R.id.total_price_textview)
        val amount: TextView = itemView.findViewById(R.id.amount_textview)
        val optionContainer: LinearLayout = itemView.findViewById(R.id.option_container)
        val deleteButton: ImageView = itemView.findViewById(R.id.btn_delete)
        val plusButton: ImageView = itemView.findViewById(R.id.btn_plus)
        val minusButton: ImageView = itemView.findViewById(R.id.btn_minus)
        val optionText: TextView = itemView.findViewById(R.id.add_option)
    }

    // 형태 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketStoreViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_basket_menu_each_store, parent, false)
        return BasketStoreViewHolder(view)
    }

    override fun onBindViewHolder(holder: BasketStoreViewHolder, position: Int) {
        val item = getItem(position)
        val options = item.optionList
        val formatter = DecimalFormat("#,###")

        holder.menu.text = item.menu
        holder.amount.text = "${item.amount}개"

        var itemTotalPrice = item.menuPrice
        options?.forEach { option ->
            itemTotalPrice += option.optionPrice
        }
        itemTotalPrice *= item.amount
        totalPriceAll += itemTotalPrice

        holder.totalPrice.text = "${formatter.format(itemTotalPrice)}원"
        holder.menuPrice.text = "${formatter.format(item.menuPrice)}원"

        // 옵션 정보 세팅
        val optionContainer = holder.optionContainer
        optionContainer.removeAllViews()

        if(options!=null&&options.isNotEmpty()){
            holder.optionText.visibility = View.VISIBLE // 옵션 있으면 '-옵션' 표시
            options?.forEach { option ->
                val optionView = LayoutInflater.from(optionContainer.context)
                    .inflate(R.layout.item_option, optionContainer, false)

                val optionNameTextView = optionView.findViewById<TextView>(R.id.option_name_textview)
                val optionPriceTextView = optionView.findViewById<TextView>(R.id.option_price_textview)

                optionNameTextView.text = " ↳ ${option.optionName}"
                optionPriceTextView.text = "${formatter.format(option.optionPrice)}원"

                optionContainer.addView(optionView)
            }
        }else{
            holder.optionText.visibility = View.GONE // 옵션 없으면 '-옵션' 숨기기
        }

        // 수량 증가
        holder.plusButton.setOnClickListener {
            val newAmount = item.amount + 1
            basketViewModel.updateAmountInFirestore(userId, item, newAmount)
        }

        // 수량 감소
        holder.minusButton.setOnClickListener {
            if (item.amount >= 2) {
                val newAmount = item.amount - 1
                basketViewModel.updateAmountInFirestore(userId, item, newAmount)
            } else {
                Toast.makeText(context, "수량은 1개 이상 가능합니다.", Toast.LENGTH_LONG).show()
            }
        }

        // 삭제
        holder.deleteButton.setOnClickListener {
            AlertDialog.Builder(context)
                .setMessage("정말 삭제하시겠습니까?")
                .setPositiveButton("삭제") { _, _ ->
                    basketViewModel.deleteItemFromFirestore(userId, item)
                    Toast.makeText(context, "삭제되었습니다.", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("취소", null)
                .show()
        }
    }

    // 데이터 갱신
    class BasketMenuDiffCallback : DiffUtil.ItemCallback<BasketMenu>() {
        override fun areItemsTheSame(oldItem: BasketMenu, newItem: BasketMenu): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: BasketMenu, newItem: BasketMenu): Boolean {
            return oldItem == newItem
        }
    }

}