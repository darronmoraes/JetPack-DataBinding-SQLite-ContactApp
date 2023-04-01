package com.example.contactmanagerdatabinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.contactmanagerdatabinding.databinding.ActivityAddNewContactBinding;

public class AddNewContactActivity extends AppCompatActivity {

    private ActivityAddNewContactBinding activityAddNewContactBinding;
    Contact contact;
    // Create an instance of inner class AddNewContactActivityClickHandlers
    public AddNewContactActivityClickHandlers addNewContactActivityClickHandlers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_contact);

        // DataBinding
        contact = new Contact();
        activityAddNewContactBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_new_contact);
        activityAddNewContactBinding.setContact(contact);

        addNewContactActivityClickHandlers = new AddNewContactActivityClickHandlers(this);
        // argument passed here should match with the one in layout file variable type
        activityAddNewContactBinding.setClickHandler(addNewContactActivityClickHandlers);
    }



    // Inner Class
    public class AddNewContactActivityClickHandlers {
        Context context;

        // Constructor
        public AddNewContactActivityClickHandlers(Context context) {
            this.context = context;
        }

        // method onSubmit Button Clicked
        public void onSubmitClicked(View view) {
            if (contact.getName() == null) {
                Toast.makeText(context, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent();
                intent.putExtra("NAME", contact.getName());
                intent.putExtra("EMAIL", contact.getEmail());

                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }
}