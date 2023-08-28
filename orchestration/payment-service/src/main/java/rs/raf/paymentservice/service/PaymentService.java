package rs.raf.paymentservice.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import rs.raf.dto.PaymentRequestDto;
import rs.raf.dto.PaymentResponseDto;
import rs.raf.enums.PaymentStatus;
import rs.raf.paymentservice.exception.NotFoundException;
import rs.raf.paymentservice.model.UserBalance;
import rs.raf.paymentservice.repository.UserBalanceRepository;


@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentService {

    private final UserBalanceRepository userBalanceRepository;

    @Transactional
    public PaymentResponseDto debit(final PaymentRequestDto paymentRequestDto){
        UserBalance userBalance = userBalanceRepository.findById(paymentRequestDto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        double balance = userBalance.getBalance();

        PaymentStatus paymentStatus = PaymentStatus.PAYMENT_APPROVED;
        if(balance < paymentRequestDto.getAmount()) {
            paymentStatus = PaymentStatus.PAYMENT_REJECTED;
        } else {
            userBalance.setBalance(balance - paymentRequestDto.getAmount());
            userBalanceRepository.save(userBalance);
        }

        log.info("Resolved debit action for {}, with status {}", paymentRequestDto, paymentStatus);

        return PaymentResponseDto.builder()
                .amount(paymentRequestDto.getAmount())
                .userId(paymentRequestDto.getUserId())
                .orderId(paymentRequestDto.getOrderId())
                .status(paymentStatus)
                .build();
    }

    @Transactional
    public void credit(final PaymentRequestDto paymentRequestDto){
        UserBalance userBalance = userBalanceRepository.findById(paymentRequestDto.getUserId())
                .orElseThrow(() -> new NotFoundException("User not found"));

        userBalance.setBalance(userBalance.getBalance() + paymentRequestDto.getAmount());
        userBalanceRepository.save(userBalance);

        log.info("Resolved credit action for {}", paymentRequestDto);
    }

}
