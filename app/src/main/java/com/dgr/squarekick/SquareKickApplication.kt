package com.dgr.squarekick

import android.app.Application
import com.dgr.squarekick.data.network.ApiFootballAPI
import com.dgr.squarekick.data.network.NetworkConnectionInterceptor
import com.dgr.squarekick.data.repositories.FixturesRepository
import com.dgr.squarekick.ui.home.HomeViewModelFactory
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.androidXModule
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton

class SquareKickApplication : Application(), KodeinAware {

    override val kodein = Kodein.lazy {
        import(androidXModule(this@SquareKickApplication))
        bind() from singleton { NetworkConnectionInterceptor(instance()) }
        bind() from singleton { ApiFootballAPI(instance()) }
        bind() from singleton { FixturesRepository(instance()) }
        bind() from provider { HomeViewModelFactory(instance()) }
    }
}