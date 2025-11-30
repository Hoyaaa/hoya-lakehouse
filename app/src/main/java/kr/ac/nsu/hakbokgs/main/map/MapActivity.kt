package kr.ac.nsu.hakbokgs.main.map

import android.animation.ValueAnimator
import android.content.Intent
import android.graphics.Path
import android.graphics.PointF
import android.graphics.PathMeasure
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.fragment.app.FragmentManager
import kr.ac.nsu.hakbokgs.R
import kr.ac.nsu.hakbokgs.main.MainActivity
import kr.ac.nsu.hakbokgs.main.chat.ChatBoardHomeActivity
import kr.ac.nsu.hakbokgs.main.mypage.MypageActivity
import kr.ac.nsu.hakbokgs.main.store.order.OrderHistoryActivity
import kotlin.math.atan2

class MapActivity : AppCompatActivity() {

    private lateinit var mapLayer: FrameLayout
    private lateinit var routeLineView: RouteLineView
    private lateinit var movingImageView: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        mapLayer = findViewById(R.id.map_layer)
        routeLineView = findViewById(R.id.route_line_view)

        // 걷는 사람 만들기
        val walkingAnimation = WalkingAnimation(this)
        movingImageView = walkingAnimation.createWalkingImageView()
        movingImageView.visibility = View.INVISIBLE // 처음엔 숨김

