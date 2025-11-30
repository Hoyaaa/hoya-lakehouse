package kr.ac.nsu.hakbokgs.main.store.popup

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import kr.ac.nsu.hakbokgs.R

object CookingStatePopup {

    fun showCookingDonePopup(context: Context, storeName: String, menuName: String, orderNumber: String) {
        val inflater = LayoutInflater.from(context)
        val view = inflater.inflate(R.layout.popup_order_complete, null)

        val storeNameText = view.findViewById<TextView>(R.id.popup_store_name)
        val menuNameText = view.findViewById<TextView>(R.id.popup_menu_summary)
        val orderNumberText = view.findViewById<TextView>(R.id.popup_order_number)

        storeNameText.text = storeName
        menuNameText.text = menuName
        orderNumberText.text = "주문번호: $orderNumber"

        val okButton = view.findViewById<TextView>(R.id.popup_ok_button)

        val dialog = AlertDialog.Builder(context)
            .setView(view)
            .setCancelable(false)
            .create()

        okButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }
}
