package com.ajou_nice.with_pet.admin.model.dto.get_applicant;

import com.ajou_nice.with_pet.domain.entity.User;
import com.ajou_nice.with_pet.enums.ApplicantStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@SuperBuilder
@ToString
public class AdminGetApplicantBasicResponse {
    protected Long applicantId;
    protected String applicantName;
    protected String applicantEmail;
    protected String applicantImg;
    protected String applicantPhone;
    protected ApplicantStatus applicantStatus;

    public static AdminGetApplicantBasicResponse of(User user){
        return AdminGetApplicantBasicResponse.builder()
                .applicantId(user.getId())
                .applicantName(user.getName())
                .applicantEmail(user.getEmail())
                .applicantImg(user.getProfileImg())
                .applicantPhone(user.getPhone())
                .applicantStatus(user.getApplicantStatus())
                .build();
    }

    public static List<AdminGetApplicantBasicResponse> toList(List<User> users){
        return users.stream().map(applicant-> AdminGetApplicantBasicResponse.builder()
                .applicantName(applicant.getName())
                .applicantId(applicant.getId())
                .applicantImg(applicant.getProfileImg())
                .applicantEmail(applicant.getEmail())
                .applicantPhone(applicant.getPhone())
                .applicantStatus(applicant.getApplicantStatus())
                .build()).collect(Collectors.toList());
    }
}
