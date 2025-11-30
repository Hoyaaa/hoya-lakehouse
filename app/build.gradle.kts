plugins {
    id("com.google.gms.google-services") // Firebase 서비스 사용
    alias(libs.plugins.android.application) // 안드로이드 애플리케이션 플러그인
    alias(libs.plugins.kotlin.android) // Kotlin 안드로이드 플러그인
    alias(libs.plugins.kotlin.compose) // Compose 플러그인
    id("kotlin-parcelize")
    id("kotlin-kapt") // 추가

}

android {
    namespace = "kr.ac.nsu.hakbokgs" // ✅ 변경
    compileSdk = 34

    defaultConfig {
        applicationId = "kr.ac.nsu.hakbokgs" // ✅ 변경
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
        }

    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        // 뷰바인딩 기능 추가
        viewBinding = true
        // 데이터바인딩 기능 추가
        dataBinding = true
    }
}

dependencies {
    // ✅ Jetpack 기본 구성
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("androidx.core:core-ktx:1.10.1") // 기준 버전 유지
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("androidx.appcompat:appcompat-resources:1.7.0")
    implementation("androidx.constraintlayout:constraintlayout:2.2.0-beta01")
    implementation("androidx.fragment:fragment-ktx:1.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.annotation:annotation:1.9.1")

    // ✅ Jetpack Compose 관련
    implementation("androidx.compose.ui:ui:1.7.8")
    implementation("androidx.compose.material3:material3:1.3.1")

    // ✅ AndroidX UI 컴포넌트
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("com.google.android.material:material:1.10.0")

    // ✅ 이미지 로딩 (Glide)
    implementation("com.github.bumptech.glide:glide:4.15.1")
    implementation(libs.androidx.gridlayout)
    kapt("com.github.bumptech.glide:compiler:4.15.1") // ✅ 수정된 부분


    // ✅ HTML 파싱 (Jsoup)
    implementation("org.jsoup:jsoup:1.15.3")

    // ✅ Emoji 지원
    implementation("androidx.emoji2:emoji2:1.4.0")
    implementation("androidx.emoji2:emoji2-views-helper:1.4.0")

    // ✅ Firebase 플랫폼 (BOM & 모듈별)
    implementation(platform("com.google.firebase:firebase-bom:33.10.0"))
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-analytics")
    implementation("com.google.firebase:firebase-storage")

    // ✅ Google Play 서비스 (Google 로그인)
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // ✅ Firebase UI - Storage 연동
    implementation("com.firebaseui:firebase-ui-storage:8.0.2")// ✅ FirebaseImageLoader 사용 시 필수

    // ✅ 테스트 관련
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // ✅ Jetpack Compose 테스트
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation ("androidx.gridlayout:gridlayout:1.0.0")
}


