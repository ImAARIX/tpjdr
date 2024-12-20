package com.lescours.tpjdrspringapi.repository;

import com.lescours.tpjdrspringapi.model.Monster;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MonsterRepository extends JpaRepository<Monster, Long> {

    Monster findMonsterById(Long id);

}
