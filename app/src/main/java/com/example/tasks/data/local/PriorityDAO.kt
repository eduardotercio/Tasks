package com.example.tasks.data.local

import androidx.room.*
import com.example.tasks.data.model.PriorityModel

@Dao
interface PriorityDAO {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(list: List<PriorityModel>)

    @Query("SELECT * FROM priority")
    suspend fun list(): List<PriorityModel>

    /**
     * Limpa o banco de dados antes de pegar a lista de priority
     */
    @Query("DELETE FROM priority")
    suspend fun clear()

}