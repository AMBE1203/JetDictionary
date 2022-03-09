package com.example.jetdictionary.presenter.screen.home

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.jetdictionary.data.source.local.ILocalSource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val iLocalSource: ILocalSource) : ViewModel() {

    init {



    }
}