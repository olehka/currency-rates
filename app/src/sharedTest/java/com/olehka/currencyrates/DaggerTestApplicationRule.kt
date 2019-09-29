package com.olehka.currencyrates

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.olehka.currencyrates.di.DaggerTestApplicationComponent
import com.olehka.currencyrates.di.TestApplicationComponent
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class DaggerTestApplicationRule : TestWatcher() {

    lateinit var component: TestApplicationComponent
        private set

    override fun starting(description: Description?) {
        super.starting(description)

        val app = ApplicationProvider.getApplicationContext<Context>() as RatesApplication
        component = DaggerTestApplicationComponent.factory().create(app)
        component.inject(app)
    }
}