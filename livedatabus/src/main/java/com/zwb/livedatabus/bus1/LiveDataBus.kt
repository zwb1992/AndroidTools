package com.zwb.livedatabus.bus1

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.LiveData
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import java.util.Map


/**
 * @ author : zhouweibin
 * @ time: 2019/10/24 11:41.
 * @ desc:https://github.com/JeremyLiao/LiveDataBus
 **/
object LiveDataBus {

    private val bus: MutableMap<Any, MutableLiveData<Any>> by lazy { mutableMapOf<Any, MutableLiveData<Any>>() }

    fun <T> getChannel(target: String, type: Class<T>): MutableLiveData<T> {
        if (!bus.containsKey(target)) {
            bus[target] = BusMutableLiveData<Any>()
        }
        return bus[target] as MutableLiveData<T>
    }

    fun getChannel(target: String): MutableLiveData<Any> {
        return getChannel(target, Any::class.java)
    }

    private class ObserverWrapper<T>(private val observer: Observer<T>?) : Observer<T> {

        private val isCallOnObserve: Boolean
            get() {
                val stackTrace = Thread.currentThread().stackTrace
                if (stackTrace.isNotEmpty()) {
                    for (element in stackTrace) {
                        if ("android.arch.lifecycle.LiveData" == element.className && "observeForever" == element.methodName) {
                            return true
                        }
                    }
                }
                return false
            }

        override fun onChanged(t: T) {
            observer?.let {
                if (!isCallOnObserve) {
                    it.onChanged(t)
                }
            }
        }
    }

    private class BusMutableLiveData<T> : MutableLiveData<T>() {

        private val observerMap = mutableMapOf<Observer<in T>, LiveDataBus.ObserverWrapper<in T>>()

        override fun observe(owner: LifecycleOwner, observer: Observer<in T>) {
            super.observe(owner, observer)
            try {
                hook(observer)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        override fun observeForever(observer: Observer<in T>) {
            if (!observerMap.containsKey(observer)) {
                observerMap[observer] = ObserverWrapper(observer)
            }
            super.observeForever(observerMap[observer] as Observer<in T>)
        }

        override fun removeObserver(observer: Observer<in T>) {
            var realObserver: Observer<in T>? = null
            if (observerMap.containsKey(observer)) {
                realObserver = observerMap.remove(observer)
            } else {
                realObserver = observer
            }
            super.removeObserver(realObserver!!)
        }

        @Throws(Exception::class)
        private fun hook(observer: Observer<in T>) {
            //get wrapper's version
            val classLiveData = LiveData::class.java
            val fieldObservers = classLiveData.getDeclaredField("mObservers")
            fieldObservers.isAccessible = true
            val objectObservers = fieldObservers.get(this)
            val classObservers = objectObservers.javaClass
            val methodGet = classObservers.getDeclaredMethod("get", Any::class.java)
            methodGet.isAccessible = true
            val objectWrapperEntry = methodGet.invoke(objectObservers, observer)
            var objectWrapper: Any? = null
            if (objectWrapperEntry is Map.Entry<*, *>) {
                objectWrapper = objectWrapperEntry.value
            }
            if (objectWrapper == null) {
                throw NullPointerException("Wrapper can not be bull!")
            }
            val classObserverWrapper = objectWrapper.javaClass.superclass
            val fieldLastVersion = classObserverWrapper!!.getDeclaredField("mLastVersion")
            fieldLastVersion.isAccessible = true
            //get livedata's version
            val fieldVersion = classLiveData.getDeclaredField("mVersion")
            fieldVersion.isAccessible = true
            val objectVersion = fieldVersion.get(this)
            //set wrapper's version
            fieldLastVersion.set(objectWrapper, objectVersion)
        }
    }
}