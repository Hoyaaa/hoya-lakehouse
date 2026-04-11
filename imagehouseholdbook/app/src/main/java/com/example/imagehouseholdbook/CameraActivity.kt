package com.example.imagehouseholdbook

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope // 코루틴 사용을 위해 추가
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import com.google.mlkit.vision.documentscanner.GmsDocumentScannerOptions
import com.google.mlkit.vision.documentscanner.GmsDocumentScanning
import com.google.mlkit.vision.documentscanner.GmsDocumentScanningResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

class CameraActivity : AppCompatActivity() {

    private val TAG = "CameraActivity"

    private val db = Firebase.firestore
    private val auth = Firebase.auth
    private val storage = Firebase.storage

    // [추가] 구글 시트 서비스
    private lateinit var sheetService: SheetService

    private lateinit var fullScreenImageView: ImageView
    private lateinit var loadingProgressBar: ProgressBar
    private lateinit var loadingText: TextView
    private lateinit var captureButton: ImageButton

    private var jobListener: ListenerRegistration? = null
    private var currentJobId: String? = null
    private var hasNavigated = false

    private val scannerLauncher =
        registerForActivityResult(ActivityResultContracts.StartIntentSenderForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val scanningResult = GmsDocumentScanningResult.fromActivityResultIntent(result.data)
                val pages = scanningResult?.pages

                if (pages != null && pages.isNotEmpty()) {
                    val imageUri = pages[0].imageUri
                    fullScreenImageView.setImageURI(imageUri)

                    showLoadingUI(true, "고화질 스캔 완료. 업로드 중...")
                    uploadScannedImageToStorage(imageUri)
                } else {
                    Toast.makeText(this, "스캔 결과가 없습니다.", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } else {
                Toast.makeText(this, "스캔이 취소되었습니다.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupSimpleUI()

        // [추가] 시트 서비스 초기화
        sheetService = SheetService(this)

        startDocumentScanner()
    }

    override fun onStop() {
        super.onStop()
        detachJobListener()
    }

    // -------------------------
    // Logging helpers
    // -------------------------
    private fun stackTraceString(e: Exception): String {
        val sw = StringWriter()
        e.printStackTrace(PrintWriter(sw))
        return sw.toString()
    }

    private fun logStorageFailure(where: String, e: Exception) {
        val se = e as? StorageException
        if (se != null) {
            Log.e(
                TAG,
                "[$where] StorageException errorCode=${se.errorCode}, http=${se.httpResultCode}, msg=${se.message}\n${stackTraceString(e)}"
            )
        } else {
            Log.e(TAG, "[$where] Exception msg=${e.message}\n${stackTraceString(e)}")
        }
    }

    // -------------------------
    // Scanner
    // -------------------------
    private fun startDocumentScanner() {
        val options = GmsDocumentScannerOptions.Builder()
            .setGalleryImportAllowed(true)
            .setPageLimit(1)
            .setResultFormats(GmsDocumentScannerOptions.RESULT_FORMAT_JPEG)
            .setScannerMode(GmsDocumentScannerOptions.SCANNER_MODE_FULL)
            .build()

        GmsDocumentScanning.getClient(options)
            .getStartScanIntent(this)
            .addOnSuccessListener { intentSender ->
                scannerLauncher.launch(IntentSenderRequest.Builder(intentSender).build())
            }
            .addOnFailureListener { e: Exception ->
                Log.e(TAG, "Scanner start failed: ${e.message}", e)
                Toast.makeText(this, "스캐너 실행 실패", Toast.LENGTH_SHORT).show()
                finish()
            }
    }

    // -------------------------
    // Upload
    // -------------------------
    private fun uploadScannedImageToStorage(imageUri: Uri) {
        val user = auth.currentUser ?: run {
            showErrorAndFinish("로그인이 필요합니다.")
            return
        }

        val uid = user.uid
        val jobId = UUID.randomUUID().toString()
        currentJobId = jobId
        hasNavigated = false

        val ts = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val objectPath = "receipts/$uid/${ts}_$jobId.jpg"
        val fileRef = storage.reference.child(objectPath)

        Log.d(TAG, "Upload start uid=$uid jobId=$jobId path=$objectPath uri=$imageUri")

        val metadata = storageMetadata {
            contentType = "image/jpeg"
            setCustomMetadata("uid", uid)
            setCustomMetadata("jobId", jobId)
            setCustomMetadata("clientTime", System.currentTimeMillis().toString())
        }

        fileRef.putFile(imageUri, metadata)
            .addOnProgressListener { taskSnapshot: UploadTask.TaskSnapshot ->
                val total = taskSnapshot.totalByteCount
                val sent = taskSnapshot.bytesTransferred

                if (total > 0) {
                    val pct = (sent * 100 / total).toInt()
                    showLoadingUI(true, "업로드 중... ($pct%)")
                } else {
                    showLoadingUI(true, "업로드 중...")
                }
            }
            .addOnFailureListener { e: Exception ->
                logStorageFailure("putFile", e)
                showErrorAndFinish("업로드 실패: ${e.message ?: "unknown"}")
            }
            .addOnSuccessListener { snap: UploadTask.TaskSnapshot ->
                fileRef.downloadUrl
                    .addOnSuccessListener { downloadUrl ->
                        createPendingJobDocument(
                            jobId = jobId,
                            uid = uid,
                            storagePath = objectPath,
                            downloadUrl = downloadUrl.toString()
                        )
                    }
                    .addOnFailureListener { e: Exception ->
                        createPendingJobDocument(
                            jobId = jobId,
                            uid = uid,
                            storagePath = objectPath,
                            downloadUrl = null
                        )
                    }
            }
    }

    // -------------------------
    // Firestore job document
    // -------------------------
    private fun createPendingJobDocument(
        jobId: String,
        uid: String,
        storagePath: String,
        downloadUrl: String?
    ) {
        val jobRef = db.collection("receiptJobs").document(jobId)

        val data = hashMapOf<String, Any>(
            "jobId" to jobId,
            "uid" to uid,
            "storagePath" to storagePath,
            "status" to "pending",
            "createdAt" to FieldValue.serverTimestamp()
        )
        if (downloadUrl != null) data["downloadUrl"] = downloadUrl

        jobRef.set(data)
            .addOnSuccessListener {
                showLoadingUI(true, "업로드 완료. 분석 중...")
                attachJobListener(jobId)
            }
            .addOnFailureListener { e: Exception ->
                showErrorAndFinish("처리요청(job) 생성 실패: ${e.message ?: "unknown"}")
            }
    }

    /**
     * receiptJobs/{jobId} 실시간 구독:
     * - status == done  -> [추가] 구글 시트 저장 후 DetailActivity 이동
     */
    private fun attachJobListener(jobId: String) {
        detachJobListener()

        val jobRef = db.collection("receiptJobs").document(jobId)

        jobListener = jobRef.addSnapshotListener { snapshot, e ->
            if (e != null) {
                showErrorAndFinish("처리 상태 수신 실패: ${e.message}")
                return@addSnapshotListener
            }

            if (snapshot == null || !snapshot.exists()) return@addSnapshotListener

            val status = snapshot.getString("status") ?: return@addSnapshotListener
            Log.d(TAG, "Job status=$status jobId=$jobId")

            when (status) {
                "pending", "processing" -> {
                    showLoadingUI(true, "AI가 영수증을 분석 중입니다...")
                }

                "done" -> {
                    if (hasNavigated) return@addSnapshotListener
                    hasNavigated = true

                    // Firestore에 저장된 기본 정보 가져오기
                    val storeName = snapshot.getString("storeName") ?: ""
                    val purchaseDate = snapshot.getString("purchaseDate") ?: ""

                    val listAny = snapshot.get("docIdList") as? List<*>
                    val docIdList = ArrayList<String>(
                        listAny?.mapNotNull { it as? String } ?: emptyList()
                    )

                    if (docIdList.isEmpty()) {
                        showErrorAndFinish("분석은 완료됐지만 결과 문서 목록이 비어있습니다.")
                        return@addSnapshotListener
                    }

                    detachJobListener()

                    // [핵심 변경] 시트에 저장하고 이동하기 (비동기 처리)
                    saveToGoogleSheetAndNavigate(docIdList, storeName, purchaseDate)
                }

                "failed" -> {
                    val msg = snapshot.getString("errorMessage") ?: "분석 실패"
                    showErrorAndFinish(msg)
                }
            }
        }
    }

    private fun detachJobListener() {
        jobListener?.remove()
        jobListener = null
    }

    // -------------------------
    // Google Sheet & Navigation Logic
    // -------------------------

    // Firestore에서 아이템 세부 정보를 읽어와 시트에 저장
    private fun saveToGoogleSheetAndNavigate(
        docIdList: ArrayList<String>,
        storeName: String,
        purchaseDate: String
    ) {
        val userEmail = auth.currentUser?.email
        if (userEmail == null) {
            navigateToDetail(docIdList) // 이메일 없으면 그냥 이동
            return
        }

        showLoadingUI(true, "구글 시트에 기록 중...")

        // UI 스레드를 차단하지 않기 위해 IO 스레드 사용
        lifecycleScope.launch(Dispatchers.IO) {
            try {
                // 1. Firestore에서 개별 상품 정보(ParsedReceiptItem) 읽어오기
                val parsedItems = mutableListOf<ParsedReceiptItem>()
                val colRef = db.collection("user").document(userEmail).collection("imagehouseholdbook")

                for (docId in docIdList) {
                    val docSnap = colRef.document(docId).get().await() // await()으로 동기처럼 대기
                    if (docSnap.exists()) {
                        val productName = docSnap.getString("productName") ?: ""
                        val unitPrice = docSnap.getLong("unitPrice")?.toInt() ?: 0
                        val receivedQty = docSnap.getLong("receivedQty")?.toInt() ?: 1
                        val totalAmount = docSnap.getLong("totalAmount")?.toInt() ?: 0

                        parsedItems.add(ParsedReceiptItem(productName, unitPrice, receivedQty, totalAmount))
                    }
                }

                // 2. 구글 시트 서비스 호출 (네트워크 작업)
                val headerInfo = mapOf(
                    "storeName" to storeName,
                    "purchaseDate" to purchaseDate
                )

                // 파일명 "가계부"에 저장 (자동 생성 또는 Append)
                sheetService.appendReceiptData6Columns("가계부", parsedItems, headerInfo)

                Log.d(TAG, "구글 시트 저장 완료")

            } catch (e: Exception) {
                Log.e(TAG, "구글 시트 저장 중 오류 발생 (무시하고 진행): ${e.message}")
                // 시트 저장이 실패해도 앱 흐름은 끊기지 않도록 함
            }

            // 3. 메인 스레드로 돌아와서 화면 이동
            withContext(Dispatchers.Main) {
                navigateToDetail(docIdList)
            }
        }
    }

    // -------------------------
    // UI
    // -------------------------
    private fun navigateToDetail(docIdList: ArrayList<String>) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putStringArrayListExtra("DOC_ID_LIST", docIdList)
        intent.putExtra("CURRENT_INDEX", 0)
        startActivity(intent)
        finish()
    }

    private fun setupSimpleUI() {
        // 1. 전체 배경 (검은색)
        val frame = FrameLayout(this).apply {
            setBackgroundColor(android.graphics.Color.BLACK)
        }

        fullScreenImageView = ImageView(this).apply {
            scaleType = ImageView.ScaleType.FIT_CENTER
        }

        // 2. 로딩 동그라미 (ProgressBar) -> 회색(Light Gray)
        loadingProgressBar = ProgressBar(this).apply {
            visibility = View.GONE
            // [핵심] 동그라미 색상을 '밝은 회색'으로 설정
            indeterminateTintList = android.content.res.ColorStateList.valueOf(android.graphics.Color.BLACK)
        }

        // 3. 안내 멘트 (TextView) -> 검은 박스 + 흰색 굵은 글씨
        loadingText = TextView(this).apply {
            // [핵심] 글씨는 '흰색'
            setTextColor(android.graphics.Color.WHITE)
            textSize = 18f
            // [핵심] 글씨 굵게 (BOLD)
            typeface = android.graphics.Typeface.DEFAULT_BOLD
            visibility = View.GONE

            // [핵심] 배경은 '반투명 검은색' (#CC000000) -> 뒤가 살짝 비치는 검은 박스
            setBackgroundColor(android.graphics.Color.parseColor("#CC000000"))

            // 텍스트 주변 여백 (박스가 글씨에 딱 붙지 않게)
            setPadding(40, 30, 40, 30)

            // 위치 설정 (화면 중앙, 동그라미보다 아래쪽으로 내림)
            layoutParams = FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.CENTER
                topMargin = 250
            }
        }

        captureButton = ImageButton(this).apply { visibility = View.GONE }

        // 뷰를 화면에 추가하는 순서 (이미지 -> 로딩바 -> 텍스트)
        frame.addView(
            fullScreenImageView,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )

        frame.addView(
            loadingProgressBar,
            FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            ).apply { gravity = Gravity.CENTER }
        )

        frame.addView(loadingText)

        setContentView(frame)
    }

    private fun showLoadingUI(isLoading: Boolean, message: String) {
        val v = if (isLoading) View.VISIBLE else View.GONE
        loadingProgressBar.visibility = v
        loadingText.visibility = v
        loadingText.text = message
    }

    private fun showErrorAndFinish(msg: String) {
        showLoadingUI(false, "")
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
        finish()
    }
}