package com.ajou_nice.with_pet.domain.entity;

import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.enums.UserRole;
import javax.persistence.AttributeOverride;
import javax.persistence.AttributeOverrides;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NotNull
    private String name;
    @NotNull
    private String id;
    @NotNull
    private String password;
    @NotNull
    private String email;
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;
    private String profileImg;
    @NotNull
    private String phone;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street_adr", column = @Column(nullable = false)),
            @AttributeOverride(name = "detail_adr", column = @Column(nullable = false)),
            @AttributeOverride(name = "zipcode", column = @Column(nullable = false))
    })
    private Address address;
}
