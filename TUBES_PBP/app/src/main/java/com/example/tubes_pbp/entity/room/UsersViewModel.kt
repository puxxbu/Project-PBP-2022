package com.example.tubes_pbp.entity.room

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class UsersViewModel(application: Application): AndroidViewModel(application) {
    private val readAllData: List<Users>
    private val repository: UserRepository

    init {
        val userDao = UsersDB.getDatabase(application).usersDao()
        repository = UserRepository(userDao)
        readAllData = repository.readAllData
    }

    fun addUser(users: Users){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(users)
        }

    }
}