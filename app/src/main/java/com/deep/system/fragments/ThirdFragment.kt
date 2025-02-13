package com.deep.system.fragments

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.deep.system.R
import com.deep.system.databinding.FragmentLayoutBinding

class ThirdFragment : Fragment() {

    private lateinit var binding: FragmentLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val result = getDummyBoolean()
        binding.valueTxt.text = result
        FirstFragment.dataList.add("Third Fragment" to result) // Store function name & value
        binding.stepsTxt.text = "Checking 3rd item"
        Handler(Looper.getMainLooper()).postDelayed({
            (requireActivity().supportFragmentManager).beginTransaction().replace(R.id.container, FourthFragment()).commit()
        }, 3000)
    }

    private fun getDummyBoolean(): String {
        val dummyBoolean = true
        return "Dummy Boolean: $dummyBoolean"
    }
}
