package com.nas.naisak.fragment.gallerynew.model

import com.google.gson.annotations.SerializedName

class GetAlbumResponseModelnew {


    @SerializedName("status") var status: Int=0
    @SerializedName("message") var message: String=""
    @SerializedName("data") var dataArrayList: ArrayList<GetAlbumDataModel>  = ArrayList()

    }
