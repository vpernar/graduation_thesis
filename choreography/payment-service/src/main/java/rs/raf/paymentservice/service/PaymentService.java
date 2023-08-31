package rs.raf.paymentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import rs.raf.dto.PaymentDto;
import rs.raf.dto.PurchaseOrderDto;
import rs.raf.events.order.OrderEvent;
import rs.raf.events.payment.PaymentEvent;
import rs.raf.events.payment.PaymentStatus;
import rs.raf.paymentservice.entity.UserTransaction;
import rs.raf.paymentservice.repository.UserBalanceRepository;
import rs.raf.paymentservice.repository.UserTransactionRepository;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final UserBalanceRepository userBalanceRepository;
    private final UserTransactionRepository userTransactionRepository;

    @Transactional
    public PaymentEvent newOrderEvent(OrderEvent orderEvent) {
        PurchaseOrderDto purchaseOrderDto = orderEvent.getPurchaseOrder();
        PaymentDto paymentDto = PaymentDto.builder()
                .orderId(purchaseOrderDto.getOrderId())
                .userId(purchaseOrderDto.getUserId())
                .amount(purchaseOrderDto.getPrice())
                .build();

        return userBalanceRepository.findById(purchaseOrderDto.getUserId())
                .filter(userBalance -> userBalance.getBalance() >= purchaseOrderDto.getPrice())
                .map(userBalance -> {
                    userBalance.setBalance(userBalance.getBalance() - purchaseOrderDto.getPrice());
                    userBalanceRepository.save(userBalance);
                    userTransactionRepository.save(UserTransaction.of(
                            purchaseOrderDto.getOrderId(),
                            purchaseOrderDto.getUserId(),
                            purchaseOrderDto.getPrice())
                    );
                    return new PaymentEvent(paymentDto, PaymentStatus.RESERVED);
                })
                .orElse(new PaymentEvent(paymentDto, PaymentStatus.REJECTED));
    }

    @Transactional
    public void cancelOrderEvent(OrderEvent orderEvent) {
        this.userTransactionRepository.findById(orderEvent.getPurchaseOrder().getOrderId())
                .ifPresent(userTransaction -> {
                    userTransactionRepository.delete(userTransaction);
                    userBalanceRepository.findById(userTransaction.getUserId())
                            .ifPresent(userBalance -> {
                                userBalance.setBalance(userBalance.getBalance() + userTransaction.getAmount());
                                userBalanceRepository.save(userBalance);
                            });
                });
    }
}
