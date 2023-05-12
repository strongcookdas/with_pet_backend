package com.ajou_nice.with_pet.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
public class Party extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partyId;
    @ManyToOne
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    private String name;
    private String partyIsbn;

    public Party(User user) {
        this.user = user;
    }

    public void updateParty(String name, String isbn) {
        this.name = name;
        this.partyIsbn = isbn;
    }
}
