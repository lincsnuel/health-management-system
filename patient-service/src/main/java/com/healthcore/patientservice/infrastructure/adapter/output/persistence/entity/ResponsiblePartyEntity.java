package com.healthcore.patientservice.infrastructure.adapter.output.persistence.entity;

import com.healthcore.patientservice.domain.model.enums.ResponsiblePartyType;
import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "responsible_party")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponsiblePartyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Column(nullable = false, length = 50)
    private String firstName;

    @Column(length = 50)
    private String lastName;

    @Column(length = 15, nullable = false)
    private String contactNumber;

    @Column(length = 100)
    private String relationship;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private ResponsiblePartyType type;

    /*
        Responsible Party Address
     */
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "address_id", nullable = false)
    private ResponsiblePartyAddressEntity address;

    /*
        Owning Patient
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "patient_id")
    private PatientEntity patient;

    /*
        Domain-style helper method
     */
    public void addAddress(ResponsiblePartyAddressEntity address) {
        this.address = address;
    }

}