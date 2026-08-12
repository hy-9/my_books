package com.example.my_books.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.my_books.activity.register.RegisterViewModel
import com.example.my_books.activity.shelves.ShelvesViewModel
import com.example.my_books.data.Repository
import com.example.my_books.fragment.myFragment.MyFragmentViewModel
import kotlin.jvm.java


class ViewModelFactory(repository: Repository) : ViewModelProvider.Factory {
    private val repository: Repository

    // Factory 构造时接收 Repository 实例
    init {
        this.repository = repository
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        // 判断要创建的 ViewModel 类型
        if (modelClass.isAssignableFrom(RegisterViewModel::class.java)) {
            // 手动 new 出 ViewModel，并把 Repository 传进去
            return RegisterViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(ShelvesViewModel::class.java)) {
            return ShelvesViewModel(repository) as T
        }
        if (modelClass.isAssignableFrom(MyFragmentViewModel::class.java)) {
            return MyFragmentViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass)
    }
}