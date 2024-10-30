package com.delivery.cargo.service;

import com.delivery.cargo.dto.KargoDto;
import com.delivery.cargo.model.PackageEntity;
import com.delivery.cargo.repository.KargoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class KargoService {

    private final KargoRepository kargoRepository;

    private final RedisTemplate<String, PackageEntity> redisTemplate;
    private static final String REDIS_KEY = "kargo";
    private final WebApplicationContext webApplicationContext;


    public KargoService(KargoRepository kargoRepository, RedisTemplate<String, PackageEntity> redisTemplate, WebApplicationContext webApplicationContext) {
        this.kargoRepository = kargoRepository;


        this.redisTemplate = redisTemplate;
        this.webApplicationContext = webApplicationContext;
    }

    private static final Logger logger = LoggerFactory.getLogger(KargoService.class);



    public PackageEntity addKargo(KargoDto kargoDto) {

        logger.info("Yeni kargo ekleniyor: {}", kargoDto.getId());
        PackageEntity kargo = KargoDto.convertPackageEntity(kargoDto);
        redisTemplate.opsForHash().put("kargo", kargoDto.getId(), kargo);
        kargoRepository.save(kargo);
        return kargo;
    }


    public ResponseEntity<PackageEntity> updateKargoStatus(Long id) throws Exception {
        webApplicationContext.getBean(KargoService.class).deleteKargoFromCache(id);
        PackageEntity packageEntity = kargoRepository.findById(id)
                .orElseThrow(() -> new Exception("Package not found with ID: " + id));
//        redisTemplate.opsForHash().rem("kargoCache", id,packageEntity);
//        deleteKargoFromCache(id);

        packageEntity.nextState();
        packageEntity.printStatus();

        kargoRepository.save(packageEntity);
        redisTemplate.opsForHash().put(String.valueOf(id), id, packageEntity);
        redisTemplate.opsForHash().put("kargoCache", id, packageEntity);




        return ResponseEntity.ok(packageEntity);
    }



//    public KargoDto getKargoById(Long id) throws Exception {
//        logger.info("Cache'te bulunamadı, Redis veya veritabanından veri çekiliyor: ID={}", id);
//        logger.info("ID'si {} olan kargo getiriliyor.", id);
//        PackageEntity packageEntity = kargoRepository.findById(id)
//                .orElseThrow(() -> new Exception("Bu ID'ye sahip kargo bulunamadı: " + id));
//        redisTemplate.opsForHash().put(REDIS_KEY, id, packageEntity);
//        return KargoDto.convertKargoDto(kargoRepository.findById(id).orElseThrow(()-> new ChangeSetPersister.NotFoundException()));
//    }

    @Cacheable(value = "kargoCache", key = "#id")
    public KargoDto getKargoById(Long id) throws Exception {
        logger.info("Cache'te bulunamadı, Redis veritabanından veri çekiliyor: ID={}", id);
        logger.info("ID'si {} olan kargo getiriliyor.", id);
        KargoDto packageEntity = KargoDto.convertKargoDto(kargoRepository.findById(id)
                .orElseThrow(() -> new Exception("Bu ID'ye sahip kargo bulunamadı: " + id)));
        redisTemplate.opsForHash().put(REDIS_KEY, id, packageEntity);
        return KargoDto.convertKargoDto(kargoRepository.findById(id)
                .orElseThrow(()->new IllegalArgumentException("Bu idli kargo yok ve getirilemedi (getkargobyId)")));

    }


    @Cacheable(value = "kargoCache", key = "'allCargos'")

    public List<KargoDto> getAllCargos() {
        logger.info("Cache'te bulunamadı, Redis veya veritabanından veri çekiliyor: ");
        logger.info("Tüm kargolar getiriliyor.");
        return kargoRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());

    }

//    public List<KargoDto> getCargosByIds(List<Long> idd) {
//        List<PackageEntity> cargos = kargoRepository1.findAllByIdIn(idd);
//        return cargos.stream().map(this::convertToDto).collect(Collectors.toList());
//    }

    public List<KargoDto> getCargosByIds(List<Long> idd) {
        List<PackageEntity> cargos = new ArrayList<>();

        for (Long id : idd) {
            PackageEntity cargo = (PackageEntity) redisTemplate.opsForHash().get(REDIS_KEY, String.valueOf(id));
            if (cargo != null) {
                cargos.add(cargo);
            }
        }

        return cargos.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private KargoDto convertToDto(PackageEntity packageEntity) {
        return new KargoDto(
                packageEntity.getId(),
                packageEntity.getGondericiAdi(),
                packageEntity.getGondericiAdresi(),
                packageEntity.getDesi(),

                packageEntity.getAliciAdi(),
                packageEntity.getAliciAdresi(),
                packageEntity.getState()
        );
    }
    @CacheEvict(value = "kargoCache", allEntries = true)
    public void clearKargoCache() {
        System.out.println("Kargo cache temizlendi.");
    }


    @CacheEvict(value = "kargoCache", key = "#id")
    public void deleteKargoFromCache(Long id) {
        logger.info("cacheden siliniyor {}",id);

    }


}
