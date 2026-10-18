package com.example.alertasismica

import android.app.Application
import android.content.Context
import com.example.alertasismica.data.local.AppDatabase
import com.example.alertasismica.data.remote.RetrofitClient
import com.example.alertasismica.data.repository.ChecklistRepository
import com.example.alertasismica.data.repository.ChecklistRepositoryImpl
import com.example.alertasismica.data.repository.ContactRepository
import com.example.alertasismica.data.repository.ContactRepositoryImpl
import com.example.alertasismica.data.repository.EarthquakeRepository
import com.example.alertasismica.data.repository.EarthquakeRepositoryImpl
import com.example.alertasismica.data.repository.SavedQuakeRepository
import com.example.alertasismica.data.repository.SavedQuakeRepositoryImpl
import com.example.alertasismica.data.repository.UserRepository
import com.example.alertasismica.data.repository.UserRepositoryImpl
import com.example.alertasismica.data.repository.WeatherRepository
import com.example.alertasismica.data.repository.WeatherRepositoryImpl

class AppContainer(context: Context) {
    private val database = AppDatabase.getInstance(context)

    val userRepository: UserRepository =
        UserRepositoryImpl(database.userDao())

    val earthquakeRepository: EarthquakeRepository =
        EarthquakeRepositoryImpl(RetrofitClient.usgsApi)

    val weatherRepository: WeatherRepository =
        WeatherRepositoryImpl(RetrofitClient.meteoApi)

    val savedQuakeRepository: SavedQuakeRepository =
        SavedQuakeRepositoryImpl(database.savedQuakeDao())

    val contactRepository: ContactRepository =
        ContactRepositoryImpl(database.contactDao())

    val checklistRepository: ChecklistRepository =
        ChecklistRepositoryImpl(database.checklistDao())
}

class App : Application() {
    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
