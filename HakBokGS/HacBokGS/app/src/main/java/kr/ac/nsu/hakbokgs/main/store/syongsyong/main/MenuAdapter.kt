package kr.ac.nsu.hakbokgs.main.store.syongsyong.main

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import kr.ac.nsu.hakbokgs.R
import kr.ac.nsu.hakbokgs.main.store.syongsyong.db.Menu

class MenuAdapter(private val onClick: (Menu) -> Unit) :
    ListAdapter<Menu, MenuAdapter.MenuViewHolder>(MenuDiffCallback) {

    class MenuViewHolder(itemView: View, val onClick: (Menu) -> Unit) :
        RecyclerView.ViewHolder(itemView) {
        private val menuTextView: TextView = itemView.findViewById(R.id.menu_name)
        private val menuImageView: ImageView = itemView.findViewById(R.id.menu_image)
        private var currentMenu: Menu? = null

        init {
            itemView.setOnClickListener {
                currentMenu?.let { onClick(it) }
            }
        }

        fun bind(menu: Menu) {
            currentMenu = menu
            menuTextView.text = menu.id

            if (!menu.imagePath.isNullOrEmpty()) {
                val storageRef = FirebaseStorage.getInstance().reference.child(menu.imagePath!!)
                Log.i("MenuAdapter", "이미지 로딩 시도: ${menu.imagePath}")
                Glide.with(menuImageView.context)
                    .load(storageRef)
                    .placeholder(R.drawable.ssyong_store_image)
                    .error(R.drawable.ssyong_store_image)
                    .into(menuImageView)
            } else {
                Log.w("MenuAdapter", "imagePath 없음, 기본 이미지 사용")
                menuImageView.setImageResource(R.drawable.ssyong_store_image)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu_ssyong, parent, false)
        return MenuViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val menu = getItem(position)
        holder.bind(menu)
    }


    object MenuDiffCallback : DiffUtil.ItemCallback<Menu>() {
        override fun areItemsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Menu, newItem: Menu): Boolean {
            return oldItem == newItem
        }
    }
}