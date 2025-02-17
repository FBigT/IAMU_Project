package good.stuff.myapplication.api.dao

import com.google.gson.annotations.SerializedName

data class CoverSerialized(
    @SerializedName("id") val id: Int,
    @SerializedName("url") val url: String
)
