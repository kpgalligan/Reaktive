package com.badoo.reaktive.observable

import com.badoo.reaktive.testutils.TestObservable
import com.badoo.reaktive.testutils.TestScheduler
import com.badoo.reaktive.testutils.dispose
import com.badoo.reaktive.testutils.hasSubscribers
import com.badoo.reaktive.testutils.isCompleted
import com.badoo.reaktive.testutils.isError
import com.badoo.reaktive.testutils.test
import com.badoo.reaktive.testutils.values
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SubscribeOnTest : UpstreamDownstreamGenericTests by UpstreamDownstreamGenericTests<Unit>({ subscribeOn(TestScheduler()) }) {

    private val scheduler = TestScheduler(isManualProcessing = true)
    private val upstream = TestObservable<Int>()
    private val observer = upstream.subscribeOn(scheduler).test()

    @Test
    fun does_not_subscribe_synchronously() {
        assertFalse(upstream.hasSubscribers)
    }

    @Test
    fun subscribes_through_scheduler() {
        scheduler.process()

        assertTrue(upstream.hasSubscribers)
    }

    @Test
    fun emits_values_synchronously() {
        scheduler.process()
        observer.reset()
        upstream.onNext(0)
        upstream.onNext(1)
        upstream.onNext(2)

        assertEquals(listOf(0, 1, 2), observer.values)
    }

    @Test
    fun completes_synchronously() {
        scheduler.process()
        observer.reset()
        upstream.onComplete()

        assertTrue(observer.isCompleted)
    }

    @Test
    fun errors_synchronously() {
        scheduler.process()
        val error = Throwable()
        observer.reset()
        upstream.onError(error)

        assertTrue(observer.isError(error))
    }

    @Test
    fun disposes_executor_WHEN_disposed() {
        observer.dispose()

        assertTrue(scheduler.executors.all(TestScheduler.Executor::isDisposed))
    }
}