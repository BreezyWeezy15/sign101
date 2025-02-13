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

class FirstFragment : Fragment() {

    private lateinit var binding : FragmentLayoutBinding
    companion object {
        val dataList = mutableListOf<Pair<String, String>>() // Stores function names & values
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val result = getDummyInt()
        binding.valueTxt.text = result
        dataList.add("First Fragment" to result) // Store function name & value

        Handler(Looper.getMainLooper()).postDelayed({
            (requireActivity().supportFragmentManager).beginTransaction().replace(R.id.container, SecondFragment()).commit()
        }, 3000)
    }

    private fun getDummyInt(): String {
        val dummyInt = 42
        return "Dummy Int: $dummyInt"
    }
}
