/*
 * Copyright 2005-2020 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.openwms.wms.receiving.impl;

import org.ameba.exception.ResourceExistsException;
import org.ameba.exception.ServiceLayerException;
import org.junit.jupiter.api.Test;
import org.openwms.wms.receiving.ReceivingApplicationTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * A ReceivingServiceImplTest.
 *
 * @author Heiko Scherrer
 */
@ReceivingApplicationTest
@Transactional
@Rollback
class ReceivingServiceImplTest {

    @Autowired
    private ReceivingServiceImpl service;

    @Test void createOrderWithNull() {
        ServiceLayerException ex = assertThrows(ServiceLayerException.class, () -> service.createOrder(null));
        assertThat(ex.getMessage()).containsIgnoringCase("order");
    }

    @Test void createOrder() {
        ReceivingOrder order = service.createOrder(new ReceivingOrder("4710"));
        assertThat(order.isNew()).isFalse();

        ResourceExistsException ex = assertThrows(ResourceExistsException.class, () -> service.createOrder(new ReceivingOrder("4710")));
        assertThat(ex.getMessage()).containsIgnoringCase("exists");
    }
}