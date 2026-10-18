package com.example.alertasismica.data.repository

import com.example.alertasismica.data.local.UserEntity

interface UserRepository {
    suspend fun register(user: UserEntity): SaveResult
    suspend fun login(usuario: String, password: String): UserEntity?
}
