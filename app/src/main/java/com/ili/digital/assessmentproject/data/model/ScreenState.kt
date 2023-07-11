package com.ili.digital.assessmentproject.data.model

data class ScreenState(
    val isLoading: Boolean = true,
    val photoList: List<MarsPhoto>? = null,
    val error: String = "",
    val cameraList: HashSet<String>? = null
)