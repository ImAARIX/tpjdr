package com.lescours.tpjdrspringapi.repository;

import com.lescours.tpjdrspringapi.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Inventory findInventoryById(Long id);
    boolean existsInventoryById(Long id);
}
