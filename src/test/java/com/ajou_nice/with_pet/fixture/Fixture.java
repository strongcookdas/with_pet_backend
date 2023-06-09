package com.ajou_nice.with_pet.fixture;

import com.ajou_nice.with_pet.domain.entity.Dog;
import com.ajou_nice.with_pet.domain.entity.Party;
import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.UserParty;
import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.enums.DogSize;
import com.ajou_nice.with_pet.enums.UserRole;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class Fixture {

    //주소
    private Address address = address = Address.simpleAddressGenerator("123456", "아주로", "팔달관");

    //유저
    private User user1 = User.simpleUserForTest("유저", "testUser1", "password123!", "email@gmail.com",
            UserRole.ROLE_USER, "010-0302-0001", address);
    private User user2 = User.simpleUserForTest("유저2", "testUser2", "password123!", "email@gmail.com",
            UserRole.ROLE_USER, "010-0302-0002", address);
    private User user3 = User.simpleUserForTest("유저3", "testUser3", "password123!", "email@gmail.com",
            UserRole.ROLE_USER, "010-0302-0003", address);

    //파티
    private Party party = Party.of(user1, "party1");

    //반려견
    Dog dog = Dog.simpleDogForTest("흰둥이", "male", party, true, LocalDate.now(), 5.0f, "image",
            "말티즈", "123456789",
            DogSize.소형견);

    //유저파티
    UserParty userParty1 = UserParty.of(user1, party);
    UserParty userParty2 = UserParty.of(user2, party);
}
