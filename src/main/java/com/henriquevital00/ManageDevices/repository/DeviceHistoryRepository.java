package com.henriquevital00.ManageDevices.repository;

import com.henriquevital00.ManageDevices.domain.entity.DeviceHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeviceHistoryRepository extends JpaRepository<DeviceHistory, Long> {
}
