package com.ajou_nice.with_pet.dto.petsitter;

import com.ajou_nice.with_pet.dto.hashtag.HashTagRequest;
import com.ajou_nice.with_pet.dto.house.HouseRequest;
import java.util.List;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@ToString
public class PetSitterCreateRequest {

    private List<HouseRequest> houseList;
    private List<HashTagRequest> hashTagRequestList;
    private String introduction;
    
}
