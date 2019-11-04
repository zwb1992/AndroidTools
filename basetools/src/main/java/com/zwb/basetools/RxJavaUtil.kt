package com.zwb.basetools

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import com.uber.autodispose.AutoDispose
import com.uber.autodispose.AutoDisposeConverter
import com.uber.autodispose.android.lifecycle.AndroidLifecycleScopeProvider

/**
 * @ author : zhouweibin
 * @ time: 2019/11/1 10:54.
 * @ desc:
 **/
object RxJavaUtil {

    @JvmStatic
    fun <T> bindLifecycle(
        lifecycleOwner: LifecycleOwner,
        event: Lifecycle.Event = Lifecycle.Event.ON_DESTROY
    ): AutoDisposeConverter<T> {
        return AutoDispose.autoDisposable(
            AndroidLifecycleScopeProvider.from(
                lifecycleOwner,
                event
            )
        )
    }
}