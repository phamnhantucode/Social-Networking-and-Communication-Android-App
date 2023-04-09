package com.phamnhantucode.composeclonemessengerclient.core.remote

import com.phamnhantucode.composeclonemessengerclient.core.util.Constant

interface WebService {
    sealed class Endpoints(val url: String) {
        object Image: Endpoints("${Constant.BASE_URL}/image") {
            fun getImageUrl(id: String): String {
                return "$url/$id"
            }
        }
    }
}