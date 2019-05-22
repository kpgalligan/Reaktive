package com.badoo.reaktive.sample.ios

import com.badoo.reaktive.observable.observableOf
import com.badoo.reaktive.observable.observeOn
import com.badoo.reaktive.observable.subscribe
import com.badoo.reaktive.scheduler.mainScheduler
import kotlin.native.concurrent.ensureNeverFrozen

class FailTests{
    init {
        ensureNeverFrozen()
    }

    fun loadData(){
        observableOf(listOf("arst"))
            .observeOn(mainScheduler)
            .subscribe {
                showData(it)
            }
    }

    fun showData(a: Any){

    }
}

fun makeCrash(){
    val ft = FailTests()
    ft.loadData()
}