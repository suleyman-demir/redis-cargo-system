package com.delivery.cargo.chain;

import com.delivery.cargo.model.PackageEntity;

import java.io.Serializable;

public class InTransitState implements PackageState, Serializable {

    @Override
    public void next(PackageEntity packageEntity) {
        packageEntity.setState(new DeliveryState());
        System.out.println("Package is now out for delivery.");
    }

    @Override
    public void prev(PackageEntity packageEntity) {
        packageEntity.setState(new AcceptedState());
        System.out.println("Package reverted to accepted state.");
    }

    @Override
    public void printStatus() {
        System.out.println("Package in transit.");
    }
}
