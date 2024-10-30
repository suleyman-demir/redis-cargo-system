package com.delivery.cargo.model;

import com.delivery.cargo.chain.AcceptedState;
import com.delivery.cargo.chain.PackageState;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UuidGenerator;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;

@Setter
@Getter
@RedisHash("PackageEntity")
@Data
@EqualsAndHashCode
public class PackageEntity implements Serializable {

    @Id
    private String id;

    private String gondericiAdi;
    private String gondericiAdresi;
    private int desi;
    private String aliciAdi;
    private String aliciAdresi;
    private PackageState state;

    public PackageEntity(String gondericiAdi, String gondericiAdresi, String id, int desi, String aliciAdi, String aliciAdresi, PackageState state) {
        this.gondericiAdi = gondericiAdi;
        this.gondericiAdresi = gondericiAdresi;
        this.id = id;
        this.desi = desi;
        this.aliciAdi = aliciAdi;
        this.aliciAdresi = aliciAdresi;
        this.state=state;
    }
    
    public PackageEntity() {
        // Başlangıç durumu kabul edilmiş olarak belirleniyor.
        this.state = new AcceptedState();
    }

    public void nextState() {
        this.state.next(this);
    }

    public void prevState() {
        this.state.prev(this);
    }

    public void printStatus() {
        this.state.printStatus();
    }
}
