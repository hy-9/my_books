package com.example.my_books.compose

object Routes {
    @JvmField
    val SETTINGS = Route("settings","设置")
    @JvmField
    val IMPORT = Route("import","导入数据")
    @JvmField
    val EXPORT = Route("export","导出数据")
    fun getAllRoutes():List<Route>{
        return listOf(SETTINGS,IMPORT,EXPORT)
    }
}
data class Route(val name:String,val description:String)