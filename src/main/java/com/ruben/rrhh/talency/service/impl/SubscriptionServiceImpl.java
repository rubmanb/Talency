package com.ruben.rrhh.talency.service.impl;

import com.ruben.rrhh.talency.entities.Company;
import com.ruben.rrhh.talency.entities.Subscription;
import com.ruben.rrhh.talency.entities.SubscriptionPlan;
import com.ruben.rrhh.talency.entities.SubscriptionStatus;
import com.ruben.rrhh.talency.repository.SubscriptionRepository;
import com.ruben.rrhh.talency.service.SubscriptionService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class SubscriptionServiceImpl implements SubscriptionService {

    private final SubscriptionRepository repository;

    public SubscriptionServiceImpl(SubscriptionRepository repository) {
        this.repository = repository;
    }

    @Override
    public Subscription createInitialSubscription(Company company) {
        Subscription subscription = Subscription.builder()
                .company(company)
                .plan(SubscriptionPlan.FREE)
                .status(SubscriptionStatus.ACTIVE)
                .startDate(LocalDate.now())
                .build();

        return repository.save(subscription);
    }
}
