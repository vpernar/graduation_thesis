package rs.raf.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import rs.raf.enums.InventoryStatus;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryResponseDto {
    private UUID orderId;
    private Integer userId;
    private Integer productId;
    private InventoryStatus status;
}
