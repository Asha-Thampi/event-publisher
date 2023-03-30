package org.messageBroker.model;

import java.io.Serializable;

public class CommunicationContainerModel implements Serializable {

    private int slaveId;
    private int quantity;

    private int[] registerValues;

    public int getSlaveId() {
        return slaveId;
    }

    public void setSlaveId(int slaveId) {
        this.slaveId = slaveId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int[] getRegisterValues() {
        return registerValues;
    }

    public void setRegisterValues(int[] registerValues) {
        this.registerValues = registerValues;
    }
}
