package com.example.alertasismica.data.repository

import com.example.alertasismica.data.local.UserDao
import com.example.alertasismica.data.local.UserEntity

class UserRepositoryImpl(
    private val dao: UserDao
) : UserRepository {

    override suspend fun register(user: UserEntity): SaveResult {
        if (dao.getByUsuario(user.usuario) != null) return SaveResult.UsuarioTaken
        if (dao.getByEmail(user.email) != null) return SaveResult.EmailTaken
        dao.insert(user)
        return SaveResult.Success
    }

    override suspend fun login(usuario: String, password: String): UserEntity? =
        dao.login(usuario.trim(), password)
}
