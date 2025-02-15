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

class SecondFragment : Fragment() {

    private lateinit var binding: FragmentLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val result = getDummyString()
        binding.valueTxt.text = result
        FirstFragment.dataList.add("Second Fragment" to result) // Store function name & value
        binding.stepsTxt.text = "Checking 2nd item"
        (requireActivity() as? FragmentCompletionListener)?.onFragmentCompleted()
    }

    private fun getDummyString(): String {
        val dummyString = "Hello, Dummy!"
        return "Dummy String: $dummyString"
    }
}
