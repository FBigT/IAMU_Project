package good.stuff.myapplication.api.dao

import com.google.gson.annotations.SerializedName

data class InvolvedCompanySerialized(
    @SerializedName("company") val company: CompanySerialized,
    @SerializedName("developer") val isDeveloper: Boolean,
    @SerializedName("publisher") val isPublisher: Boolean
)