package com.aynlss.jumboxproject.data.source.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aynlss.jumboxproject.data.model.response.ProductEntity

@Database(entities = [ProductEntity::class], version = 1)
abstract class ProductRoomDB : RoomDatabase() {

    abstract fun productDao(): ProductDao
}