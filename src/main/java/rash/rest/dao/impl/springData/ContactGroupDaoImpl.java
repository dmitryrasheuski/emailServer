package rash.rest.dao.impl.springData;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import rash.rest.dao.ContactGroupDao;
import rash.rest.entity.contactGroup.ContactGroup;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Repository
public interface ContactGroupDaoImpl extends CrudRepository<ContactGroup, Long>, ContactGroupDao {

    @Override
    default Optional<ContactGroup> addGroup(ContactGroup group) {
        if(group.getId() != null) return Optional.empty();
        return Optional.of( save(group) );
    }

    @Override
    default Optional<ContactGroup> getGroup(String title) {
        return findByTitle(title);
    }

    @Override
    default Optional<ContactGroup> getGroup(Long id) {
        return findById(id);
    }

    @Override
    default List<ContactGroup> getGroups() {
        Iterable<ContactGroup> iterable = findAll();
        List<ContactGroup> groups = StreamSupport.stream(iterable.spliterator(), false)
                .collect(Collectors.toList());

        return groups;
    }

    @Override
    default Optional<ContactGroup> updateGroup(ContactGroup group) {
        if( group.getId() == null ) return Optional.empty();
        return Optional.of( save(group) );
    }

    @Override
    default Optional<ContactGroup> removeGroup(ContactGroup group) {
        Optional<ContactGroup> optional = findById(group.getId());
        optional.ifPresent(this::delete);

        return optional;
    }

    Optional<ContactGroup> findByTitle(String title);
}
