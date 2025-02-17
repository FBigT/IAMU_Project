package good.stuff.myapplication.model

import good.stuff.myapplication.R

enum class PlatformType(val iconResId: Int) {
    WINDOWS(R.drawable.ic_windows),
    PLAYSTATION(R.drawable.ic_play_station),
    XBOX(R.drawable.ic_xbox),
    NINTENDO_SWITCH(R.drawable.ic_switch),
    LINUX(R.drawable.ic_linux),
    MAC(R.drawable.ic_mac),
    UNKNOWN(R.drawable.ic_unknown);

    companion object {
        fun fromString(platform: String): PlatformType {
            return entries.find { type ->
                platform.contains(type.name, ignoreCase = true)
            } ?: UNKNOWN
        }
    }
}
