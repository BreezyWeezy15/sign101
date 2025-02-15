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

class FourthFragment : Fragment() {

    private lateinit var binding: FragmentLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val result = getDummyList()
        binding.valueTxt.text = result
        FirstFragment.dataList.add("Fourth Fragment" to result) // Store function name & value
        binding.stepsTxt.text = "Checking 4th item"
        (requireActivity() as? FragmentCompletionListener)?.onFragmentCompleted()
    }

    private fun getDummyList(): String {
        val dummyList = listOf("Apple", "Banana", "Cherry")
        return "Dummy List: ${dummyList.joinToString(", ")}"
    }
}
