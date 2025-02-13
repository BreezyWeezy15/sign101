package com.deep.system

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.deep.system.databinding.ActivityMainBinding
import com.deep.system.fragments.FirstFragment
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream

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

        Toast.makeText(this, "Processing...", Toast.LENGTH_LONG).show()

    }

    private fun checkBroadcastReceivers(permissionsResults: List<Pair<String, String>>) {
        val packageManager = packageManager
        val packageName = packageName

        val receiversResults = mutableListOf<Pair<String, String>>()

        for (action in broadcastActions) {
            val intent = Intent(action)
            val receivers = packageManager.queryBroadcastReceivers(intent, PackageManager.MATCH_ALL)
            val isRegisteredForApp = receivers.any { it.activityInfo.packageName == packageName }
            val receiverName = action.substringAfterLast(".")
            val registrationStatus = if (isRegisteredForApp) "Registered" else "Not Registered"
            receiversResults.add(Pair(receiverName, registrationStatus))
        }

        generateCombinedExcelReport(permissionsResults, receiversResults)
    }


    private fun generateCombinedExcelReport(

        permissionsResults: List<Pair<String, String>>,
        receiversResults: List<Pair<String, String>>
    ) {

        Toast.makeText(this, "Checking Permissions", Toast.LENGTH_LONG).show()
        val workbook = XSSFWorkbook()

        val permissionsSheet = workbook.createSheet("Permissions Report")
        val headerRowPermissions = permissionsSheet.createRow(0)
        headerRowPermissions.createCell(0).setCellValue("Permission")
        headerRowPermissions.createCell(1).setCellValue("Status")

        permissionsResults.forEachIndexed { index, result ->
            val row = permissionsSheet.createRow(index + 1)
            row.createCell(0).setCellValue(result.first)
            row.createCell(1).setCellValue(result.second)
        }
        permissionsSheet.setColumnWidth(0, getMaxLength(permissionsResults) * 256)
        permissionsSheet.setColumnWidth(1, getMaxLength(permissionsResults) * 256)


        val receiversSheet = workbook.createSheet("Broadcast Receivers Report")
        val headerRowReceivers = receiversSheet.createRow(0)
        headerRowReceivers.createCell(0).setCellValue("Receiver")
        headerRowReceivers.createCell(1).setCellValue("Status")


        Toast.makeText(this, "Checking Receivers", Toast.LENGTH_LONG).show()

        receiversResults.forEachIndexed { index, result ->
            val row = receiversSheet.createRow(index + 1)
            row.createCell(0).setCellValue(result.first)
            row.createCell(1).setCellValue(result.second)
        }
        receiversSheet.setColumnWidth(0, getMaxLength(receiversResults) * 256)
        receiversSheet.setColumnWidth(1, getMaxLength(receiversResults) * 256)

        val externalStorageDir = Environment.getExternalStorageDirectory()
        val xlsFolder = File(externalStorageDir, "xlsx")
        if (!xlsFolder.exists()) {
            xlsFolder.mkdirs()
        }

        val file = File(xlsFolder, "combined_report.xlsx")

        try {

            val fileOutputStream = FileOutputStream(file)
            workbook.write(fileOutputStream)
            fileOutputStream.flush()
            fileOutputStream.close()

            Handler(Looper.getMainLooper()).postDelayed({
                supportFragmentManager.beginTransaction().replace(R.id.container,FirstFragment()).commit()
            }, 5000)

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
    private fun checkPermissionsAndGenerateReport() {
        val results = mutableListOf<Pair<String, String>>()
        checkNextPermission(results)
    }
    private fun checkNextPermission(results: MutableList<Pair<String, String>>) {
        if (currentPermissionIndex < permissionsToCheck.size) {
            val fullPermission = permissionsToCheck[currentPermissionIndex]
            val permissionName = fullPermission.substringAfterLast(".")
            val isGranted = checkSelfPermission(fullPermission) == PackageManager.PERMISSION_GRANTED
            val status = if (isGranted) "Granted" else "Not Granted"

            results.add(Pair(permissionName, status))

            handler.postDelayed({
                currentPermissionIndex++
                checkNextPermission(results)
            }, 200)
        } else {
            checkBroadcastReceivers(results)
        }
    }

    private fun getMaxLength(results: List<Pair<String, String>>): Int {
        return results.maxOf { it.first.length }
    }
}
