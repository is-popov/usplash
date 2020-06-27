package com.ipopov.usplash.ui.base.permissions

enum class Permission(val code: Int) {
    CAMERA(100),
    LOCATION(101),
    STORAGE(102),
    CONTACTS(103);

    companion object {
        fun codeOf(code: Int): Permission? {
            return values().first { it.code == code }
        }
    }

}
