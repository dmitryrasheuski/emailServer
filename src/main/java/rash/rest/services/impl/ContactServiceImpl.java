package rash.rest.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rash.rest.dao.ContactDao;
import rash.rest.dao.ContactGroupDao;
import rash.rest.entity.contact.Contact;
import rash.rest.entity.contactGroup.ContactGroup;
import rash.rest.services.ContactService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ContactServiceImpl implements ContactService {

    @Autowired
    private ContactDao contactDao;

    @Autowired
    private ContactGroupDao groupDao;

    @Override
    public Optional<Contact> addContact(Contact contact) {
        if ( contact.getId() != null ) throw new RuntimeException("The new contact most have no 'id'.");
        if ( contact.getEmail() == null ) throw new RuntimeException("The saved contact didn't have 'email'.");
        List<ContactGroup> groups = new ArrayList<>();
        for (ContactGroup group : contact.getGroups()) {

            Optional<ContactGroup> dbGroup = null;
            if (group.getId() != null) {
                dbGroup = groupDao.getGroup( group.getId() );
            } else if (group.getTitle() != null) {
                dbGroup = groupDao.getGroup( group.getTitle() );
            }

            if( !dbGroup.isPresent() ) throw new RuntimeException("Group has not been found into database. group: " + group.toString());

            groups.add(dbGroup.get());
        }

        Contact savedContact = new Contact( contact.getEmail() );
        savedContact.setGroups(groups);

        return contactDao.addContact(savedContact);
    }

    @Override
    public List<Contact> getContacts() {
        return contactDao.getContacts();
    }

    @Override
    public List<Contact> getContacts(ContactGroup group) {
        if (group.getId() == null && group.getTitle() == null) throw new NullPointerException("group id and group title has been 'null'");
        if(group.getId() == null) {
            String groupTitle = group.getTitle();
            group = groupDao.getGroup( groupTitle )
                    .orElseThrow(() -> new RuntimeException("group with title:\"" + groupTitle + "\" has not been found"));
        }

        return contactDao.getContacts(group);
    }

    @Override
    public List<Contact> getContacts(String email) {
        return getContact( new Contact(email) );
    }

    @Override
    public List<Contact> getContact(Long id) {
        Contact contact = new Contact();
        contact.setId(id);
        return getContact(contact);
    }

    @Override
    public Optional<Contact> removeContact(Contact contact) {
        return contactDao.removeContact(contact);
    }

    @Override
    public Optional<Contact> updateContact(Contact contact) {
        return (contact.getId() != null) ?
                contactDao.updateContact(contact) :
                Optional.empty();
    }

    List<Contact> getContact(Contact contact) {
        Optional<Contact> optionalContact = Optional.empty();
        if ( contact.getId() != null ) {
            optionalContact = contactDao.getContactById( contact.getId() );
        } else if ( contact.getEmail() != null ) {
            optionalContact = contactDao.getContactByEmail( contact.getEmail() );
        }
        List<Contact> contacts = new ArrayList<>();
        optionalContact.ifPresent(contacts::add);
        return contacts;
    }
}
