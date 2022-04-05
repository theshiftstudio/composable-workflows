package com.shiftstudio.workflow.sample.hello.infrastructure.inject

import android.app.Application
import com.shiftstudio.workflow.sample.hello.infrastructure.DemoApplication
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DemoAppModule {

    @Provides
    @Singleton
    fun provideAppScope(application: Application): CoroutineScope =
        (application as DemoApplication).appScope
}
