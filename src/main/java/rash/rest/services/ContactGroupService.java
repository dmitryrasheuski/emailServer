package rash.rest.services;

import rash.rest.entity.contactGroup.ContactGroup;

import java.util.List;
import java.util.Optional;

public interface ContactGroupService {
    Optional<ContactGroup> addGroup(ContactGroup group);
    List<ContactGroup> getAll();
    Optional<ContactGroup> updateGroup(ContactGroup group);
    Optional<ContactGroup> removeGroup(ContactGroup group);
}
