package rash.rest.services.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import rash.rest.dao.ContactGroupDao;
import rash.rest.entity.contactGroup.ContactGroup;
import rash.rest.services.ContactGroupService;

import java.util.List;
import java.util.Optional;

@Service
public class ContactGroupServiceImpl implements ContactGroupService {

    @Autowired
    private ContactGroupDao groupDao;

    @Override
    public Optional<ContactGroup> addGroup(ContactGroup group) {
        return groupDao.addGroup(group);
    }

    @Override
    public List<ContactGroup> getAll() {
        return groupDao.getGroups();
    }

    @Override
    public Optional<ContactGroup> updateGroup(ContactGroup group) {
        return groupDao.updateGroup(group);
    }

    @Override
    public Optional<ContactGroup> removeGroup(ContactGroup group) {
        return groupDao.removeGroup(group);
    }
}
