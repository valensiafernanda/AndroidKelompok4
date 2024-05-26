package com.pemrogandroid.uas_.ui.listbuku

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ListBukuViewModel: ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is list buku Fragment"
    }
    val text: LiveData<String> = _text
}