package rs.raf.paymentservice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.raf.paymentservice.entity.UserBalance;
import rs.raf.paymentservice.repository.UserBalanceRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataRunner implements CommandLineRunner {
    private final UserBalanceRepository userBalanceRepository;

    @Override
    public void run(String... args) throws Exception {
        UserBalance userBalance1 = new UserBalance(1, 1000);
        UserBalance userBalance2 = new UserBalance(1, 1000);
        UserBalance userBalance3 = new UserBalance(1, 1000);
        userBalanceRepository.saveAll(List.of(userBalance1, userBalance2, userBalance3));
    }
}
