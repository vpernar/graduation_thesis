package rs.raf.events.inventory;

import lombok.Data;
import rs.raf.dto.InventoryDto;
import rs.raf.events.Event;

import java.util.Date;
import java.util.UUID;

@Data
public class InventoryEvent implements Event {

    private final UUID eventId = UUID.randomUUID();
    private final Date date = new Date();
    private InventoryDto inventory;
    private InventoryStatus status;

    public InventoryEvent() {
    }

    public InventoryEvent(InventoryDto inventory, InventoryStatus status) {
        this.inventory = inventory;
        this.status = status;
    }

    @Override
    public UUID getEventId() {
        return this.eventId;
    }

    @Override
    public Date getDate() {
        return this.date;
    }

    public InventoryDto getInventory() {
        return inventory;
    }

    public InventoryStatus getStatus() {
        return status;
    }

}
