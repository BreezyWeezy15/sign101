package com.deep.system.fragments



import android.os.Bundle
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.deep.system.databinding.FragmentLayoutBinding
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class FifthFragment : Fragment() {

    private lateinit var binding: FragmentLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val result = getDummyMap()
        binding.valueTxt.text = result
        FirstFragment.dataList.add("Fifth Fragment" to result) // Store function name & value

        saveToExcel() // Save data to Excel when reaching this fragment
    }

    private fun getDummyMap(): String {
        val dummyMap = mapOf("Key1" to "Value1", "Key2" to "Value2")
        return "Dummy Map: $dummyMap"
    }

    private fun saveToExcel() {
        val externalStorageDir = Environment.getExternalStorageDirectory()
        val xlsFolder = File(externalStorageDir, "xlsx")
        if (!xlsFolder.exists()) {
            xlsFolder.mkdirs()
        }

        val documentId = System.currentTimeMillis().toString()
        val file = File(xlsFolder, "combined_report_$documentId.xlsx")

        try {
            val workbook = if (file.exists()) {
                WorkbookFactory.create(file.inputStream()) // Open existing file
            } else {
                XSSFWorkbook() // Create new workbook if file doesn't exist
            }

            val sheetIndex = 2 // Third page (0-based index)
            val sheet = if (workbook.numberOfSheets > sheetIndex) {
                workbook.getSheetAt(sheetIndex)
            } else {
                workbook.createSheet("Dummy Data") // Create if it doesn't exist
            }

            // Insert function names and values into sheet
            FirstFragment.dataList.forEachIndexed { index, (functionName, result) ->
                val row = sheet.createRow(index)
                row.createCell(0).setCellValue(functionName)
                row.createCell(1).setCellValue(result)
            }

            // Write to file
            val fos = FileOutputStream(file)
            workbook.write(fos)
            fos.close()
            workbook.close()

            binding.valueTxt.text = "Excel file saved at: ${file.absolutePath}"
        } catch (e: IOException) {
            e.printStackTrace()
            binding.valueTxt.text = "Error saving Excel file: ${e.message}"
        }
    }
}
