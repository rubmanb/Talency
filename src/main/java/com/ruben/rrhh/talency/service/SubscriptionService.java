package com.ruben.rrhh.talency.service;

import com.ruben.rrhh.talency.entities.Company;
import com.ruben.rrhh.talency.entities.Subscription;

public interface SubscriptionService {
    Subscription createInitialSubscription(Company company);
}
