package com.philbrick.ui.screen.product

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.philbrick.R
import com.philbrick.databinding.FragmentAboutBinding
import com.philbrick.databinding.FragmentContactUsBinding
import com.philbrick.ui.customview.BaseFragment

class ContactUsFragment : BaseFragment() {

    private var parentActivity: SideMenuActivity? = null
    private lateinit var contactUsBinding: FragmentContactUsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parentActivity = if (activity is SideMenuActivity) activity as SideMenuActivity else null

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        contactUsBinding = FragmentContactUsBinding.inflate(layoutInflater, container, false)
        return contactUsBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init(){

    }

}