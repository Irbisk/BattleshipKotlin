interface Auth {
    fun login()
    fun logout()
}

class AdminAuth : Auth {
    override fun login() {
        println("Admin logged in.")
    }

    override fun logout() {
        println("Admin logged out.")
    }
}

class UserAuth : Auth {
    override fun login() {
        println("User logged in.")
    }

    override fun logout() {
        println("User logged out.")
    }
}

class SystemUser(userName: String, isAdmin: Boolean): Auth by if (isAdmin) {
    AdminAuth()
} else {
    UserAuth()
} {

}