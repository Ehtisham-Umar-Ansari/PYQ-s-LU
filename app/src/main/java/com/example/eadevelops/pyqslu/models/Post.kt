package com.example.eadevelops.pyqslu.models

class Post {
    var image : String = ""
    var caption : String = ""
    var uid : String = ""
    var time : String = ""
    //Using it to differentiate between normal post and notices
    //notice -> notice || post -> post
    var notice : String = ""

    constructor()

    constructor(image: String, caption: String, uid: String, time: String, notice: String) {
        this.image = image
        this.caption = caption
        this.uid = uid
        this.time = time
        this.notice = notice
    }

    constructor(caption: String, uid: String, time: String, notice: String) {
        this.caption = caption
        this.uid = uid
        this.time = time
        this.notice = notice
    }

}