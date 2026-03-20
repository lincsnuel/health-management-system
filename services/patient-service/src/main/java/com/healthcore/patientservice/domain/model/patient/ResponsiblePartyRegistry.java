package com.healthcore.patientservice.domain.model.patient;

import com.healthcore.patientservice.domain.model.enums.ResponsiblePartyType;

import java.util.*;

class ResponsiblePartyRegistry {

    private final Map<ResponsiblePartyType, ResponsibleParty> parties = new EnumMap<>(ResponsiblePartyType.class);

    void add(ResponsibleParty party) {
        Objects.requireNonNull(party);
        parties.put(party.getType(), party);
    }

    void remove(ResponsiblePartyType type) {
        parties.remove(type);
    }

    boolean contains(ResponsiblePartyType type) {
        return parties.containsKey(type);
    }

    boolean isEmpty() {
        return parties.isEmpty();
    }

    Collection<ResponsibleParty> all() {
        return Collections.unmodifiableCollection(parties.values());
    }
}