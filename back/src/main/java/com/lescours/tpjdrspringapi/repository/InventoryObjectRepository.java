package com.lescours.tpjdrspringapi.repository;

import com.lescours.tpjdrspringapi.model.Inventory;
import com.lescours.tpjdrspringapi.model.InventoryObject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InventoryObjectRepository extends JpaRepository<InventoryObject, Long> {

    List<InventoryObject> findByInventory(Inventory inventory);

}
