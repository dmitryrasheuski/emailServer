package rash.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rash.rest.entity.contact.Contact;
import rash.rest.entity.contactGroup.ContactGroup;
import rash.rest.services.ContactService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contact")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping("/")
    public ResponseEntity<Contact> addContact(@RequestBody Contact contact) {

        Optional<Contact> optional = contactService.addContact(contact);
        return ( optional.isPresent() ) ?
                ResponseEntity.ok( optional.get() ) :
                ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping("/")
    public ResponseEntity<List<Contact>> getContacts(
            @RequestParam(required = false) String groupId,
            @RequestParam(required = false) String groupTitle,
            @RequestParam(required = false) String email
    ) {

        List<Contact> contacts = null;
        if( groupId != null ) {
            ContactGroup group = new ContactGroup();
            group.setId(Long.parseLong(groupId));
            contacts = contactService.getContacts(group);
        } else if ( groupTitle != null ) {
            ContactGroup group = new ContactGroup(groupTitle);
            contacts = contactService.getContacts(group);
        } else if ( email != null ) {
            contacts = contactService.getContacts(email);
        } else {
            contacts = contactService.getContacts();
        }

        return contacts.isEmpty() ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(contacts);
    }

    @PutMapping("/")
    public ResponseEntity<Contact> updateContact(@RequestBody Contact contact) {

        Optional<Contact> optional = contactService.updateContact(contact);
        return ( optional.isPresent() ) ?
                ResponseEntity.ok( optional.get() ) :
                ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @DeleteMapping("/")
    public ResponseEntity<Contact> removeContact(@RequestBody Contact contact) {

        Optional<Contact> optional = contactService.removeContact(contact);
        return ( optional.isPresent() ) ?
                ResponseEntity.ok( optional.get() ) :
                ResponseEntity.status(HttpStatus.CONFLICT).build();
    }
}
