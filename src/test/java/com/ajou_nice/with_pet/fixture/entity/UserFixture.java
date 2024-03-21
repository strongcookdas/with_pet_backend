package com.ajou_nice.with_pet.fixture.entity;

import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import com.ajou_nice.with_pet.enums.Gender;
import com.ajou_nice.with_pet.enums.UserRole;

import java.time.LocalDate;

public class UserFixture {
    public static User createUser(Long id, String name, String email, String password, UserRole userRole,
                                  String profileImg, String phone) {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .role(userRole)
                .profileImg(profileImg)
                .phone(phone)
                .build();
    }

    public static User createUserByApplication(Long id, String name, String email, String password, UserRole userRole,
                                               String profileImg, String phone, Address address, LocalDate birth, Boolean isSmoking, Gender gender, Boolean havingWithPet, String animalCareer,
                                               String motivation, String licenseImg, ApplicantStatus applicantStatus, Integer applicantCount) {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .role(userRole)
                .profileImg(profileImg)
                .phone(phone)
                .address(address)
                .birth(birth)
                .isSmoking(isSmoking)
                .gender(gender)
                .havingWithPet(havingWithPet)
                .animalCareer(animalCareer)
                .motivation(motivation)
                .licenseImg(licenseImg)
                .applicantCount(applicantCount)
                .applicantStatus(applicantStatus)
                .build();
    }

    public static User createUserWithApplicantStatus(Long id, String name, String email, String password, UserRole userRole,
                                                     String profileImg, String phone, Integer applicationCount, ApplicantStatus applicantStatus, Address address) {
        return User.builder()
                .id(id)
                .name(name)
                .email(email)
                .password(password)
                .role(userRole)
                .profileImg(profileImg)
                .phone(phone)
                .applicantCount(applicationCount)
                .applicantStatus(applicantStatus)
                .address(address)
                .build();
    }
}
