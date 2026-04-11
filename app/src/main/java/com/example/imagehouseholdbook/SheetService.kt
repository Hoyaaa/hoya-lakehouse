package com.example.imagehouseholdbook

import android.content.Context
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.api.client.extensions.android.http.AndroidHttp
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.sheets.v4.Sheets
import com.google.api.services.sheets.v4.model.Spreadsheet
import com.google.api.services.sheets.v4.model.SpreadsheetProperties
import com.google.api.services.sheets.v4.model.ValueRange
import java.util.Collections

class SheetService(private val context: Context) {

    // 로그인된 사용자의 구글 계정 정보를 이용해 시트 서비스(API)를 준비합니다.
    private val mService: Sheets? by lazy {
        val account = GoogleSignIn.getLastSignedInAccount(context)
        if (account != null) {
            val credential = GoogleAccountCredential.usingOAuth2(
                context,
                Collections.singletonList("https://www.googleapis.com/auth/spreadsheets")
            )
            credential.selectedAccount = account.account

            Sheets.Builder(
                AndroidHttp.newCompatibleTransport(),
                JacksonFactory.getDefaultInstance(),
                credential
            )
                .setApplicationName("HouseholdBook")
                .build()
        } else {
            null
        }
    }

    /**
     * [6컬럼 전용] 영수증 데이터를 구글 시트에 이어쓰기(Append)
     *
     * 시트 컬럼 순서:
     * [가맹점 명, 구매 날짜, 상품명, 단가, 수령, 금액]
     *
     * headerInfo 키:
     * - "storeName"
     * - "purchaseDate" (yyyy-MM-dd)
     */
    fun appendReceiptData6Columns(
        fileName: String,
        items: List<ParsedReceiptItem>,
        headerInfo: Map<String, String>
    ) {
        if (mService == null) {
            Log.e("SheetService", "로그인이 필요하거나 권한이 없습니다.")
            return
        }

        try {
            // 1) 시트 ID 찾기 (없으면 새로 생성)
            val spreadsheetId = getOrCreateSpreadsheet6Columns(fileName)

            val storeName = headerInfo["storeName"] ?: ""
            val purchaseDate = headerInfo["purchaseDate"] ?: ""

            // 2) 추가할 데이터 행(Row) 생성
            val rows = mutableListOf<List<Any>>()
            for (item in items) {
                val row = listOf(
                    storeName,
                    purchaseDate,
                    item.productName,
                    item.unitPrice,
                    item.receivedQty,
                    item.totalAmount
                )

                rows.add(row)
            }

            val body = ValueRange().setValues(rows)

            // 3) 구글 시트 API로 맨 아래에 Append
            mService!!.spreadsheets().values()
                .append(spreadsheetId, "A1", body)
                .setValueInputOption("USER_ENTERED")
                .execute()

            Log.d("SheetService", "구글 시트 저장 성공! (6컬럼)")

        } catch (e: Exception) {
            Log.e("SheetService", "구글 시트 저장 실패(6컬럼): ${e.message}")
            e.printStackTrace()
        }
    }

    /**
     * [6컬럼 전용] 파일 이름으로 시트를 찾거나 새로 만들고,
     * 최초 생성 시 1행에 6컬럼 헤더를 작성합니다.
     */
    private fun getOrCreateSpreadsheet6Columns(title: String): String {
        val prefs = context.getSharedPreferences("SheetPrefs", Context.MODE_PRIVATE)

        // 1) 기존 생성된 시트 ID가 있으면 재사용
        val savedId = prefs.getString("sheet_id_6c_$title", null)
        if (savedId != null) return savedId

        // 2) 없으면 새로 생성
        val spreadsheet = Spreadsheet()
            .setProperties(SpreadsheetProperties().setTitle(title))
        val created = mService!!.spreadsheets().create(spreadsheet).execute()

        // 3) 헤더(6컬럼) 작성
        val headerRow = listOf("가맹점 명", "구매 날짜", "상품명", "단가", "수령", "금액")
        val body = ValueRange().setValues(listOf(headerRow))

        mService!!.spreadsheets().values()
            .append(created.spreadsheetId, "A1", body)
            .setValueInputOption("USER_ENTERED")
            .execute()

        // 4) 생성된 ID 저장
        prefs.edit().putString("sheet_id_6c_$title", created.spreadsheetId).apply()

        return created.spreadsheetId
    }
}
