package rs.raf.orderservice;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import rs.raf.orderservice.entity.ProductPrice;
import rs.raf.orderservice.repository.ProductPriceRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
public class DataRunner implements CommandLineRunner {
    private final ProductPriceRepository productPriceRepository;

    @Override
    public void run(String... args) throws Exception {
        ProductPrice productPrice1 = new ProductPrice(1, 100d);
        ProductPrice productPrice2 = new ProductPrice(2, 100d);
        ProductPrice productPrice3 = new ProductPrice(3, 100d);
        productPriceRepository.saveAll(List.of(productPrice1, productPrice2, productPrice3));
    }
}
