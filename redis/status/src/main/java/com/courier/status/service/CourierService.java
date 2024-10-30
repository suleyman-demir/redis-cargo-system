package com.courier.status.service;

import com.courier.status.client.CargoServiceClient;
import com.courier.status.dto.CourierDto;
import com.courier.status.model.CourierEntity;
import com.courier.status.repository.CourierRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CourierService {


    private final CourierRepository courierRepository;

    private final CargoServiceClient cargoServiceClient;


    public CourierService(CourierRepository courierRepository, CargoServiceClient cargoServiceClient) {
        this.courierRepository = courierRepository;
        this.cargoServiceClient = cargoServiceClient;
    }




    public CourierEntity addCourier(CourierDto courierDto) {
        log.info("Adding Courier: {}", courierDto.courierId() + courierDto.courierName());
        CourierEntity courierEntity = CourierDto.convert(courierDto);
        return courierRepository.save(courierEntity);

    }

    public ResponseEntity<CourierEntity> getCourierById(Long courierId) throws Exception {
        log.info("Getting Courier: {}", courierId);
        CourierEntity courierEntity = courierRepository.findByCourierId(courierId)
                .orElseThrow(() -> new Exception("No courier with id: {}" + courierId));
        return ResponseEntity.ok(courierEntity);
    }



    public void addCargoOnCourierIds(Long courierId, List<Long> id) throws Exception {
        log.info("Adding Cargo :{}  On Courier: {}", id.toString() , courierId);
        CourierEntity courierEntity =courierRepository.findByCourierId(courierId)
                .orElseThrow(() -> new Exception("No courier with id: {}" + courierId));
         courierEntity.setCouriersCargosIds(id);
        courierRepository.save(courierEntity);
        log.info("Cargo {} added on Courier: {}",id ,courierId);


    }





//    public List<CargoDto> getAllCargos() {
//        log.info("Getting all cargos");
//        return cargoServiceClient.getAllCargos().getBody();
//
//    }
//    public CargoDto getCargoById(Long cargoId) {
//        log.info("Getting cargo by id: {}", cargoId);
//        CargoDto cargoDto=cargoServiceClient.getCargoById(cargoId);
//
//         return ResponseEntity.ok(cargoDto).getBody();
//   }


}

