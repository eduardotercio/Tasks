package com.example.tasks.data.local

import androidx.room.*
import com.example.tasks.data.model.PriorityModel

@Dao
interface PriorityDAO {

    @Insert
    suspend fun insert(list: List<PriorityModel>)

    @Query("SELECT * FROM priority")
    suspend fun list(): List<PriorityModel>

    @Query("SELECT description FROM priority WHERE id = :id")
    suspend fun priority(id: Int): String

    /**
     * Limpa o banco de dados antes de pegar a lista de priority
     */
    @Query("DELETE FROM priority")
    suspend fun clear()

}