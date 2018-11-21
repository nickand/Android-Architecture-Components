package com.nickand.architectureexample

import android.arch.lifecycle.ViewModel

interface BaseView<out T: ViewModel> {
    val viewModel: T

    fun getViewModelClass() : Class<out T>
}