package com.delivery.cargo.repository;

import com.delivery.cargo.dto.KargoDto;
import com.delivery.cargo.model.PackageEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface KargoRepository extends CrudRepository<PackageEntity, String> {
    Optional<PackageEntity> findById(Long id);
    Optional<PackageEntity> getKargoById(Long id);
    List<PackageEntity> findAll();









}
