package com.ajou_nice.with_pet.diary.model.dto.user;

import java.time.LocalDate;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
@ToString
public class UserDiaryPostRequest {

    @NotNull(message = "반려견을 선택해주세요.")
    private Long userDiaryDogId;
    @NotBlank(message = "제목을 입력해주세요.")
    private String userDiaryTitle;
    @NotNull(message = "카테고리를 선택해주세요.")
    private Long userDiaryCategoryId;
    @NotBlank(message = "내용을 입력해주세요.")
    private String userDiaryContent;
    private String userDiaryDogImg;
    @NotNull(message = "날짜를 입력해주세요.")
    private LocalDate userDiaryCreatedAt;

}
