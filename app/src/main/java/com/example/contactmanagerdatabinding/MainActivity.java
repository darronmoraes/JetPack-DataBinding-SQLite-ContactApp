package com.example.contactmanagerdatabinding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;

import com.example.contactmanagerdatabinding.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    // Database instances
    private ContactAppDatabase contactAppDatabase;
    private ArrayList<Contact> contacts = new ArrayList<>();
    private ContactDataAdapter contactDataAdapter;

    // Binding instances
    private ActivityMainBinding activityMainBinding;
    private MainActivityClickHandler handler;

    public MainActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Data Binding
        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);
        handler = new MainActivityClickHandler(this);
        activityMainBinding.setClickHandler(handler);

        // RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerView_contacts_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        // Adapter
        contactDataAdapter = new ContactDataAdapter(contacts);
        recyclerView.setAdapter(contactDataAdapter);

        // Database initialization
        contactAppDatabase = Room.databaseBuilder(
                getApplicationContext(),
                ContactAppDatabase.class,
                "ContactDB"
        ).build();

        // Add Data
        LoadData();

        // Handling swiping #ItemTouchHelper.SimpleCallback()
        // dragDirs = 0, ItemTouchHelper = left
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                return 0;
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Contact contact = contacts.get(viewHolder.getAdapterPosition());
                deleteContact(contact);
            }
        }).attachToRecyclerView(recyclerView);   // Attach the ItemTouchHelper to recyclerView

        // FAB already created in MainActivityCLickHandler class so no need to do it here


    }

    // onActivityResult method
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            String name = data.getStringExtra("NAME");
            String email = data.getStringExtra("EMAIL");

            Contact contact = new Contact(name, email, 0);
            addNewContact(contact);
        }
    }

    private void deleteContact(Contact contact) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                // onBackground
                contactAppDatabase.getContactDao().delete(contact);
                contacts.remove(contact);
                // onPost Execution
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        contactDataAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void LoadData() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                // onBackground
                contacts.addAll(contactAppDatabase.getContactDao().getAllContacts());
                // onPost Execution
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        contactDataAdapter.setContacts(contacts);
                        contactDataAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    private void addNewContact(Contact contact) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                // onBackground
                contactAppDatabase.getContactDao().insert(contact);
                contacts.add(contact);
                // onPost Execution
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        contactDataAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
    }

    // class for MainActivityClickHandler
    public class MainActivityClickHandler {
        Context context;

        // constructor
        public MainActivityClickHandler(Context context) {
            this.context = context;
        }

        public void onFABClicked(View view) {
            Intent intent = new Intent(MainActivity.this, AddNewContactActivity.class);
            startActivityForResult(intent, 1);
        }
    }
}