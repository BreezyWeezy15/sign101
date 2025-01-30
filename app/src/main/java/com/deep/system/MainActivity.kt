package com.deep.system

import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.deep.system.databinding.ActivityMainBinding

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
            } else {
                binding.result1.text = "--"
                binding.result1.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat2.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result2.text = "Possible"
                binding.result2.setTextColor(resources.getColor(R.color.greenColor))
            } else {
                binding.result2.text = "--"
                binding.result2.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat3.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result3.text = "Possible"
                binding.result3.setTextColor(resources.getColor(R.color.greenColor))
            } else {
                binding.result3.text = "--"
                binding.result3.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat4.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result4.text = "Possible"
                binding.result4.setTextColor(resources.getColor(R.color.greenColor))
            } else {
                binding.result4.text = "--"
                binding.result4.setTextColor(resources.getColor(R.color.black))
            }

        }
        binding.switchCompat5.setOnCheckedChangeListener { buttonView, isChecked ->

            if(isChecked){
                binding.result5.setText("Possible")
                binding.result5.setTextColor(resources.getColor(R.color.greenColor))
            } else {
                binding.result5.setText("--")
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
    }
}