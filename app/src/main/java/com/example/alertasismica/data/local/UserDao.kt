package com.example.alertasismica.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Insert
    suspend fun insert(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE usuario = :usuario LIMIT 1")
    suspend fun getByUsuario(usuario: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE usuario = :usuario AND password = :password LIMIT 1")
    suspend fun login(usuario: String, password: String): UserEntity?
}
