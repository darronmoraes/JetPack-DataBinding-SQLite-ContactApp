package com.example.contactmanagerdatabinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.example.contactmanagerdatabinding.databinding.ContactListItemBinding;

import java.util.ArrayList;

public class ContactDataAdapter extends RecyclerView.Adapter<ContactDataAdapter.ContactViewHolder> {

    private ArrayList<Contact> contacts;

    public ContactDataAdapter(ArrayList<Contact> contacts) {
        this.contacts = contacts;
    }


    // Custom method to setContacts to notify on data set changes
    public void setContacts(ArrayList<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Changes made here
        ContactListItemBinding contactListItemBinding = DataBindingUtil.inflate(
                LayoutInflater.from(parent.getContext()),
                R.layout.contact_list_item,
                parent,
                false
        );
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_list_item, parent, false);
        return new ContactViewHolder(contactListItemBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contacts.get(position);

        holder.contactListItemBinding.setContact(contact);
    }

    @Override
    public int getItemCount() {
        if (contacts != null) {
            return contacts.size();
        } else {
            return 0;
        }
    }


    class ContactViewHolder extends RecyclerView.ViewHolder {
        // making instance of ContactListItemBinding *databinding
        ContactListItemBinding contactListItemBinding;

        // public constructor
        public ContactViewHolder(@NonNull ContactListItemBinding contactListItemBinding) {
            super(contactListItemBinding.getRoot());
            this.contactListItemBinding = contactListItemBinding;
        }
    }
}
