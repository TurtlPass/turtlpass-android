package com.turtlpass.rule

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@ExperimentalCoroutinesApi
class StandardCoroutineRule(
    val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {

    override fun starting(description: Description) {
        super.starting(description)
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        super.finished(description)
        Dispatchers.resetMain()
    }
}

@ExperimentalCoroutinesApi
fun StandardCoroutineRule.runTest(
    dispatchTimeoutMs: Long = 10_000L,
    block: suspend TestScope.(TestDispatcher) -> Unit
) {
    TestScope(testDispatcher).runTest(dispatchTimeoutMs) {
        block(testDispatcher)
    }
}
