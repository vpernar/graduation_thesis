package rs.raf.inventoryservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.dto.InventoryRequestDto;
import rs.raf.dto.InventoryResponseDto;
import rs.raf.inventoryservice.service.InventoryService;

@RestController
@RequestMapping("inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final InventoryService inventoryService;

    @PostMapping("deduct")
    public ResponseEntity<InventoryResponseDto> deduct(@RequestBody final InventoryRequestDto inventoryRequestDto){
        return ResponseEntity.ok(inventoryService.deductInventory(inventoryRequestDto));
    }

    @PostMapping("add")
    public ResponseEntity<Void> add(@RequestBody final InventoryRequestDto inventoryRequestDto){
        inventoryService.addInventory(inventoryRequestDto);
        return ResponseEntity.status(200).build();
    }

}
