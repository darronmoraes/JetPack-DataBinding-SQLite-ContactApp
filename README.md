# JetPack-DataBinding-SQLite-ContactApp

**DOCUMENTATION FOR IMPLEMENTATION**

    Transforming RecyclerView to DataBinding
    Steps include:
        1. Adding the contact_list_item views inside <layout> here...  </layout>
        2. adding data binding tag --> <data> 
            inside here will go, <variable name="contact" and type="package location for the contact class"/>
            and close the tag
        3. modifying the TextViews with text attributes to,
            (name: "@{contact.name}") and (email: "@{contact.email}")
        4. In ContactDataAdapter.class --> inner class on ContactViewHolder.class,
            we now don't require TextView Initialization and finding of the views using id's
            -> make an instance of ContactListItemBinding
            -> change constructor params from View to ContactListItemBinding
            -> super(contactListItemBinding.getRoot());
            -> this.contactListItemBinding = contactListItemBinding
            
        5. now an error will arise in the ContactDataAdapter.class 
            changes taking affect on onCreateViewHolder() && onBindViewHolder()
            
            -> making changes to onCreateViewHolder()
                ContactListItemBinding contactListItemBinding = DataBindingUtil.inflate(
                        LayoutInflater.from(parent.getContext()),
                        R.layout.contact_list_item,
                        parent,
                        false
                      );
                 return new ContactViewHolder(contactListItemBinding);
                 
            -> making changes to onCreateViewHolder()
                Contact contact = contacts.get(position);
                holder.contactListItemBinding.setContact(contact);
                
         6. activity_main.xml
              add id to constraint layout "constraint_layout_main"
              
         7. change findViewById() for recyclerView to activityMainBinding.recyclerView;
        
    Clean the project
    Rebuild the project
