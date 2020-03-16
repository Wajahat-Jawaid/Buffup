package com.wajahat.buffup

/**
 * Created by Wajahat Jawaid(wajahatjawaid@gmail.com)
 */
class BuffUp {

    init {
        mInstance = this
    }

    companion object {

        private var mInstance: BuffUp? = null
        private var hasInitialized = false

        fun initialize() {
            hasInitialized = true
        }
    }
}