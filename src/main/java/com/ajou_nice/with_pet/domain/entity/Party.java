package com.ajou_nice.with_pet.domain.entity;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
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
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "userId", nullable = false)
    private User user;
    private String name;
    private String partyIsbn;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "party")
    private List<UserParty> userPartyList = new ArrayList<>();

    public Party(User user) {
        this.user = user;
    }

    public void updateParty(String name, String isbn) {
        this.name = name;
        this.partyIsbn = isbn;
    }
}
