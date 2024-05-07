package com.example.eadevelops.pyqslu.models

class User {

    var image : String? = null
    var name : String? = null
    var email : String? = null
    var password : String? = null
    var bio : String? = null
    var course : String? = null
    var instaUrl : String? = null

    constructor()

    constructor(name: String?, email: String?, password: String?) {
        this.name = name
        this.email = email
        this.password = password
    }

    constructor(image: String?, name: String?, email: String?, password: String?) {
        this.image = image
        this.name = name
        this.email = email
        this.password = password
    }

    constructor(email: String?, password: String?) {
        this.email = email
        this.password = password
    }

    constructor(
        image: String?,
        name: String?,
        email: String?,
        password: String?,
        bio: String?,
        course: String?,
        instaUrl: String?
    ) {
        this.image = image
        this.name = name
        this.email = email
        this.password = password
        this.bio = bio
        this.course = course
        this.instaUrl = instaUrl
    }

}