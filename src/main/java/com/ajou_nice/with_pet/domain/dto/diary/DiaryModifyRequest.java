package com.ajou_nice.with_pet.domain.dto.diary;

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
public class DiaryModifyRequest {

    @NotBlank(message = "제목을 입력해주세요.")
    private String title;
    @NotNull(message = "카테고리를 선택해주세요.")
    private Long categoryId;
    @NotBlank(message = "내용을 입력해주세요.")
    private String contentBody;
    private String dogImgToday;
    @NotNull(message = "날짜를 입력해주세요.")
    private LocalDate createdAt;

}
