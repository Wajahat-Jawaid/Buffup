package com.wajahat.buffdemo.base

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
open class BaseViewState<T>(var data: T?, var error: Throwable?, var currentState: Int?) {

    enum class State private constructor(var value: Int) {
        LOADING(0), SUCCESS(1), FAILED(-1)
    }
}