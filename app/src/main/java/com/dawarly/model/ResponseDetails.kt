package com.dawarly.model

import java.io.Serializable
import java.util.ArrayList

class ResponseDetails : Serializable{

    lateinit var result: ResultModel
    var next_page_token: String? =""
    var reference: String? = ""
    var status: String? = ""
}