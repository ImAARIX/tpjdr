package com.lescours.tpjdrspringapi.repository;

import com.lescours.tpjdrspringapi.model.Character;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CharacterRepository extends JpaRepository<com.lescours.tpjdrspringapi.model.Character, Long> {

    Character findCharacterByUsername(String username);
    boolean existsCharacterByUsername(String username);

}
