package rash.rest.entity.contact;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import rash.rest.entity.contactGroup.ContactGroup;

import javax.persistence.*;
import java.util.List;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@Entity
@Table(name = "contact")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NonNull
    private String email;
    @ManyToMany(fetch = FetchType.EAGER, cascade = {CascadeType.REFRESH, CascadeType.DETACH, CascadeType.MERGE})
    @JoinTable(
            name = "contact_contact_group",
            joinColumns = @JoinColumn(name = "id_contact"),
            inverseJoinColumns = @JoinColumn(name = "id_group")
    )
    private List<ContactGroup> groups;
}
