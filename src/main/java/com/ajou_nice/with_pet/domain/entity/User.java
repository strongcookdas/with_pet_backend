package com.ajou_nice.with_pet.domain.entity;

import com.ajou_nice.with_pet.domain.dto.auth.UserSignUpRequest;
import com.ajou_nice.with_pet.dto.applicant.ApplicantCreateRequest;
import com.ajou_nice.with_pet.dto.applicant.ApplicantCreateRequest.ApplicantModifyRequest;
import com.ajou_nice.with_pet.domain.dto.user.MyInfoModifyRequest;
import com.ajou_nice.with_pet.domain.entity.embedded.Address;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import com.ajou_nice.with_pet.enums.Gender;
import com.ajou_nice.with_pet.enums.UserRole;
import java.time.LocalDate;
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
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Getter
@Table(name = "users")
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    private String id;
    @NotNull
    private String name;
    @NotNull
    private String password;
    @NotNull
    private String email;
    @NotNull
    @Enumerated(EnumType.STRING)
    private UserRole role;
    @Lob
    private String profileImg;
    @NotNull
    private String phone;
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "streetAdr", column = @Column(nullable = false)),
            @AttributeOverride(name = "detailAdr", column = @Column(nullable = false)),
            @AttributeOverride(name = "zipcode", column = @Column(nullable = false))
    })
    private Address address;

    private LocalDate birth;
    private Boolean isSmoking;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    private Boolean havingWithPet;
    @Lob
    private String animalCareer;
    @Lob
    private String motivation;
    private String licenseImg;

    @Enumerated(EnumType.STRING)
    private ApplicantStatus applicantStatus;

    //몇번 지원했는지 count
    private Integer applicantCount;
    private Integer partyCount;

    public void updateUserRole(UserRole userRole) {
        this.role = userRole;
    }

    public void updateUser(MyInfoModifyRequest modifyRequest, BCryptPasswordEncoder encoder) {
        this.name = modifyRequest.getUserName();
        this.password = encoder.encode(modifyRequest.getUserPassword());
        this.email = modifyRequest.getUserEmail();
        this.profileImg = modifyRequest.getProfileImg();
        this.phone = modifyRequest.getPhoneNum();
        this.address = Address.toAddressEntity(modifyRequest.getAddress());
    }

    public void updateApplicantCount() {
        applicantCount++;
    }

    public void updateApplicantStatus(ApplicantStatus status) {
        this.applicantStatus = status;
    }

    public void registerApplicantInfo(ApplicantCreateRequest applicantCreateRequest) {
        this.licenseImg = applicantCreateRequest.getLicenseImg();
        this.birth = applicantCreateRequest.getBirth();
        this.isSmoking = applicantCreateRequest.getIsSmoking();
        this.havingWithPet = applicantCreateRequest.getHavingWithPet();
        this.gender = applicantCreateRequest.getGender();
        this.animalCareer = applicantCreateRequest.getAnimalCareer();
        this.motivation = applicantCreateRequest.getMotivation();
    }

    public void updateApplicantInfo(ApplicantModifyRequest applicantModifyRequest) {
        this.animalCareer = applicantModifyRequest.getApplicant_animal_career();
        this.motivation = applicantModifyRequest.getApplicant_motivate();
    }

    public static User simpleUserForTest(String userName, String userId, String password,
            String email, UserRole userRole, String phone,
            Address address) {
        return User.builder()
                .name(userName)
                .password(password)
                .email(email)
                .role(userRole)
                .phone(phone)
                .address(address)
                .build();
    }

    public static User simpleUserForTest(String userName, String userId, String password,
            String email, UserRole userRole, String phone,
            Address address, Integer partyCount) {
        return User.builder()
                .name(userName)
                .password(password)
                .email(email)
                .role(userRole)
                .phone(phone)
                .address(address)
                .partyCount(partyCount)
                .build();
    }

    public static User toUserEntity(UserSignUpRequest userSignUpRequest,
            BCryptPasswordEncoder encoder) {

        //이미지 null 체크 null이면 기본이미지로 insert
        String img = userSignUpRequest.getProfileImg();
        if (userSignUpRequest.getProfileImg() == null || userSignUpRequest.getProfileImg()
                .isEmpty()) {
            img = "https://withpetoriginimage.s3.ap-northeast-1.amazonaws.com/default.png";
        }

        return User.builder()
                .name(userSignUpRequest.getName())
                .password(encoder.encode(userSignUpRequest.getPassword()))
                .email(userSignUpRequest.getEmail())
                .role(UserRole.ROLE_USER)
                .profileImg(img)
                .phone(userSignUpRequest.getPhone())
                .address(Address.toAddressEntity(userSignUpRequest.getAddress()))
                .applicantCount(0)
                .partyCount(0)
                .build();
    }

    public static User createTestEntity() {

        return User.builder()
                .userId(1L)
                .name("홍길동")
                .password("password")
                .email("email@gmail.com")
                .role(UserRole.ROLE_USER)
                .profileImg("image")
                .phone("010-0000-0000")
                .address(Address.builder()
                        .zipcode("123456")
                        .streetAdr("아주대로")
                        .detailAdr("팔달관")
                        .build())
                .applicantCount(0)
                .partyCount(0)
                .build();
    }

    public void updatePartyCount(Integer partyCount) {
        this.partyCount = partyCount;
    }
}
