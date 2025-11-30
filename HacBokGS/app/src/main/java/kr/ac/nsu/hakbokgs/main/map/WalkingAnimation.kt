package kr.ac.nsu.hakbokgs.main.map

import android.content.Context
import android.graphics.drawable.AnimationDrawable
import android.util.TypedValue
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.core.content.ContextCompat
import kr.ac.nsu.hakbokgs.R

class WalkingAnimation(private val context: Context) {

    fun createWalkingImageView(): ImageView {
        val imageView = ImageView(context)

        // 🔥 사이즈를 명시적으로 50dp x 50dp로 고정
        val sizeInDp = 20
        val sizeInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            sizeInDp.toFloat(),
            context.resources.displayMetrics
        ).toInt()

        imageView.layoutParams = FrameLayout.LayoutParams(sizeInPx, sizeInPx)

        val walkingAnimation = AnimationDrawable().apply {
            isOneShot = false // 무한 반복
            addFrame(ContextCompat.getDrawable(context, R.drawable.lode_people_r)!!, 200)
            addFrame(ContextCompat.getDrawable(context, R.drawable.lode_people_l)!!, 200)
        }

        imageView.setImageDrawable(walkingAnimation)
        walkingAnimation.start()

        return imageView
    }
}
