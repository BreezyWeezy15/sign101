package com.deep.system

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
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.deep.system.databinding.ActivityMainBinding
import java.io.File
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
                binding.result1.text = "Possible"
                binding.result1.setTextColor(resources.getColor(R.color.greenColor))
                if(testWriteSecureSettings()){
                    Toast.makeText(this,"Successfully changed Secure Settings",Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this,"Permission denied",Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result1.text = "--"
                binding.result1.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat2.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result2.text = "Possible"
                binding.result2.setTextColor(resources.getColor(R.color.greenColor))
                if(installApkSilently()){
                    Toast.makeText(this, "Silent installation triggered", Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this, "Failed to install APK silently", Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result2.text = "--"
                binding.result2.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat3.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result3.text = "Possible"
                binding.result3.setTextColor(resources.getColor(R.color.greenColor))
                if(isSystemApp("com.deep.system")){
                    Toast.makeText(this,"Success checking system app",Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this,"Error checking system app",Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result3.text = "--"
                binding.result3.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat4.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result4.text = "Possible"
                binding.result4.setTextColor(resources.getColor(R.color.greenColor))
                if(testInternalApis()){
                    Toast.makeText(this,"Access to internal API succeeded",Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this,"Access to internal API failed",Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result4.text = "--"
                binding.result4.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat5.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result5.text = "Possible"
                binding.result5.setTextColor(resources.getColor(R.color.greenColor))
                if(isUpgradingExistingSystemApp("com.deep.system")){
                    Toast.makeText(this,"Systep App Upgrade Success",Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this,"System App Upgrade Failed",Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result5.text = "--"
                binding.result5.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat6.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result6.text = "Not Possible"
                binding.result6.setTextColor(resources.getColor(R.color.redColor))
            } else {
                binding.result6.text = "--"
                binding.result6.setTextColor(resources.getColor(R.color.black))
            }

        }

        /////////////////////////
        binding.switchCompat7.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result7.setText("Not Possible")
                binding.result7.setTextColor(resources.getColor(R.color.redColor))
            } else {
                binding.result7.setText("--")
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
            } else {
                binding.result9.text = "--"
                binding.result9.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat10.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result10.text = "Not Possible"
                binding.result10.setTextColor(resources.getColor(R.color.redColor))
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

            val apkFile = File("apkPath") // Change this to actual APK path
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
    private fun isSystemApp(packageName: String): Boolean {
        val systemAppPaths = listOf("/system/app/", "/system/priv-app/")
        return try {
            val apkPath = "/data/app/$packageName/base.apk"
            systemAppPaths.any { File(apkPath).exists() }
        } catch (e: Exception) {
            false
        }
    }
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