package com.example.tugasbesar.room

import androidx.room.*

@Dao
interface UserDao {
    @Insert
    suspend fun addNote(user: User)
    @Update
    suspend fun updateNote(user: User)
    @Delete
    suspend fun deleteNote(user: User)
    @Query("SELECT * FROM User")
    suspend fun getNotes() : List<User>
    @Query("SELECT * FROM User WHERE id =:user_id")
    suspend fun getNote(user_id: Int) : List<User>
    @Query("SELECT * FROM User WHERE username =:user_username")
    suspend fun getUser(user_username: String) : List<User>
    @Query("SELECT * FROM User WHERE username =:user_username AND password=:user_password")
    suspend fun getAccount(user_username: String,user_password:String) : List<User>
}
