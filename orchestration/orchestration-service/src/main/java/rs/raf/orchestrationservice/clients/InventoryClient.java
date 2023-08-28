package rs.raf.orchestrationservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rs.raf.dto.InventoryRequestDto;
import rs.raf.dto.InventoryResponseDto;

@FeignClient(value = "invenotry", url = "${service.endpoint.inventory}")
public interface InventoryClient {

    @PostMapping("deduct")
    ResponseEntity<InventoryResponseDto> deduct(@RequestBody InventoryRequestDto inventoryRequestDto);

    @PostMapping("add")
    ResponseEntity<InventoryResponseDto> add(@RequestBody InventoryRequestDto inventoryRequestDto);
}
