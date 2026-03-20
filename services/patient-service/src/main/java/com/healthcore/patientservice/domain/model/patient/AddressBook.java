package com.healthcore.patientservice.domain.model.patient;

import com.healthcore.patientservice.domain.exception.AddressLimitExceededException;
import com.healthcore.patientservice.domain.exception.AddressNotFoundException;
import com.healthcore.patientservice.domain.exception.CannotRemovePrimaryAddressException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

class AddressBook {

    private final List<Address> addresses = new ArrayList<>();

    void add(Address address) {

        Objects.requireNonNull(address);

        if (addresses.size() >= 3) {
            throw new AddressLimitExceededException("Maximum of 3 addresses allowed");
        }

        if (address.isPrimary()) {

            List<Address> updated = addresses.stream()
                    .map(Address::unsetPrimary)
                    .toList();

            addresses.clear();
            addresses.addAll(updated);

        } else if (addresses.isEmpty()) {
            address = address.setPrimary();
        }

        addresses.add(address);
    }

    void remove(Address address) {

        if (!addresses.contains(address)) {
            throw new AddressNotFoundException("Address not found");
        }

        if (address.isPrimary() && addresses.size() == 1) {
            throw new CannotRemovePrimaryAddressException(
                    "Add another address before removing primary"
            );
        }

        addresses.remove(address);
    }

    void setPrimary(Address address) {

        if (!addresses.contains(address)) {
            throw new AddressNotFoundException("Address not found");
        }

        List<Address> updated = addresses.stream()
                .map(Address::unsetPrimary)
                .toList();

        addresses.clear();
        addresses.addAll(updated);

        int index = addresses.indexOf(address.unsetPrimary());
        addresses.set(index, address.setPrimary());
    }

    List<Address> all() {
        return Collections.unmodifiableList(addresses);
    }
}