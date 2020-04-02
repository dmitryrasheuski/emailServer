package rash.rest.dao;

import rash.rest.entity.contact.Contact;
import rash.rest.entity.contactGroup.ContactGroup;

import java.util.List;
import java.util.Optional;

public interface ContactDao {

    Optional<Contact> addContact(Contact contact);
    Optional<Contact> getContactByEmail(String email);
    List<Contact> getContacts();
    List<Contact> getContacts(ContactGroup group);
    Optional<Contact> removeContact(Contact contact);
    Optional<Contact> updateContact(Contact contact);
}
