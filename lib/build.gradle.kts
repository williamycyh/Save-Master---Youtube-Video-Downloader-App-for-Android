plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.lib"
    compileSdk = 35

    defaultConfig {
//        applicationId = "com.example.lib"
        minSdk = 24
        targetSdk = 35
//        versionCode = 1
//        versionName = "1.0"

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
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
}

dependencies {

    //cookies
//    implementation ("com.github.franmontiel:PersistentCookieJar:v1.0.1")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //rest api retrofit
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")

    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.12.0"))
    // define any required OkHttp artifacts without version
    implementation ("com.squareup.okhttp3:logging-interceptor")
    implementation ("com.squareup.okhttp3:okhttp")
    implementation ("com.squareup.okhttp3:okhttp-urlconnection")

    implementation("org.jsoup:jsoup:1.13.1")
    implementation("androidx.appcompat:appcompat:1.6.1")
//    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("org.jetbrains:annotations:15.0")

    //cookies
//    implementation("com.github.franmontiel:PersistentCookieJar:v1.0.1")
    implementation(project(mapOf("path" to ":okhttp3-persistent-cookiejar")))

    //Network call library
//    implementation("com.amitshekhar.android:android-networking:1.0.2")

//    implementation ("androidx.webkit:webkit:1.10.0")
//    implementation ("androidx.window:window-java:1.3.0-alpha02")
//    implementation ("androidx.window:window:1.3.0-alpha02")

//    implementation ("org.apache.commons:commons-text:1.11.0")
//    implementation ("commons-io:commons-io:2.15.1")
    implementation ("org.apache.commons:commons-text:1.9")
//    implementation(project(mapOf("path" to ":okhttp3-persistent-cookiejar")))
}