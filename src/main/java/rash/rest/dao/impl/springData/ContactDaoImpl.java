package rash.rest.dao.impl.springData;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rash.rest.dao.ContactDao;
import rash.rest.entity.contact.Contact;
import rash.rest.entity.contactGroup.ContactGroup;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public interface ContactDaoImpl extends CrudRepository<Contact, Long>, ContactDao {

    @Override
    default Optional<Contact> addContact(Contact contact) {
        return (contact.getId() == null) ?
                Optional.of( save(contact) ) :
                Optional.empty();
    }

    @Override
    default List<Contact> getContacts() {
        Iterable<Contact> contactIterable = findAll();
        List<Contact> contacts = StreamSupport.stream(contactIterable.spliterator(), false)
                .collect(Collectors.toList());
        return contacts;
    }

    @Override
    default List<Contact> getContacts(ContactGroup group) {

        Long groupId = group.getId();
        Iterable<Contact> contactIterable = getByGroup(groupId);
        List<Contact> contacts =
                StreamSupport.stream(contactIterable.spliterator(), false)
                .collect(Collectors.toList());
        return contacts;
    }

    @Override
    default Optional<Contact> removeContact(Contact contact) {
        Optional<Contact> optional = findById(contact.getId());
        optional.ifPresent(this::delete);
        return optional;
    }

    @Override
    default Optional<Contact> getContactByEmail(String email) {
        return findByEmail(email);
    }

    @Override
    default Optional<Contact> updateContact(Contact contact) {
        return (contact.getId() == null) ?
                Optional.empty() :
                Optional.of( save(contact) );
    }


    @Query("SELECT c FROM Contact c INNER JOIN c.groups g WHERE g.id = :id")
    List<Contact> getByGroup(long id);

    Optional<Contact> findByEmail(String email);

}
