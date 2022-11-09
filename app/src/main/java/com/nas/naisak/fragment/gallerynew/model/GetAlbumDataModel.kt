package com.nas.naisak.fragment.gallerynew.model

import com.google.gson.annotations.SerializedName

class GetAlbumDataModel {

    @SerializedName("id") var id: Int=0
    @SerializedName("title") var title: String=""
    @SerializedName("cover_image") var cover_image: String=""
    @SerializedName("created_at") var created_at: String=""

}