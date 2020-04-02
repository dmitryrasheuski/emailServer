package rash.rest.dao;

import rash.rest.entity.contactGroup.ContactGroup;

import java.util.List;
import java.util.Optional;

public interface ContactGroupDao {
    Optional<ContactGroup> addGroup(ContactGroup group);
    Optional<ContactGroup> getGroup(Long id);
    Optional<ContactGroup> getGroup(String title);
    List<ContactGroup> getGroups();
    Optional<ContactGroup> updateGroup(ContactGroup group);
    Optional<ContactGroup> removeGroup(ContactGroup group);
}
