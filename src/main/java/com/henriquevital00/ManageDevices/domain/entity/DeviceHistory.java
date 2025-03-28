package com.henriquevital00.ManageDevices.domain.entity;


import com.henriquevital00.ManageDevices.domain.enums.DeviceStateEnum;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class DeviceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, updatable = false)
    private Long deviceId;
    @Column(nullable = false, updatable = false)
    private String name;
    @Column(nullable = false, updatable = false)
    private String brand;
    @Column(nullable = false, updatable = false)
    private DeviceStateEnum state;
    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp;
    @Column(nullable = false, updatable = false)
    private String operationType;

    @PrePersist
    protected void onCreate() {
        timestamp = LocalDateTime.now();
    }
}
