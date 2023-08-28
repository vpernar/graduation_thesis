package rs.raf.paymentservice.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import rs.raf.dto.PaymentRequestDto;
import rs.raf.dto.PaymentResponseDto;
import rs.raf.paymentservice.service.PaymentService;

@RestController
@RequestMapping("payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("debit")
    public ResponseEntity<PaymentResponseDto> debit(@RequestBody PaymentRequestDto paymentRequestDto) {
        return ResponseEntity.ok(paymentService.debit(paymentRequestDto));
    }

    @PostMapping("credit")
    public ResponseEntity<Void> credit(@RequestBody PaymentRequestDto paymentRequestDto) {
        paymentService.credit(paymentRequestDto);
        return ResponseEntity.status(200).build();
    }

}
