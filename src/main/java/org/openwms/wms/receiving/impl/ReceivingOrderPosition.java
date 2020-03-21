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

import org.ameba.integration.jpa.BaseEntity;
import org.openwms.core.units.api.Measurable;
import org.openwms.wms.inventory.Product;
import org.openwms.wms.order.OrderState;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.ZonedDateTime;

/**
 * A ReceivingOrderPosition.
 * 
 * @author Heiko Scherrer
 */
@Entity
@Table(name = "WMS_REC_ORDER_POSITION")
public class ReceivingOrderPosition extends BaseEntity implements Serializable {

    @ManyToOne(optional = false)
    @JoinColumn(name = "C_ORDER_ID", foreignKey = @ForeignKey(name = "FK_POS_ORDER_ID"))
    private ReceivingOrder order;

    /** The position number is a unique index within a single {@link ReceivingOrder} instance. */
    @Column(name = "C_POS_NO", nullable = false)
    private int posNo;

    /** Current position state. Default is {@value} */
    @Enumerated(EnumType.STRING)
    @Column(name = "C_STATE")
    private OrderState state = OrderState.UNDEFINED;

    @org.hibernate.annotations.Type(type = "org.openwms.core.units.persistence.UnitUserType")
    @org.hibernate.annotations.Columns(columns = {
            @Column(name = "C_QTY_EXPECTED_TYPE", nullable = false),
            @Column(name = "C_QTY_EXPECTED", nullable = false)
    })
    private Measurable qtyExpected;

    /** The ordered {@link Product} identified by it's SKU. */
    @ManyToOne
    @JoinColumn(name = "C_SKU", referencedColumnName = "C_SKU", foreignKey = @ForeignKey(name = "FK_POS_PRODUCT"))
    private Product product;

    /** The barcode of an expected {@code TransportUnit} to be received. */
    private String transportUnitBK;

    /** Latest finish date of this Order. */
    @Column(name = "C_LATEST_DUE")
    private ZonedDateTime latestDueDate;

    /** Used by the JPA provider. */
    protected ReceivingOrderPosition() {}

    public String getTransportUnitBK() {
        return transportUnitBK;
    }
}