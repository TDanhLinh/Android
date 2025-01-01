package vn.edu.hust.roomexamples

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface StudentDao {
  @Query("select * from students")
  suspend fun getAllStudents(): Array<Student>

  @Query("select * from students where mssv = :ms")
  suspend fun getStudentByMssv(ms: String): Array<Student>

  @Query("select * from students where hoten like '%' || :name || '%'")
  suspend fun getStudentsByName(name: String): Array<Student>

  @Query("select * from students where hoten like '%' || :name || '%' and mssv = :ms")
  suspend fun getStudentsByNameAndMSSV(name: String, ms: String): Student

  @Insert
  suspend fun insertStudent(student: Student): Long

  @Update
  suspend fun updateStudent(student: Student): Int

  @Delete
  suspend fun deleteStudent(student: Student): Int

  @Query("delete from students where mssv = :mssv")
  suspend fun deleteByMssv(mssv: String): Int
}