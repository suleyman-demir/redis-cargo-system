package com.delivery.cargo.dto;


import com.delivery.cargo.chain.PackageState;
import com.delivery.cargo.model.PackageEntity;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
@Getter
@Setter
public class KargoDto implements Serializable {
    private static final long serialVersionUID = 1L;
    private String id;

    private String gondericiAdi;
    private String gondericiAdresi;
    private int desi;
    private String aliciAdi;
    private String aliciAdresi;
    private PackageState state;

    public KargoDto(String id, String gondericiAdi, String gondericiAdresi, int desi, String aliciAdi, String aliciAdresi, PackageState state) {
        this.id = id;
        this.gondericiAdi = gondericiAdi;
        this.gondericiAdresi = gondericiAdresi;
        this.desi = desi;
        this.aliciAdi = aliciAdi;
        this.aliciAdresi = aliciAdresi;
        this.state = state;
    }

    public static KargoDto convertKargoDto(PackageEntity from) {
        return new KargoDto(from.getId(), from.getGondericiAdi(), from.getGondericiAdresi(), from.getDesi(), from.getAliciAdi(), from.getAliciAdresi(),from.getState());

    }

    public static PackageEntity convertPackageEntity(KargoDto dto) {
        return new PackageEntity(dto.gondericiAdi, dto.gondericiAdresi, dto.id, dto.desi, dto.aliciAdi, dto.aliciAdresi, dto.state);


    }


}
