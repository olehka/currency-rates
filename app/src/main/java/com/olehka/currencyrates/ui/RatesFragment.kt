package com.olehka.currencyrates.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.olehka.currencyrates.databinding.FragmentRatesBinding
import dagger.android.support.DaggerFragment


class RatesFragment : DaggerFragment() {

    private lateinit var viewDataBinding: FragmentRatesBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewDataBinding = FragmentRatesBinding.inflate(inflater, container, false)
        return viewDataBinding.root
    }

}