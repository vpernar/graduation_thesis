package rs.raf.paymentservice;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.raf.paymentservice.model.UserBalance;
import rs.raf.paymentservice.repository.UserBalanceRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataRunner implements CommandLineRunner {
    private final UserBalanceRepository userBalanceRepository;

    @Override
    public void run(String... args) throws Exception {
        UserBalance userBalance1 = new UserBalance(1, 1000d);
        UserBalance userBalance2 = new UserBalance(2, 1000d);
        UserBalance userBalance3 = new UserBalance(3, 1000d);
        userBalanceRepository.saveAll(List.of(userBalance1, userBalance2, userBalance3));
    }
}
