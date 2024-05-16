package com.ajou_nice.with_pet.petsitter.main;

import com.ajou_nice.with_pet.TestConfig;
import com.ajou_nice.with_pet.petsitter.repository.PetSitterRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(TestConfig.class)
public class PetSitterMainRepositoryTest {

    @Autowired
    PetSitterRepository petSitterRepository;




}
