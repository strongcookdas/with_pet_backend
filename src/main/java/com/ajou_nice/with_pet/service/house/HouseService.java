package com.ajou_nice.with_pet.service.house;

import com.ajou_nice.with_pet.domain.entity.House;
import com.ajou_nice.with_pet.domain.entity.PetSitter;
import com.ajou_nice.with_pet.dto.house.HouseRequest;
import com.ajou_nice.with_pet.dto.house.HouseResponse;
import com.ajou_nice.with_pet.repository.HouseRepository;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HouseService {

    private final HouseRepository repository;

    @Transactional
    public List<House> registerAllHouse(PetSitter petSitter, List<HouseRequest> houseRequests) {
        List<House> houses = houseRequests.stream().map(h -> House.of(petSitter, h))
                .collect(Collectors.toList());
        houses = repository.saveAll(houses);
        List<HouseResponse> houseResponses = houses.stream().map(HouseResponse::of).collect(
                Collectors.toList());
        return houses;
    }
}
