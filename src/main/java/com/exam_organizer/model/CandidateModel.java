package com.exam_organizer.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "candidates")
public class CandidateModel implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long candidateId;

    private String candidateName;
    private Date dateOfBirth;
    private String username;
    private String phoneNumber;
    private String password;
    private String email;

    private String role;

    private String status;
    @Lob
    @Column(name = "image", columnDefinition = "LONGBLOB")
    private byte[] image;


    @OneToMany(mappedBy = "candidateModel", cascade = CascadeType.ALL)
    private List<CandidateExamRegisteredModel> registeredExams = new ArrayList<>();


//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return null;
//    }


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }
    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Constructors, getters, and setters
}
