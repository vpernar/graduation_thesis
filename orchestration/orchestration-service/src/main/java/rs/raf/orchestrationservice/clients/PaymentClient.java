package rs.raf.orchestrationservice.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import rs.raf.dto.InventoryRequestDto;
import rs.raf.dto.InventoryResponseDto;
import rs.raf.dto.PaymentRequestDto;
import rs.raf.dto.PaymentResponseDto;

@FeignClient(value = "payment", url = "${service.endpoint.payment}")
public interface PaymentClient {

    @PostMapping("debit")
    ResponseEntity<PaymentResponseDto> debit(@RequestBody PaymentRequestDto paymentRequestDto);

    @PostMapping("credit")
    ResponseEntity<PaymentResponseDto> credit(@RequestBody PaymentRequestDto paymentRequestDto);
}
