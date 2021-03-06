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

import org.ameba.integration.jpa.ApplicationEntity;
import org.openwms.values.Problem;
import org.openwms.wms.order.OrderState;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

/**
 * A ReceivingOrder.
 * 
 * @author Heiko Scherrer
 */
@Entity
@Table(name = "WMS_REC_ORDER",
        uniqueConstraints = @UniqueConstraint(name = "UC_REC_ORDER_ID", columnNames = { "C_ORDER_ID" })
)
public class ReceivingOrder extends ApplicationEntity implements Serializable {

    /** Unique order id, business key. */
    @Column(name = "C_ORDER_ID", nullable = false)
    private String orderId;

    /** Current state of this order. */
    @Enumerated(EnumType.STRING)
    @Column(name = "C_ORDER_STATE")
    private OrderState orderState = OrderState.UNDEFINED;

    /**
     * Property to lock/unlock an order.
     * <ul>
     * <li>true: locked</li>
     * <li>false: unlocked</li>
     * </ul>
     * Default is {@value}
     */
    @Column(name = "C_LOCKED")
    private boolean locked = false;

    /** Current priority of the order. */
    @Column(name = "C_PRIORITY")
    private int priority;

    /** Latest date of this order can be processed. */
    @Column(name = "C_LATEST_DUE")
    private ZonedDateTime latestDueDate;

    /** Earliest date the order can be started. */
    @Column(name = "C_START_DATE")
    private ZonedDateTime startDate;

    /** Date when the order should be allocated. */
    @Column(name = "C_NEXT_ALLOC")
    private ZonedDateTime nextAllocationDate;

    /** Latest problem that is occurred on this order. */
    @Embedded
    private Problem problem;

    /** All ReceivingOrderPosition this order has. */
    @OneToMany(mappedBy = "order", cascade = {ALL}, fetch = FetchType.EAGER)
    @OrderBy("posNo")
    private Set<ReceivingOrderPosition> positions = new HashSet<>();

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name = "WMS_REC_ORDER_DETAILS",
            joinColumns = {
                    @JoinColumn(name = "C_ORDER_ID", referencedColumnName = "C_ORDER_ID")
            },
            foreignKey = @ForeignKey(name = "FK_DETAILS_RO")
    )
    @MapKeyColumn(name = "C_KEY")
    @Column(name = "C_VALUE")
    private Map<String, String> details;

    /*~ -------------- Constructors -------------- */
    /** Used by the JPA provider. */
    protected ReceivingOrder() {}

    protected ReceivingOrder(String orderId) {
        this.orderId = orderId;
    }
    /*~ --------------- Lifecycle ---------------- */
    @PrePersist
    protected void prePersist() {
        this.orderState = OrderState.CREATED;
    }

    public String getOrderId() {
        return orderId;
    }

    public OrderState getOrderState() {
        return orderState;
    }

    public void setOrderState(OrderState orderState) {
        this.orderState = orderState;
    }

    public boolean isLocked() {
        return locked;
    }

    public int getPriority() {
        return priority;
    }

    public ZonedDateTime getLatestDueDate() {
        return latestDueDate;
    }

    public ZonedDateTime getStartDate() {
        return startDate;
    }

    public ZonedDateTime getNextAllocationDate() {
        return nextAllocationDate;
    }

    public Problem getProblem() {
        return problem;
    }

    public Set<ReceivingOrderPosition> getPositions() {
        return positions == null ? Collections.emptySet() : positions;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return orderId;
    }
}