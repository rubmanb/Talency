package com.ruben.rrhh.talency.repository;

import com.ruben.rrhh.talency.entities.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    Subscription findByCompanyId(Long companyId);
}
