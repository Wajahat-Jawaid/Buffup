package com.wajahat.buffdemo.injection.module

import com.wajahat.buffdemo.utils.DialogUtils
import dagger.Module
import dagger.Provides

/**
 * Module which provides all required dependencies related to activities
 */
@Module
class ActivityModule {

    /**
     * Provides the DialogUtils dependency
     * @return singleton instance of DialogUtils.
     */
    @Provides
    fun provideDialogUtils(): DialogUtils {
        return DialogUtils()
    }
}