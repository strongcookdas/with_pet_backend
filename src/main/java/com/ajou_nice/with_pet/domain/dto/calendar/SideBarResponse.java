package com.ajou_nice.with_pet.domain.dto.calendar;

import com.ajou_nice.with_pet.domain.dto.category.CategoryResponse;
import com.ajou_nice.with_pet.dog.model.dto.DogSimpleInfoResponse;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class SideBarResponse {

    private List<DogSimpleInfoResponse> dogSimpleInfoResponses;
    private List<CategoryResponse> categoryResponses;

    public static SideBarResponse of(List<DogSimpleInfoResponse> dogSimpleInfoResponses,
            List<CategoryResponse> categoryResponses) {
        return SideBarResponse.builder()
                .dogSimpleInfoResponses(dogSimpleInfoResponses)
                .categoryResponses(categoryResponses)
                .build();
    }
}
