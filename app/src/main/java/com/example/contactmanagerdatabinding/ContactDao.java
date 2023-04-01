package com.example.contactmanagerdatabinding;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDao {

    @Insert
    void insert(Contact contact);

    @Query("SELECT * FROM contacts_table")
    List<Contact> getAllContacts();

    @Delete
    void delete(Contact contact);
}
