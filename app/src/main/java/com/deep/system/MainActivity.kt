package com.deep.system

import android.annotation.SuppressLint
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageInfo
import android.content.pm.PackageInstaller
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.net.wifi.WifiManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.deep.system.databinding.ActivityMainBinding
import com.google.android.gms.common.GoogleApiAvailability
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.BufferedReader
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStreamReader
import java.security.MessageDigest

class MainActivity : AppCompatActivity() {

    private val broadcastActions = listOf(
        Intent.ACTION_POWER_CONNECTED,
        Intent.ACTION_POWER_DISCONNECTED,
        Intent.ACTION_BOOT_COMPLETED,
        Intent.ACTION_SCREEN_ON,
        Intent.ACTION_SCREEN_OFF,
        Intent.ACTION_PACKAGE_ADDED,
        Intent.ACTION_PACKAGE_REMOVED,
        Intent.ACTION_PACKAGE_REPLACED,
        Intent.ACTION_BATTERY_LOW,
        Intent.ACTION_BATTERY_OKAY,
        Intent.ACTION_AIRPLANE_MODE_CHANGED,
        Intent.ACTION_HEADSET_PLUG,
        Intent.ACTION_MEDIA_MOUNTED,
        Intent.ACTION_MEDIA_UNMOUNTED,
        Intent.ACTION_TIME_TICK,
        Intent.ACTION_TIME_CHANGED,
        Intent.ACTION_DATE_CHANGED,
        Intent.ACTION_CONFIGURATION_CHANGED,
        Intent.ACTION_LOCALE_CHANGED,
        Intent.ACTION_DREAMING_STARTED,
        Intent.ACTION_DREAMING_STOPPED,
        Intent.ACTION_MEDIA_SCANNER_SCAN_FILE,
        Intent.ACTION_MEDIA_SCANNER_STARTED,
        Intent.ACTION_MEDIA_SCANNER_FINISHED,
        Intent.ACTION_UID_REMOVED,
        Intent.ACTION_WALLPAPER_CHANGED,
        Intent.ACTION_PACKAGE_RESTARTED
    )
    private val permissionsToCheck = listOf(
        "android.permission.ACCESS_LOCATION_EXTRA_COMMANDS",
        "android.permission.ACCESS_NETWORK_STATE",
        "android.permission.ACCESS_WIFI_STATE",
        "android.permission.BLUETOOTH",
        "android.permission.BLUETOOTH_ADMIN",
        "android.permission.BROADCAST_STICKY",
        "android.permission.CHANGE_NETWORK_STATE",
        "android.permission.CHANGE_WIFI_MULTICAST_STATE",
        "android.permission.CHANGE_WIFI_STATE",
        "android.permission.DISABLE_KEYGUARD",
        "android.permission.EXPAND_STATUS_BAR",
        "android.permission.FOREGROUND_SERVICE",
        "android.permission.GET_PACKAGE_SIZE",
        "android.permission.INTERNET",
        "android.permission.KILL_BACKGROUND_PROCESSES",
        "android.permission.MANAGE_OWN_CALLS",
        "android.permission.MODIFY_AUDIO_SETTINGS",
        "android.permission.NFC",
        "android.permission.READ_SYNC_SETTINGS",
        "android.permission.READ_SYNC_STATS",
        "android.permission.RECEIVE_BOOT_COMPLETED",
        "android.permission.REORDER_TASKS",
        "android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS",
        "android.permission.SET_ALARM",
        "android.permission.SET_TIME_ZONE",
        "android.permission.SET_WALLPAPER",
        "android.permission.READ_CALENDAR",
        "android.permission.WRITE_CALENDAR",
        "android.permission.CAMERA",
        "android.permission.READ_CONTACTS",
        "android.permission.WRITE_CONTACTS",
        "android.permission.GET_ACCOUNTS",
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.ACCESS_COARSE_LOCATION",
        "android.permission.RECORD_AUDIO",
        "android.permission.READ_PHONE_STATE",
        "android.permission.CALL_PHONE",
        "android.permission.READ_CALL_LOG",
        "android.permission.WRITE_CALL_LOG",
        "android.permission.ADD_VOICEMAIL",
        "android.permission.USE_SIP",
        "android.permission.PROCESS_OUTGOING_CALLS",
        "android.permission.BODY_SENSORS",
        "android.permission.SEND_SMS",
        "android.permission.RECEIVE_SMS",
        "android.permission.READ_SMS",
        "android.permission.RECEIVE_WAP_PUSH",
        "android.permission.RECEIVE_MMS",
        "android.permission.READ_EXTERNAL_STORAGE",
        "android.permission.WRITE_EXTERNAL_STORAGE",
        "android.permission.SYSTEM_ALERT_WINDOW",
        "android.permission.WRITE_SETTINGS",
        "android.permission.REQUEST_INSTALL_PACKAGES",
        "android.permission.PACKAGE_USAGE_STATS",
        "android.permission.BIND_ACCESSIBILITY_SERVICE",
        "android.permission.BIND_VPN_SERVICE",
        "android.permission.BIND_NOTIFICATION_LISTENER_SERVICE",
        "android.permission.BIND_INPUT_METHOD",
        "android.permission.BIND_DEVICE_ADMIN"
    )
    private var currentPermissionIndex = 0
    private val handler = Handler(Looper.getMainLooper())
    private val manageStoragePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (isManageExternalStorageGranted()) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show()
                checkPermissionsAndGenerateReport()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }

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

        setUpUI()
        binding.file.setOnClickListener {
            checkBroadcastReceivers()
        }

    }

    private fun checkBroadcastReceivers() {
        val packageManager = packageManager
        val packageName = packageName

        val results = mutableListOf<Pair<String, String>>()

        for (action in broadcastActions) {

            val intent = Intent(action)
            val receivers = packageManager.queryBroadcastReceivers(intent, PackageManager.MATCH_ALL)
            val isRegisteredForApp = receivers.any { it.activityInfo.packageName == packageName }
            val receiverName = action.substringAfterLast(".")
            val registrationStatus = if (isRegisteredForApp) "Registered" else "Not Registered"
            results.add(Pair(receiverName, registrationStatus))
            val message = "$receiverName: $registrationStatus"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            Log.d("BroadcastCheck", message)
        }

        generateReceiversExcelReport(results)
    }
    private fun generateReceiversExcelReport(results: List<Pair<String, String>>) {
        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Broadcast Receivers Report")

        // Create header row
        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Receiver Name")
        headerRow.createCell(1).setCellValue("Registration Status")
        sheet.setColumnWidth(0, getMaxLength(results, 0) * 256)
        sheet.setColumnWidth(1, getMaxLength(results, 1) * 256)
        results.forEachIndexed { index, result ->
            val row: Row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(result.first)
            row.createCell(1).setCellValue(result.second)
        }

        val externalStorageDir = Environment.getExternalStorageDirectory()
        val xlsFolder = File(externalStorageDir, "xlsx")

        if (!xlsFolder.exists()) {
            xlsFolder.mkdirs()
        }

        val file = File(xlsFolder, "broadcast_receivers_report.xlsx")

        try {
            val fileOutputStream = FileOutputStream(file)
            workbook.write(fileOutputStream)

            fileOutputStream.flush()
            fileOutputStream.close()

            Toast.makeText(this, "Excel report saved", Toast.LENGTH_LONG).show()

        }
        catch (e: Exception) {
            Toast.makeText(this, "Failed to save Excel report", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
        finally {
            try {
                workbook.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    override fun onResume() {
        super.onResume()
        checkAndRequestManageExternalStorage()
    }
    private fun isManageExternalStorageGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            true
        }
    }
    private fun checkAndRequestManageExternalStorage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!isManageExternalStorageGranted()) {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION).apply {
                    data = Uri.parse("package:$packageName")
                }
                manageStoragePermissionLauncher.launch(intent)
            } else {
                checkPermissionsAndGenerateReport()
            }
        } else {
            checkPermissionsAndGenerateReport()
        }
    }
    private fun setUpUI(){

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
        binding.switchCompat7.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result7.setTextColor(resources.getColor(R.color.redColor))
                if(testModifyOtherAppData()){
                    binding.result7.text = "Not Possible"
                    binding.result7.setTextColor(resources.getColor(R.color.greenColor))
                    Toast.makeText(this,"Success.",Toast.LENGTH_LONG).show()
                } else {
                    binding.result7.text = "Not Possible"
                    binding.result7.setTextColor(resources.getColor(R.color.redColor))
                    Toast.makeText(this,"Selinux blocked data ,Cannot access other app’s data.",Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result7.text = "--"
                binding.result7.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat8.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result8.setTextColor(resources.getColor(R.color.redColor))
                if(testModifyKernel()){
                    binding.result8.text = "Possible"
                    binding.result8.setTextColor(resources.getColor(R.color.greenColor))
                    Toast.makeText(this,"Kernel modification successful!",Toast.LENGTH_LONG).show()
                } else {
                    binding.result8.text = "Not Possible"
                    binding.result8.setTextColor(resources.getColor(R.color.redColor))
                    Toast.makeText(this,"Kernel modification failed!",Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result8.text = "--"
                binding.result8.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat9.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result9.setTextColor(resources.getColor(R.color.redColor))
                if(testSandboxBypass()){
                    binding.result9.text = "Possible"
                    binding.result9.setTextColor(resources.getColor(R.color.greenColor))
                    Toast.makeText(this,"Success bypassing sandbox",Toast.LENGTH_LONG).show()
                } else {
                    binding.result9.text = "Not Possible"
                    binding.result9.setTextColor(resources.getColor(R.color.redColor))
                    Toast.makeText(this,"Failed to bypass sandbox",Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result9.text = "--"
                binding.result9.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat10.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result10.text = "Not Possible"
                binding.result10.setTextColor(resources.getColor(R.color.redColor))
                if(testDisableFRP()){
                    Toast.makeText(this,"Disabled FRP!",Toast.LENGTH_LONG).show()
                } else {
                    Toast.makeText(this,"FRP is protected",Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result10.text = "--"
                binding.result10.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat11.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                if(setWiFiEnabled()){
                    binding.result11.text = "Possible"
                    binding.result11.setTextColor(resources.getColor(R.color.greenColor))
                    Toast.makeText(this,"Wifi Enabled Successfully",Toast.LENGTH_LONG).show()
                } else {
                    binding.result11.text = "Not Possible"
                    binding.result11.setTextColor(resources.getColor(R.color.redColor))
                    Toast.makeText(this,"Wifi Disabled",Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result11.text = "--"
                binding.result11.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat12.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result12.setTextColor(resources.getColor(R.color.redColor))
                if(setMobileDataEnabled()){
                    binding.result12.text = "Possible"
                    binding.result12.setTextColor(resources.getColor(R.color.greenColor))
                    Toast.makeText(this,"Mobile data enabled",Toast.LENGTH_LONG).show()
                } else {
                    binding.result12.text = "Not Possible"
                    binding.result12.setTextColor(resources.getColor(R.color.redColor))
                    Toast.makeText(this,"Mobile data disabled",Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result12.text = "--"
                binding.result12.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat13.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result13.setTextColor(resources.getColor(R.color.redColor))
                if(setAirplaneMode()){
                    binding.result13.text = "Possible"
                    binding.result13.setTextColor(resources.getColor(R.color.greenColor))
                    Toast.makeText(this,"Airplane mode enabled",Toast.LENGTH_LONG).show()
                } else {
                    binding.result13.text = "Not Possible"
                    binding.result13.setTextColor(resources.getColor(R.color.redColor))
                    Toast.makeText(this,"Failed to enable airplane mode",Toast.LENGTH_LONG).show()
                }
            } else {
                binding.result13.text = "--"
                binding.result13.setTextColor(resources.getColor(R.color.black))
            }

        }

    }
    private fun checkPermissionsAndGenerateReport() {
        val results = mutableListOf<Pair<String, String>>()
        checkNextPermission(results)
    }
    private fun checkNextPermission(results: MutableList<Pair<String, String>>) {
        if (currentPermissionIndex < permissionsToCheck.size) {
            val fullPermission = permissionsToCheck[currentPermissionIndex]
            val permissionName = fullPermission.substringAfterLast(".") // Extracts only the name
            val isGranted = checkSelfPermission(fullPermission) == PackageManager.PERMISSION_GRANTED
            val status = if (isGranted) "Granted" else "Not Granted"

            results.add(Pair(permissionName, status))
            Toast.makeText(this, "$permissionName: $status", Toast.LENGTH_SHORT).show()

            handler.postDelayed({
                currentPermissionIndex++
                checkNextPermission(results)
            }, 1000)
        } else {
            // Run this only once when all permissions are checked
            generateExcelReport(results)
        }
    }
    private fun generateExcelReport(results: List<Pair<String, String>>) {


        val workbook = XSSFWorkbook()
        val sheet = workbook.createSheet("Permissions Report")

        val headerRow = sheet.createRow(0)
        headerRow.createCell(0).setCellValue("Permission")
        headerRow.createCell(1).setCellValue("Status")

        results.forEachIndexed { index, result ->
            val row: Row = sheet.createRow(index + 1)
            row.createCell(0).setCellValue(result.first)
            row.createCell(1).setCellValue(result.second)
        }

        sheet.setColumnWidth(0, getMaxLength(results, 0) * 256)
        sheet.setColumnWidth(1, getMaxLength(results, 1) * 256)

        val externalStorageDir = Environment.getExternalStorageDirectory()
        val xlsFolder = File(externalStorageDir, "xlsx")

        if (!xlsFolder.exists()) {
            xlsFolder.mkdirs()
        }
        val documentId  = System.currentTimeMillis().toString()
        val file = File(xlsFolder, "permissions_report$documentId.xlsx")

        try {
            val fileOutputStream = FileOutputStream(file)
            workbook.write(fileOutputStream)

            fileOutputStream.flush()
            fileOutputStream.close()

            Toast.makeText(this, "Excel report saved", Toast.LENGTH_LONG).show()

        } catch (e: Exception) {
            Toast.makeText(this, "Failed to save Excel report", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        } finally {
            try {
                workbook.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    private fun getMaxLength(results: List<Pair<String, String>>, columnIndex: Int): Int {
        var maxLength = 0
        results.forEach {
            val length = it.toList()[columnIndex].length
            if (length > maxLength) {
                maxLength = length
            }
        }
        return maxLength
    }



    ////////////////////////////////////////////////

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
    private fun testModifyOtherAppData() : Boolean {
        val targetPath = File("/data/data/com.other.app/files/test.txt")
        try {
            if (targetPath.exists()) {
                targetPath.writeText("Test")
                return true
            } else {
                return false
            }
        } catch (e: Exception) {
            return false
        }
    }
    private fun testSandboxBypass() : Boolean {
        val targetFile = File("/data/data/com.some.other.app/shared_prefs/config.xml")
        try {
            if (targetFile.exists()) {
                targetFile.readText()
                return true
            } else {
                return false
            }
        } catch (e: Exception) {
            return false
        }
    }
    private fun testDisableFRP() : Boolean {
        try {
            Settings.Global.putInt(contentResolver, "device_provisioned", 0)
            return true
        } catch (e: SecurityException) {
            return false
        }
    }
    private fun testModifyKernel() : Boolean {
        val cpuFile = File("/proc/sys/kernel/hostname")
        try {
            if (cpuFile.exists()) {
                cpuFile.writeText("HackedKernel")
                return true
            } else {
                Log.e("SystemAppTest", "Cannot modify kernel settings.")
                return false
            }
        } catch (e: Exception) {
            Log.e("SystemAppTest", "Kernel protection blocked modification: ${e.message}")
            return false
        }
    }
    private fun setAirplaneMode() : Boolean {
        try {

            Settings.Global.putInt(contentResolver, Settings.Global.AIRPLANE_MODE_ON, 1)
            val intent = Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED)
            intent.putExtra("state", true)
            sendBroadcast(intent)
            return true
        } catch (e: SecurityException) {
            Log.e("SystemAppTest", "Permission denied: ${e.message}")
            return false
        } catch (e: Exception) {
            Log.e("SystemAppTest", "Failed to change Airplane mode: ${e.message}")
            return false
        }
    }
    private fun setMobileDataEnabled() : Boolean {
        try {
            Settings.Global.putInt(contentResolver, "mobile_data", 1)
            return true
        } catch (e: SecurityException) {
            Log.e("SystemAppTest", "Permission denied: ${e.message}")
            return false
        } catch (e: Exception) {
            Log.e("SystemAppTest", "Failed to change mobile data state: ${e.message}")
            return false
        }
    }
    private fun setWiFiEnabled() : Boolean {
        try {
            val wifiManager = getSystemService(Context.WIFI_SERVICE) as WifiManager
            wifiManager.isWifiEnabled = true
            return true
        } catch (e: SecurityException) {
            Log.e("SystemAppTest", "Permission denied: ${e.message}")
            return false
        } catch (e: Exception) {
            Log.e("SystemAppTest", "Failed to change WiFi state: ${e.message}")
            return false
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