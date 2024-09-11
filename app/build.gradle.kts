import java.io.FileInputStream
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Properties

plugins {
    id("com.android.application")
}

val keystorePropertiesFile = file("signing.properties")
val keystoreProperties = Properties()
keystoreProperties.load(FileInputStream(keystorePropertiesFile))

android {
    namespace = "me.t3sl4.ondergrup"
    compileSdk = 34

    applicationVariants.all {
        outputs.all {
            val output = this as com.android.build.gradle.internal.api.BaseVariantOutputImpl
            val variantName = name
            val versionName = versionName
            val formattedDate = SimpleDateFormat("dd-MM-yyyy").format(Date())
            val fileExtension = output.outputFile.extension
            output.outputFileName = "OnderGrup ${variantName}_${formattedDate}_v${versionName}.${fileExtension}"
        }
    }

    val versionPropsFile = file("version.properties")
    val versionProps = Properties()

    if (versionPropsFile.canRead()) {
        versionProps.load(FileInputStream(versionPropsFile))

        val patch = versionProps["PATCH"].toString().toInt() + 1
        var minor = versionProps["MINOR"].toString().toInt()
        var major = versionProps["MAJOR"].toString().toInt()
        val realVersionCode = versionProps["VERSION_CODE"].toString().toInt() + 1

        if (patch == 100) {
            minor += 1
            versionProps["PATCH"] = "0"
        } else {
            versionProps["PATCH"] = patch.toString()
        }

        if (minor == 10) {
            major += 1
            minor = 0
        }

        versionProps["MINOR"] = minor.toString()
        versionProps["MAJOR"] = major.toString()
        versionProps["VERSION_CODE"] = realVersionCode.toString()
        versionProps.store(versionPropsFile.writer(), null)

        defaultConfig {
            applicationId = "me.t3sl4.ondergrup"
            minSdk = 29
            targetSdk = 34
            versionCode = realVersionCode
            versionName = "$major.$minor.$patch($versionCode)"
            resourceConfigurations += listOf("en", "tr")
            renderscriptTargetApi = 19
            renderscriptSupportModeEnabled = true

            buildConfigField("String", "BASE_URL", "\"https://ondergrup.hidirektor.com.tr\"")
            buildConfigField("String", "ONESIGNAL_APP_ID", "\"9cbe059f-be21-4512-a1f6-5a9fc0f77506\"")

            testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        }

        signingConfigs {
            create("key0") {
                keyAlias = keystoreProperties["keyAlias"] as String
                keyPassword = keystoreProperties["keyPassword"] as String
                storeFile = file(keystoreProperties["storeFile"] as String)
                storePassword = keystoreProperties["storePassword"] as String
            }
        }
    } else {
        throw GradleException("Could not read version.properties!")
    }

    buildFeatures {
        viewBinding = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            buildFeatures.buildConfig = true

            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-Xlint:deprecation")
    options.isWarnings = true
}

dependencies {

    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")

    //Custom
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("com.android.volley:volley:1.2.1")
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    implementation("com.jakewharton.retrofit:retrofit2-rxjava2-adapter:1.0.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.1")
    implementation("com.github.YarikSOffice:lingver:1.3.0")
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("com.github.sigma1326:NiceSwitch:1.0")

    implementation("com.onesignal:OneSignal:[5.0.0, 5.99.99]")

    implementation("io.github.chaosleung:pinview:1.4.4")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.google.zxing:core:3.4.1")
    implementation("com.github.Z-P-J:ZCheckBox:1.0.0")

    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
}