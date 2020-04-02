package rash.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import rash.rest.entity.contactGroup.ContactGroup;
import rash.rest.services.ContactGroupService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/contact/group")
public class ContactsGroupController {

    @Autowired
    private ContactGroupService contactGroupService;

    @PostMapping("/")
    public ResponseEntity<ContactGroup> addGroup(@RequestBody ContactGroup group) {

        Optional<ContactGroup> optional = contactGroupService.addGroup(group);
        return ( optional.isPresent() ) ?
                ResponseEntity.ok( optional.get() ) :
                ResponseEntity.status(HttpStatus.CONFLICT).build();
    }

    @GetMapping("")
    public ResponseEntity<List<ContactGroup>> getGroups() {

        List<ContactGroup> groups = contactGroupService.getAll();
        return ( groups.isEmpty() ) ?
                ResponseEntity.notFound().build() :
                ResponseEntity.ok(groups);
    }

    @PutMapping("/")
    public ResponseEntity<ContactGroup> updateContact(@RequestBody ContactGroup group) {
        Optional<ContactGroup> optional = contactGroupService.updateGroup(group);
        return ( optional.isPresent() ) ?
                ResponseEntity.ok( optional.get() ) :
                ResponseEntity.notFound().build();
    }

    @DeleteMapping("/")
    public ResponseEntity<ContactGroup> removeContact(@RequestBody ContactGroup group) {
        Optional<ContactGroup> optional = contactGroupService.removeGroup(group);
        return ( optional.isPresent() ) ?
                ResponseEntity.ok( optional.get() ) :
                ResponseEntity.notFound().build();
    }
}
