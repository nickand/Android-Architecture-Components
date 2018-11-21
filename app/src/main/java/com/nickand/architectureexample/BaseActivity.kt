package com.nickand.architectureexample

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

abstract class BaseActivity<out T: ViewModel> : AppCompatActivity(), BaseView<T> {

    override val viewModel: T by lazy {
        ViewModelProviders.of(this).get(getViewModelClass())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())

    }

    abstract fun getLayoutResId(): Int

}