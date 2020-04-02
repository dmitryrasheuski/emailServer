package rash.rest.services;

import rash.rest.entity.contact.Contact;
import rash.rest.entity.contactGroup.ContactGroup;

import java.util.List;
import java.util.Optional;

public interface ContactService {
    Optional<Contact> addContact(Contact contact);
    List<Contact> getContacts();
    List<Contact> getContacts(ContactGroup group);
    List<Contact> getContacts(String email);
    List<Contact> getContact(Long id);
    Optional<Contact> removeContact(Contact contact);
    Optional<Contact> updateContact(Contact contact);
}
