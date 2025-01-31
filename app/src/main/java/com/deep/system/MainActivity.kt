package com.deep.system

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageInfo
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.deep.system.databinding.ActivityMainBinding
import com.google.android.gms.common.GoogleApiAvailability
import java.io.BufferedReader
import java.io.File
import java.io.InputStreamReader
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }


        binding.switchCompat1.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result1.setTextColor(resources.getColor(R.color.greenColor))
                if(testWriteSecureSettings()){
                    binding.result1.text = "Possible"
                    binding.result1.setTextColor(resources.getColor(R.color.greenColor))
                    Toast.makeText(this,"Successfully changed Secure Settings",Toast.LENGTH_LONG).show()
                } else {
                    binding.result1.text = "Not Possible"
                    binding.result1.setTextColor(resources.getColor(R.color.redColor))
                    Toast.makeText(this,"Permission denied",Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result1.text = "--"
                binding.result1.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat2.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result2.setTextColor(resources.getColor(R.color.greenColor))
                if(installApkSilently()){
                    binding.result2.text = "Possible"
                    binding.result2.setTextColor(resources.getColor(R.color.greenColor))
                    Toast.makeText(this, "Silent installation triggered", Toast.LENGTH_LONG).show()
                } else {
                    binding.result2.text = "Not Possible"
                    binding.result2.setTextColor(resources.getColor(R.color.redColor))
                    Toast.makeText(this, "Failed to install APK silently", Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result2.text = "--"
                binding.result2.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat3.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result3.setTextColor(resources.getColor(R.color.greenColor))
                if(isSystemApp()){
                    binding.result3.text = "Possible"
                    binding.result3.setTextColor(resources.getColor(R.color.greenColor))
                    Toast.makeText(this,"Success checking system app",Toast.LENGTH_LONG).show()
                } else {
                    binding.result3.text = "Not Possible"
                    binding.result3.setTextColor(resources.getColor(R.color.redColor))
                    Toast.makeText(this,"Error checking system app",Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result3.text = "--"
                binding.result3.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat4.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result4.setTextColor(resources.getColor(R.color.greenColor))
                if(testInternalApis()){
                    binding.result4.text = "Possible"
                    binding.result4.setTextColor(resources.getColor(R.color.greenColor))
                    Toast.makeText(this,"Access to internal API succeeded",Toast.LENGTH_LONG).show()
                } else {
                    binding.result4.text = "Not Possible"
                    binding.result4.setTextColor(resources.getColor(R.color.redColor))
                    Toast.makeText(this,"Access to internal API failed",Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result4.text = "--"
                binding.result4.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat5.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result5.setTextColor(resources.getColor(R.color.greenColor))
                if(isUpgradingExistingSystemApp("com.deep.system")){
                    binding.result5.text = "Possible"
                    binding.result5.setTextColor(resources.getColor(R.color.greenColor))
                    Toast.makeText(this,"Systep App Upgrade Success",Toast.LENGTH_LONG).show()
                } else {
                    binding.result5.text = "Not Possible"
                    binding.result5.setTextColor(resources.getColor(R.color.redColor))
                    Toast.makeText(this,"System App Upgrade Failed",Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result5.text = "--"
                binding.result5.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat6.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result6.setTextColor(resources.getColor(R.color.redColor))
                if(testRootAccess()){
                    binding.result6.text = "Possible"
                    binding.result6.setTextColor(resources.getColor(R.color.greenColor))
                    Toast.makeText(this,"Root access available!",Toast.LENGTH_LONG).show()
                } else {
                    binding.result6.text = "Not Possible"
                    binding.result6.setTextColor(resources.getColor(R.color.redColor))
                    Toast.makeText(this,"Root access denied.",Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result6.text = "--"
                binding.result6.setTextColor(resources.getColor(R.color.black))
            }

        }

        /////////////////////////
        binding.switchCompat7.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result7.text = "Not Possible"
                binding.result7.setTextColor(resources.getColor(R.color.redColor))
                testModifyOtherAppData()
            } else {
                binding.result7.text = "--"
                binding.result7.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat8.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result8.text = "Not Possible"
                binding.result8.setTextColor(resources.getColor(R.color.redColor))
            } else {
                binding.result8.text = "--"
                binding.result8.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat9.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result9.text = "Not Possible"
                binding.result9.setTextColor(resources.getColor(R.color.redColor))
                testSandboxBypass()
            } else {
                binding.result9.text = "--"
                binding.result9.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat10.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result10.text = "Not Possible"
                binding.result10.setTextColor(resources.getColor(R.color.redColor))
                testDisableFRP()
            } else {
                binding.result10.text = "--"
                binding.result10.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat11.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result11.text = "Possible"
                binding.result11.setTextColor(resources.getColor(R.color.greenColor))
            } else {
                binding.result11.text = "--"
                binding.result11.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat12.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result12.text = "Not Possible"
                binding.result12.setTextColor(resources.getColor(R.color.redColor))
            } else {
                binding.result12.text = "--"
                binding.result12.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat13.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result13.text = "Not Possible"
                binding.result13.setTextColor(resources.getColor(R.color.redColor))
            } else {
                binding.result13.text = "--"
                binding.result13.setTextColor(resources.getColor(R.color.black))
            }

        }
    }

    private fun testWriteSecureSettings() : Boolean {
        try {
            Settings.Global.putString(contentResolver, "adb_enabled", "1")
            return true
        } catch (e: SecurityException) {
            return false
        }
    }
    private fun installApkSilently() : Boolean {
        try {
            val packageInstaller = packageManager.packageInstaller
            val sessionParams = PackageInstaller.SessionParams(PackageInstaller.SessionParams.MODE_FULL_INSTALL)
            val sessionId = packageInstaller.createSession(sessionParams)
            val session = packageInstaller.openSession(sessionId)

            val apkFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "base.apk")
            apkFile.inputStream().use { inputStream ->
                session.openWrite("base.apk", 0, apkFile.length()).use { outputStream ->
                    inputStream.copyTo(outputStream)
                    session.fsync(outputStream)
                }
            }

            // Create an IntentSender using a PendingIntent
            val intent = Intent(this, InstallResultReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_MUTABLE
            )
            val intentSender = pendingIntent.intentSender

            session.commit(intentSender)
            return true
        } catch (e: Exception) {
            return false
        }
    }
    private fun isSystemApp(): Boolean {
        val systemAppPaths = listOf("/system/app/", "/system/priv-app/")
        return try {
            val apkPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
            systemAppPaths.any { File(apkPath, "base.apk").exists() }
        } catch (e: Exception) {
            false
        }
    }
    @SuppressLint("PrivateApi")
    private fun testInternalApis() : Boolean {
        try {
            Class.forName("com.android.internal.app.ActivityTrigger")
            return true
        } catch (e: ClassNotFoundException) {
            return false
        }
    }
    private fun isUpgradingExistingSystemApp(packageName: String): Boolean {
        return try {
            val packageInfo: PackageInfo = packageManager.getPackageInfo(packageName, 0)
            (packageInfo.applicationInfo!!.flags and android.content.pm.ApplicationInfo.FLAG_SYSTEM) != 0
        } catch (e: PackageManager.NameNotFoundException) {
            Log.e("SystemAppTest", "App not found: ${e.message}")
            false
        }
    }
    private fun getApkSignature(packageName: String, packageManager: PackageManager): String? {
        return try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_SIGNATURES)
            val signature = packageInfo.signatures?.get(0)?.toByteArray()
            val md = MessageDigest.getInstance("SHA")
            md.update(signature)
            md.digest().joinToString(":") { "%02x".format(it) }
        } catch (e: Exception) {
            Log.e("SystemAppTest", "Failed to get signature: ${e.message}")
            null
        }
    }
    private fun compareWithSystemAppSignature(systemAppPackage: String): Boolean {
        val mySignature = getApkSignature(packageName, packageManager)
        val systemSignature = getApkSignature(systemAppPackage, packageManager)

        Log.d("SystemAppTest", "My Signature: $mySignature")
        Log.d("SystemAppTest", "System App Signature: $systemSignature")

        return mySignature == systemSignature
    }


    /////////////////////

    private fun testRootAccess() : Boolean {
        try {
            val process = Runtime.getRuntime().exec("su")
            val output = BufferedReader(InputStreamReader(process.inputStream)).readLine()
            if (output != null) {
                return true
            } else {
                return false
            }
        } catch (e: Exception) {
            Toast.makeText(this,"Failed to execute root command",Toast.LENGTH_LONG).show()
            return false
        }
    }
    private fun testModifyOtherAppData() {
        val targetPath = File("/data/data/com.other.app/files/test.txt")
        try {
            if (targetPath.exists()) {
                targetPath.writeText("Test")
                Toast.makeText(this,"Modified another app’s data!",Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this,"Cannot access other app’s data.",Toast.LENGTH_LONG).show()
            }
        } catch (e: Exception) {
            Toast.makeText(this,"SELinux blocked access",Toast.LENGTH_LONG).show()
        }
    }
    private fun testSandboxBypass() {
        val targetFile = File("/data/data/com.some.other.app/shared_prefs/config.xml")
        try {
            if (targetFile.exists()) {
                val data = targetFile.readText()
                Log.d("SystemAppTest", "Read another app’s data: $data")
            } else {
                Log.e("SystemAppTest", "Cannot access another app’s data.")
            }
        } catch (e: Exception) {
            Log.e("SystemAppTest", "Sandboxing prevented access: ${e.message}")
        }
    }
    private fun testDisableFRP() {
        try {
            Settings.Global.putInt(contentResolver, "device_provisioned", 0)
            Log.d("SystemAppTest", "Disabled FRP!")
        } catch (e: SecurityException) {
            Log.e("SystemAppTest", "FRP is protected: ${e.message}")
        }
    }
    private fun testModifyKernel() {
        val cpuFile = File("/proc/sys/kernel/hostname")
        try {
            if (cpuFile.exists()) {
                cpuFile.writeText("HackedKernel")
                Log.d("SystemAppTest", "Kernel modification successful!")
            } else {
                Log.e("SystemAppTest", "Cannot modify kernel settings.")
            }
        } catch (e: Exception) {
            Log.e("SystemAppTest", "Kernel protection blocked modification: ${e.message}")
        }
    }
    private fun testModifySystemApp() {
        val targetApk = File("/system/app/Calculator/Calculator.apk")
        try {
            if (targetApk.exists()) {
                targetApk.writeText("Modified")
                Log.d("SystemAppTest", "Modified system app!")
            } else {
                Log.e("SystemAppTest", "Cannot modify system app.")
            }
        } catch (e: Exception) {
            Log.e("SystemAppTest", "System protection blocked modification: ${e.message}")
        }
    }
    private fun testPlayServicesAccess(context: Context) {
        val status = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context)
        if (status == com.google.android.gms.common.ConnectionResult.SUCCESS) {
            Log.d("SystemAppTest", "Google Play Services available!")
        } else {
            Log.e("SystemAppTest", "Google Play Services check failed.")
        }
    }

    /// RECEIVER
    class InstallResultReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val status = intent?.getIntExtra(PackageInstaller.EXTRA_STATUS, -1)
            if (status == PackageInstaller.STATUS_SUCCESS) {
                Toast.makeText(context, "Silent installation succeeded!", Toast.LENGTH_LONG).show()
            } else {
                val message = intent?.getStringExtra(PackageInstaller.EXTRA_STATUS_MESSAGE)
                Toast.makeText(context, "Silent installation failed: $message", Toast.LENGTH_LONG).show()
            }
        }
    }
}