        // 🔥 걷는 사람 크기를 50dp로 고정해서 추가
        val sizeInDp = 20
        val sizeInPx = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            sizeInDp.toFloat(),
            resources.displayMetrics
        ).toInt()

        mapLayer.addView(movingImageView, FrameLayout.LayoutParams(sizeInPx, sizeInPx))

        val buildingIds = listOf(
            R.id.map_book,
            R.id.map_art,
            R.id.map_bread,
            R.id.map_cafe,
            R.id.map_clean,
            R.id.map_cu,
            R.id.map_popup,
            R.id.map_post,
            R.id.map_print,
            R.id.map_print2,
            R.id.map_hair
        )

        findViewById<LinearLayout>(R.id.main_home).setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.main_chat).setOnClickListener {
            startActivity(Intent(this, ChatBoardHomeActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.main_mypage).setOnClickListener {
            startActivity(Intent(this, MypageActivity::class.java))
        }
        findViewById<LinearLayout>(R.id.main_list).setOnClickListener {
            startActivity(Intent(this, OrderHistoryActivity::class.java))
        }

        buildingIds.forEach { id ->
            findViewById<View>(id).setOnClickListener {
                drawPathToBuilding(id)
            }
        }
    }

    private fun drawPathToBuilding(buildingId: Int) {
        val stairs = findViewById<View>(R.id.map_stairs)
        val building = findViewById<View>(buildingId)

        val p1 = getCenterPoint(stairs)

        val route = when (buildingId) {

            R.id.map_hair -> {
                val p2 = PointF(p1.x, p1.y - 20f)
                val p3 = PointF(p2.x + 20f, p2.y)
                val p4 = PointF(p3.x, p3.y - 1100f)
                val p5 = PointF(p4.x - 20f, p4.y)
                listOf(p1, p2, p3, p4, p5)
            }

            R.id.map_cu -> {
                val p2 = PointF(p1.x, p1.y - 220f)
                val p3 = PointF(p2.x + 170f, p2.y)
                val p4 = PointF(p3.x, p3.y - 150f)
                val p5 = PointF(p4.x - 200f, p4.y)
                listOf(p1, p2, p3, p4, p5)
            }

            R.id.map_art -> {
                val p2 = PointF(p1.x, p1.y - 220f)
                val p3 = PointF(p2.x + 40f, p2.y)
                val p4 = PointF(p3.x, p3.y - 1250f)
                val p5 = PointF(p4.x + 200f, p4.y)
                val p6 = PointF(p5.x, p5.y - 70f)
                listOf(p1, p2, p3, p4, p5, p6)
            }

            R.id.map_print -> {
                val p2 = PointF(p1.x, p1.y - 220f)
                val p3 = PointF(p2.x + 40f, p2.y)
                val p4 = PointF(p3.x, p3.y - 1250f)
                val p5 = PointF(p4.x + 530f, p4.y)
                val p6 = PointF(p5.x, p5.y - 70f)
                listOf(p1, p2, p3, p4, p5, p6)
            }

            R.id.map_bread -> {
                val p2 = PointF(p1.x, p1.y - 220f)
                val p3 = PointF(p2.x + 40f, p2.y)
                val p4 = PointF(p3.x, p3.y - 1250f)
                val p5 = PointF(p4.x + 750f, p4.y)
                listOf(p1, p2, p3, p4, p5)
            }

            R.id.map_popup -> {
                val p2 = PointF(p1.x, p1.y - 220f)
                val p3 = PointF(p2.x + 40f, p2.y)
                val p4 = PointF(p3.x, p3.y - 1250f)
                val p5 = PointF(p4.x + 200f, p4.y)
                val p6 = PointF(p5.x, p5.y + 70f)
                listOf(p1, p2, p3, p4, p5, p6)
            }

            R.id.map_cafe -> {
                val p2 = PointF(p1.x, p1.y - 220f)
                val p3 = PointF(p2.x + 180f, p2.y)
                val p4 = PointF(p3.x, p3.y - 250f)
                listOf(p1, p2, p3, p4)
            }

            R.id.map_post -> {
                val p2 = PointF(p1.x, p1.y - 220f)
                val p3 = PointF(p2.x + 180f, p2.y)
                val p4 = PointF(p3.x, p3.y - 200f)
                val p5 = PointF(p4.x + 390f, p4.y)
                val p6 = PointF(p5.x, p5.y - 50f)
                listOf(p1, p2, p3, p4, p5, p6)
            }

            R.id.map_book -> {
                val p2 = PointF(p1.x, p1.y - 220f)
                val p3 = PointF(p2.x + 180f, p2.y)
                val p4 = PointF(p3.x, p3.y - 200f)
                val p5 = PointF(p4.x + 510f, p4.y)
                val p6 = PointF(p5.x, p5.y + 50f)
                listOf(p1, p2, p3, p4, p5, p6)
            }

            R.id.map_clean -> {
                val p2 = PointF(p1.x, p1.y - 220f)
                val p3 = PointF(p2.x + 180f, p2.y)
                val p4 = PointF(p3.x, p3.y - 200f)
                val p5 = PointF(p4.x + 220f, p4.y)
                val p6 = PointF(p5.x, p5.y + 50f)
                listOf(p1, p2, p3, p4, p5, p6)
            }

            R.id.map_print2 -> {
                val p2 = PointF(p1.x, p1.y - 220f)
                val p3 = PointF(p2.x + 180f, p2.y)
                val p4 = PointF(p3.x, p3.y - 200f)
                val p5 = PointF(p4.x + 580f, p4.y)
                val p6 = PointF(p5.x, p5.y -150f)
                val p7 =PointF(p6.x + 100f, p6.y)
                listOf(p1, p2, p3, p4, p5, p6,p7)
            }

            else -> {
                val p2 = PointF(p1.x, p1.y + 100f)
                val p3 = PointF(building.x + building.width / 2f, p2.y)
                val p4 = getCenterPoint(building)
                listOf(p1, p2, p3, p4)
            }
        }

        startDrawingRouteAndWalking(route, buildingId)
    }

    private fun startDrawingRouteAndWalking(routePoints: List<PointF>, buildingId: Int) {
        // 1. 선 세팅만 먼저 (drawRoute 안에서 애니메이션 없음)
        routeLineView.drawRoute(routePoints)

        // 2. 경로 생성
        val path = Path().apply {
            moveTo(routePoints[0].x, routePoints[0].y)
            for (i in 1 until routePoints.size) {
                lineTo(routePoints[i].x, routePoints[i].y)
            }
        }

        val pathMeasure = PathMeasure(path, false)

        val pos = FloatArray(2)
        val tan = FloatArray(2)

        val totalDistance = pathMeasure.length

        val animator = ValueAnimator.ofFloat(0f, totalDistance).apply {
            duration = 3000L // 선 + 사람 동시에 3초
            addUpdateListener { animation ->
                val distance = animation.animatedValue as Float
                val fraction = distance / totalDistance // 전체 거리 대비 비율 계산

                pathMeasure.getPosTan(distance, pos, tan)

                movingImageView.x = pos[0] - movingImageView.width / 2
                movingImageView.y = pos[1] - movingImageView.height / 2

                val degree = (Math.toDegrees(atan2(tan[1].toDouble(), tan[0].toDouble()))).toFloat()
                movingImageView.rotation = degree

                // 🔥 이동한 만큼 선도 같이 그려주기
                routeLineView.updateProgress(fraction)
            }
            doOnEnd {
                movingImageView.visibility = View.INVISIBLE
                showPopup(getPopupLayout(buildingId))
            }
        }

        movingImageView.visibility = View.VISIBLE
        animator.start()
    }


    private fun getCenterPoint(view: View): PointF {
        val x = view.x + view.width / 2f
        val y = view.y + view.height / 2f
        return PointF(x, y)
    }

    private fun getPopupLayout(buildingId: Int): Int {
        return when (buildingId) {
            R.id.map_art -> R.layout.fragment_map_art
            R.id.map_book -> R.layout.fragment_map_book
            R.id.map_bread -> R.layout.fragment_map_bread
            R.id.map_cafe -> R.layout.fragment_map_cafe
            R.id.map_clean -> R.layout.fragment_map_clean
            R.id.map_cu -> R.layout.fragment_map_cu
            R.id.map_popup -> R.layout.fragment_map_popup
            R.id.map_post -> R.layout.fragment_map_post
            R.id.map_print, R.id.map_print2 -> R.layout.fragment_map_print
            R.id.map_hair -> R.layout.fragment_map_hair
            else -> -1
        }
    }

    private fun showPopup(layoutId: Int) {
        val fragmentManager: FragmentManager = supportFragmentManager
        val dialogFragment = GenericDialogFragment.newInstance(layoutId)
        dialogFragment.show(fragmentManager, "Popup")
    }
}
