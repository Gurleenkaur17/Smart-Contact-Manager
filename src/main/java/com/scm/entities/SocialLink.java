package com.scm.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "SocialLinks")
@Getter
@Setter
@NoArgsConstructor
public class SocialLink {
    @Id
    private int id;
    private String name;
    private String link;

    @ManyToOne
    private Contact contact;
}
