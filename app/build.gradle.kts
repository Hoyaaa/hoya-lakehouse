plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
}

android {
    namespace = "com.example.imagehouseholdbook"
    compileSdk = 34

    // [수정 1] signingConfigs 중괄호 닫기 오류 수정 완료
    signingConfigs {
        create("release") {
            storeFile = file("E:\\key\\scan.jks") // 경로 확인
            storePassword = "000000"
            keyAlias = "key0"
            keyPassword = "000000"
        }
    }

    defaultConfig {
        applicationId = "com.example.imagehouseholdbook"
        minSdk = 30
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            // [중요] 릴리즈 빌드 시 위에서 설정한 키스토어 서명 적용
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
        viewBinding = true
    }
}

dependencies {
    // AndroidX & Compose 기본
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)

    // UI 구성요소 (중복 제거 및 최신화)
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation(libs.androidx.exifinterface)

    // Firebase & Google Login (BOM 사용으로 버전 관리 일원화)
    implementation(platform("com.google.firebase:firebase-bom:32.7.0"))
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // [중요] 구글 로그인 라이브러리
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // CameraX (안정적인 1.3.1 버전으로 통일)
    implementation("androidx.camera:camera-core:1.3.1")
    implementation("androidx.camera:camera-camera2:1.3.1")
    implementation("androidx.camera:camera-lifecycle:1.3.1")
    implementation("androidx.camera:camera-video:1.3.1")
    implementation("androidx.camera:camera-view:1.3.1")
    implementation("androidx.camera:camera-extensions:1.3.1")

    // Retrofit (Naver OCR 통신용)
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.0")

    // Google API Client (Sheets API 등)
    implementation("com.google.api-client:google-api-client-android:1.26.0") {
        exclude(group = "org.apache.httpcomponents")
    }
    implementation("com.google.apis:google-api-services-sheets:v4-rev581-1.25.0") {
        exclude(group = "org.apache.httpcomponents")
    }

    // Google ML Kit Document Scanner (문서 스캐너)
    implementation("com.google.android.gms:play-services-mlkit-document-scanner:16.0.0-beta1")

    // [삭제됨] Gemini 라이브러리는 제거하였습니다. (Naver OCR 전용 사용)

    // Test
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)

    implementation(platform("com.google.firebase:firebase-bom:33.+" )) // 예시(프로젝트에 맞춰 고정 권장)

    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore-ktx")

    // 이 줄이 빠져서 storage / storageMetadata 가 unresolved 된 가능성이 큼
    implementation("com.google.firebase:firebase-storage-ktx")
}