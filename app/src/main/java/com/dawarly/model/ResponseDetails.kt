package com.dawarly.model

import java.io.Serializable

class ResponseDetails : Serializable{

    var result: ResultModel? = null
    var next_page_token: String? = null
    var reference: String? = null
    var status: String? = null
}