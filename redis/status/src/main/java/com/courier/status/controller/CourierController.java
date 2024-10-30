package com.courier.status.controller;

import com.courier.status.client.CargoServiceClient;
import com.courier.status.dto.CargoDto;
import com.courier.status.dto.CourierDto;
import com.courier.status.dto.CourierWithCargoDto;
import com.courier.status.model.CourierEntity;
import com.courier.status.service.CourierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/api/deliveryy")
@Slf4j
public class CourierController {

    private final CargoServiceClient cargoServiceClient;
    private final CourierService courierService;


    public CourierController(CargoServiceClient cargoServiceClient, CourierService courierService) {
        this.cargoServiceClient = cargoServiceClient;
        this.courierService = courierService;

    }


    @PostMapping("/kuryeekle")
    public ResponseEntity<CourierDto> addCourier(@RequestBody CourierDto courierDto) {
        log.info("Courier added: " + courierDto);
        CourierEntity courierEntity = courierService.addCourier(courierDto);
        return ResponseEntity.ok(CourierDto.convert(courierEntity));
    }

    @GetMapping("/kuryegetir/{courierId}")
    public ResponseEntity<CourierDto> getCourier(@PathVariable Long courierId) {
        log.info("Courier found: " + courierId);
        try {
            CourierEntity courierEntity = courierService.getCourierById(courierId).getBody();
            if (courierEntity != null) {
                CourierDto courierDto = CourierDto.convert(courierEntity);
                return ResponseEntity.ok(courierDto);
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            log.error("kurye bulunamadı : {}", courierId, e);
            throw new RuntimeException(e);
        }


    }

    @GetMapping("/cargos")
    public ResponseEntity<List<CargoDto>> getAllCargos() {
        log.info("Fetching all cargos via courier service");
        return cargoServiceClient.getAllCargos(); // Feign Client üzerinden cargo mikroservisinden verileri alır
    }

    @PostMapping("/setState/{id}")
    public ResponseEntity<CargoDto> setState(@PathVariable Long id) {
        log.info("Set state via courier service");
        return cargoServiceClient.setState(id);
    }

    @GetMapping("/cargo/{id}")
    public CargoDto getCargoById(@PathVariable Long id) {
        log.info("Fetch cargo by id via courier service");
        return cargoServiceClient.getCargoById(id);
    }

    @PostMapping("/courier/addcargoid")
    public ResponseEntity<Void> addCargoOnCourierIds(@RequestParam Long courierId, @RequestParam List<Long> id) throws Exception {
        log.info("Add cargo id via courier service");
        courierService.addCargoOnCourierIds(courierId,id);
        return ResponseEntity.ok().build();

    }

    @PostMapping("/byIds")
    public ResponseEntity<List<CargoDto>> findByIds(@RequestBody List<Long> ids) {
        log.info("Fetch cargo by id via courier service");
        return cargoServiceClient.findByIds(ids);
    }

    @GetMapping("/{courierId}/cargos")
    public CourierWithCargoDto getCourierWithCargos(@PathVariable Long courierId) throws Exception {

        CourierDto courierDto = CourierDto.convert(courierService.getCourierById(courierId).getBody());


        List<CargoDto> cargos = cargoServiceClient.findByIds(courierDto.couriersCargosIds()).getBody();


        return new CourierWithCargoDto(courierDto, cargos);
    }
}
