package com.proj242.proj242.repository;

import com.proj242.proj242.entity.Measurement;
import com.proj242.proj242.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MeasurementRepository extends JpaRepository<Measurement, Long> {

    List<Measurement> findAllByUser(User user);
}
