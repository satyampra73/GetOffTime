package com.project.OffTime.utills

class ApiUrls {
    companion object{
        const val BaseURL = "https://firebase-notification.developerbrothersproject.com/datastore/api/"
        const val Login = BaseURL + "user/login"
        const val SignUp = BaseURL + "user/signup"
        const val SendData = BaseURL + "user/store-data"
    }
}