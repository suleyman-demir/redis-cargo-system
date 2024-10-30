package com.delivery.cargo.chain;

import com.delivery.cargo.model.PackageEntity;

import java.io.Serializable;

public class DeliveredState implements PackageState, Serializable {

    @Override
    public void next(PackageEntity packageEntity) {
        System.out.println("Package is already delivered.");
    }

    @Override
    public void prev(PackageEntity packageEntity) {
        packageEntity.setState(new DeliveryState());
        System.out.println("Package reverted to out for delivery.");
    }

    @Override
    public void printStatus() {
        System.out.println("Package delivered.");
    }
}
