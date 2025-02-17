package good.stuff.myapplication.api.dao

import com.google.gson.annotations.SerializedName

data class PlatformSerialized(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
)
