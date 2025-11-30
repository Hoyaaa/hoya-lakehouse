package kr.ac.nsu.hakbokgs.main.store.cart

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.ac.nsu.hakbokgs.R
import kr.ac.nsu.hakbokgs.main.store.cart.data.BasketMenu
import kr.ac.nsu.hakbokgs.main.store.cart.data.BasketViewModel


class BasketAdapter(
    private val context: Context,
    private val userId: String,
    private val basketViewModel: BasketViewModel,
    private val lifecycleOwner: LifecycleOwner
) : RecyclerView.Adapter<BasketAdapter.BasketViewHolder>() {
    private val tag = "BasketAdapter"

    private val storeList = mutableListOf<String>() // 가게 목록
    private var groupByStoreBasket: Map<String, List<BasketMenu>> = emptyMap() // 가게별 메뉴 목록

    /* 어댑터 초기화 */
    init {
        // 데이터 변경 시 자동 업데이트
        basketViewModel.filteredList.observe(lifecycleOwner) { filterMap ->
            groupByStoreBasket = filterMap
            storeList.clear()
            storeList.addAll(filterMap.keys)
            notifyDataSetChanged()
        }
    }

    inner class BasketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val storeName: TextView = itemView.findViewById(R.id.store_name_textview)
        val waitingAmount: TextView = itemView.findViewById(R.id.waiting_amount)
        val storeImage: ImageView = itemView.findViewById(R.id.store_image)
        val storeBasketRecyclerView: RecyclerView = itemView.findViewById(R.id.rv_store_order)

    }

    // RV 형태 설정
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BasketViewHolder {
        val view = LayoutInflater.from(context)
            .inflate(R.layout.item_basket, parent, false)
        return BasketViewHolder(view)
    }

    override fun onBindViewHolder(holder: BasketViewHolder, position: Int) {
        val store = storeList[position]
        holder.storeName.text = store

        // 이미지, 대기 순서
        when (store) {
            "버거운버거" -> {
                holder.storeImage.setImageResource(R.drawable.burger_store_image)
                holder.waitingAmount.text = "1팀 대기 중.."
            }

            "꾸이한끼" -> {
                holder.storeImage.setImageResource(R.drawable.kkui_store_image)
                holder.waitingAmount.text = "2팀 대기 중.."
            }

            "태산김치찜" -> {
                holder.storeImage.setImageResource(R.drawable.taesan_store_image)
                holder.waitingAmount.text = "3팀 대기 중.."
            }

            "쑝쑝돈가스" -> {
                holder.storeImage.setImageResource(R.drawable.syongsyong_store_image)
                holder.waitingAmount.text = "4팀 대기 중.."
            }

            else -> Log.e(tag, "존재하지 않는 store 데이터")
        }

        // 해당 가게의 메뉴 목록
        val basketMenuEachStore = groupByStoreBasket[store] ?: emptyList()

        /* 내부 recyclerView */
        // 사이즈 조절
        holder.storeBasketRecyclerView.setHasFixedSize(false)
        holder.storeBasketRecyclerView.isNestedScrollingEnabled = false
        // 형태 조절
        holder.storeBasketRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        // 내부 recyclerView 출력
        val basketStoreAdapter = BasketStoreAdapter(userId, context, basketViewModel)
        val menuAmount = basketMenuEachStore.sumOf { it.amount }
        Log.d(tag, "가게명: $store → 메뉴 수: $menuAmount")
        // 데이터 연결
        basketStoreAdapter.submitList(basketMenuEachStore)
        holder.storeBasketRecyclerView.adapter = basketStoreAdapter
        
        // 구분선
        if (holder.storeBasketRecyclerView.getItemDecorationCount() == 0) {
            val dividerItemDecoration = DividerItemDecoration(
                context, LinearLayoutManager.VERTICAL
            )
            holder.storeBasketRecyclerView.addItemDecoration(dividerItemDecoration)
        }
    }

    override fun getItemCount(): Int = storeList.size
}