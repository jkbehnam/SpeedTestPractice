package ir.bbeigzadeh.speedtest.model

data class UiState(
    val speed: String = "",
    val ping: String = "-",
    val maxSpeed: String = "-",
    val arcValue: Float = 0f,
    val inProgress: Boolean = false
)
