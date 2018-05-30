package app.mmguardian.com.location_tracking.db.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import app.mmguardian.com.location_tracking.db.model.User;

@Dao

public interface UserDao {

    @Insert
    public long[] insertUsers(User... users);

    @Insert
    public void insertUserList(List<User> users);

    @Update
    public void updateUsers(User... users);

    @Delete
    public void deleteUsers(User... users);

    @Query("select * from User")
    public List<User> searchAllUsers();

    @Query("select * from User where :age > 18")
    public List<User> searchUsersByAge(int age);

    @Query("select * from User where firstName like :name limit 1")
    public User searchUserByName(String name);
}
