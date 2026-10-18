package com.example.alertasismica.navigation

object Routes {
    // Sin barra inferior
    const val LOGIN = "login"
    const val REGISTER = "register"

    // Pestañas de la barra inferior
    const val QUAKES = "quakes"
    const val SAVED = "saved"
    const val CONTACTS = "contacts"
    const val CHECKLIST = "checklist"

    // Detalle del sismo
    const val ARG_QUAKE_ID = "quakeId"
    const val QUAKE_DETAIL = "quake/{$ARG_QUAKE_ID}"
    fun quakeDetail(id: String) = "quake/$id"

    // Formulario de contactos (nuevo / editar)
    const val ARG_CONTACT_ID = "contactId"
    const val CONTACT_NEW = "contact_new"
    const val CONTACT_EDIT = "contact_edit/{$ARG_CONTACT_ID}"
    fun contactEdit(id: Int) = "contact_edit/$id"
}
