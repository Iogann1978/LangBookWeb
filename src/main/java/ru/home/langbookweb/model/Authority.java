package ru.home.langbookweb.model;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Authority implements GrantedAuthority {
    @Id
    private String authority;
    @ManyToMany(mappedBy = "authorities")
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<User> users;
}
