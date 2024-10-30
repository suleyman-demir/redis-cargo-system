package com.delivery.cargo.chain;

import com.delivery.cargo.model.PackageEntity;

import java.io.Serializable;

public class AcceptedState implements PackageState, Serializable {

    @Override
    public void next(PackageEntity packageEntity) {
        packageEntity.setState(new InTransitState());
        System.out.println("Package is now in transit.");
    }

    @Override
    public void prev(PackageEntity packageEntity) {
        System.out.println("The package is in the initial accepted state.");
    }

    @Override
    public void printStatus() {
        System.out.println("Package accepted.");
    }
}
