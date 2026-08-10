package com.example.my_books.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.my_books.data.dao.BookDao
import com.example.my_books.data.dao.CurrentUserDao
import com.example.my_books.data.dao.ShelvesDao
import com.example.my_books.data.dao.UserDao
import com.example.my_books.data.entity.Book
import com.example.my_books.data.entity.CurrentUser
import com.example.my_books.data.entity.Site
import com.example.my_books.data.entity.User

@Database(
    entities = [User::class, Site::class, Book::class, CurrentUser::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
//    abstract fun siteDao(): SiteDao
    abstract fun shelvesDao(): ShelvesDao
    abstract fun bookDao(): BookDao
    abstract fun currentUserDao(): CurrentUserDao


    companion object {
        // 定义从版本 1 到版本 2 的迁移
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // 1. 创建带有主键的临时新表 (匹配 Room 的期望)
                database.execSQL(
                    "CREATE TABLE user_use_new (id INTEGER NOT NULL PRIMARY KEY, user_name TEXT, password TEXT)"
                )
                // 2. 将旧表数据复制到新表
                database.execSQL(
                    "INSERT INTO user_use_new (id, user_name, password) SELECT id, user_name, password FROM user_use"
                )
                // 3. 删除旧表
                database.execSQL("DROP TABLE user_use")
                // 4. 将新表重命名为旧表名
                database.execSQL("ALTER TABLE user_use_new RENAME TO user_use")
            }
        }
        private val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
            }
        }
        @Volatile
        private var INSTANCE: AppDatabase? = null
        // 双重检查锁（DCL）实现线程安全的单例
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                // 构建数据库实例，建议使用 applicationContext 防止内存泄漏
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "sql.db" // 数据库文件名
                ).addMigrations(MIGRATION_1_2,MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}