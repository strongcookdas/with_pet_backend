package com.ajou_nice.with_pet.group.model.entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

import com.ajou_nice.with_pet.domain.entity.BaseEntity;
import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Getter
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE party SET deleted_at = CURRENT_TIMESTAMP where party_id = ?")
public class Party extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long partyId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User partyLeader;
    private String partyName;
    private String partyIsbn;
    private Integer memberCount;
    private Integer dogCount;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "party", orphanRemoval = true)
    private List<UserParty> userPartyList = new ArrayList<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "party", orphanRemoval = true)
    private List<Dog> dogList = new ArrayList<>();

    public Party(User partyLeader) {
        this.partyLeader = partyLeader;
    }

    public void updatePartyIsbn(String isbn) {
        this.partyIsbn = isbn;
    }

    public static Party of(User user, String partyName) {
        String formatedNow = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String isbn = RandomStringUtils.randomAlphabetic(6) + formatedNow;
        return Party.builder()
                .partyLeader(user)
                .partyName(partyName)
                .partyIsbn(isbn)
                .memberCount(1)
                .dogCount(1)
                .build();
    }

    public void updatePartyLeader(User user) {
        this.partyLeader = user;
    }

    public void updateMemberCount(Integer memberCount){
        this.memberCount = memberCount;
    }

    public void updateDogCount(Integer dogCount){
        this.dogCount = dogCount;
    }
}
