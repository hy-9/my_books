package com.example.my_books.activity.compose

object Routes {
    @JvmField
    val SETTINGS = Route("settings","设置")
    fun getAllRoutes():List<Route>{
        return listOf(SETTINGS)
    }
}
data class Route(val name:String,val description:String)