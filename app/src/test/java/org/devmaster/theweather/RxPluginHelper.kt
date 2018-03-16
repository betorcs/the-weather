package org.devmaster.theweather

import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.schedulers.Schedulers


object RxPluginHelper {

    fun setup() {
        RxJavaPlugins.setNewThreadSchedulerHandler({ Schedulers.trampoline() })
        RxJavaPlugins.setIoSchedulerHandler({ Schedulers.trampoline() })
        RxJavaPlugins.setComputationSchedulerHandler({ Schedulers.trampoline() })
        RxAndroidPlugins.setMainThreadSchedulerHandler({ Schedulers.trampoline() })
    }

    fun reset() {
        RxJavaPlugins.reset()
        RxAndroidPlugins.reset()
    }

}