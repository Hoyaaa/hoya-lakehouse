package com.example.imagehouseholdbook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.text.DecimalFormat

class HouseholdAdapter(
    private var groupList: List<HouseholdGroup>,
    private val onItemClick: (HouseholdGroup) -> Unit
) : RecyclerView.Adapter<HouseholdAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val date: TextView = view.findViewById(R.id.tvItemDate)           // 기존: editpurchasedate
        val title: TextView = view.findViewById(R.id.tvItemProductName)   // 기존: editproductname
        val storeName: TextView = view.findViewById(R.id.tvItemStoreName) // 기존: editpurchaseplace → 가맹점
        val totalAmount: TextView = view.findViewById(R.id.tvItemTotalAmount)  // 기존: editprice
        val panelInfo: TextView = view.findViewById(R.id.tvItemPanelId)   // 기존: editpanelid
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_main_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = groupList[position]

        holder.date.text = item.date
        holder.storeName.text = item.place  // panelId 또는 storeName 대표
        holder.title.text = item.representativeName
        holder.totalAmount.text = "총 ${DecimalFormat("#,###").format(item.totalPrice)}원"
        holder.panelInfo.text = "ID: ${item.place} (${item.itemDocIds.size}건)"

        holder.itemView.setOnClickListener {
            onItemClick(item)
        }
    }

    override fun getItemCount(): Int = groupList.size

    fun updateData(newGroups: List<HouseholdGroup>) {
        groupList = newGroups
        notifyDataSetChanged()
    }
}
