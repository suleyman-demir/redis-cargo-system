package com.delivery.cargo.controller;

import com.delivery.cargo.dto.KargoDto;
import com.delivery.cargo.model.PackageEntity;
import com.delivery.cargo.service.KargoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/v1/api/delivery")
public class KargoController {


    private final KargoService kargoService;

    public KargoController(KargoService kargoService) {
        this.kargoService = kargoService;
    }


//    @GetMapping("/{id}")
//    public ResponseEntity<KargoDto> getKargoById(@PathVariable Long id) {
//        log.info("Kargo getiriliyor: {}", id);
//        try {
//            Optional<ResponseEntity<KargoEntity>> kargoDtoOptional = Optional.ofNullable(kargoService.getKargoById(id));
//            if (kargoDtoOptional.isPresent()) {
//                return ResponseEntity.ok(KargoDto.convert(kargoDtoOptional.get().getBody()));
//            }
//        } catch (Exception e) {
//            log.error("Kargo getirilemedi: {}", id, e);
//            throw new RuntimeException(e);
//        }
//        return ResponseEntity.notFound().build();
//    }

//    @GetMapping("/cargo/{id}")
//    public ResponseEntity<KargoDto> getCargoById(@PathVariable Long id) {
//        log.info("Kargo getiriliyor: {}", id);
//        try {
//            KargoDto packageEntity = kargoService.getKargoById(id);
//            if (packageEntity != null) {
//                //KargoDto kargoDto = KargoDto.convertKargoDto(packageEntity);
//                return ResponseEntity.ok(packageEntity);
//            } else {
//                return ResponseEntity.notFound().build();
//            }
//        } catch (Exception e) {
//            log.error("Kargo getirilemedi: {}", id, e);
//            throw new RuntimeException(e);
//        }
//    }

    @GetMapping("/cargo/{id}")
    public ResponseEntity<KargoDto> getCargoById(@PathVariable Long id) {
        log.info("Kargo getiriliyor: {}", id);
        try {
            KargoDto packageEntity = kargoService.getKargoById(id);
            if (packageEntity != null) {
                return ResponseEntity.ok(packageEntity);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("Kargo getirilemedi: {}", id, e);
            throw new RuntimeException(e);
        }
    }


    @PostMapping("/add")
    public ResponseEntity<KargoDto> addKargo(@RequestBody KargoDto kargoDto) {
        log.info("Kargo add: {}", kargoDto);
        PackageEntity packageEntity = kargoService.addKargo(kargoDto);
        return ResponseEntity.ok(KargoDto.convertKargoDto(packageEntity));
    }


    @PostMapping("setState/{id}")
    public ResponseEntity<PackageEntity> updateKargoStatus(@PathVariable Long id) throws Exception {
        log.info("Kargo durumu güncelleniyor: {}", id);
        PackageEntity updatedPackage = kargoService.updateKargoStatus(id).getBody();
        return ResponseEntity.ok(updatedPackage);
    }

    @GetMapping("/cargos")
    public ResponseEntity<List<KargoDto>> getAllCargos() {
        log.info("Tüm kargolar getiriliyor");
        List<KargoDto> kargoDtos=kargoService.getAllCargos();
        if (kargoDtos != null && !kargoDtos.isEmpty()) {
            return ResponseEntity.ok(kargoDtos);
        }else {
            return ResponseEntity.noContent().build();


        }
    }
    @PostMapping("/byIds")
    public List<KargoDto> getCargosByIds(@RequestBody List<Long> cargoIds) {
        return kargoService.getCargosByIds(cargoIds);
    }


    @DeleteMapping("/clear")
    public String clearCache() {
        kargoService.clearKargoCache();
        return "Cache başarıyla temizlendi.";
    }

}