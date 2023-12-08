package com.mauroalexandro.anappstore.views

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mauroalexandro.anappstore.R
import com.mauroalexandro.anappstore.databinding.FragmentDetailsBinding
import com.mauroalexandro.anappstore.models.App
import com.mauroalexandro.anappstore.utils.Utils

class DetailsFragment(private val activity: FragmentActivity, private val app: App, private val utils: Utils) : BottomSheetDialogFragment() {

    private var _binding: FragmentDetailsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.AppBottomSheetDialogTheme)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //Fill data detail from this App
        if(app.name.isNotEmpty()) {
            binding.appDetailName.text = app.name
        }
        if(app.vername.isNotEmpty()) {
            binding.appDetailVername.text = app.vername
        }
        binding.appDetailVercode.text = app.vercode.toString()

        binding.appDetailDownloadButton.setOnClickListener {
            utils.showAlertDialog(activity)
        }
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}