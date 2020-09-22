package elan.verify.redisson;

import elan.verify.redisson.biz.quantity.Quantity;
import elan.verify.redisson.biz.quantity.QuantityRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class App implements ApplicationRunner {
    @Autowired
    private QuantityRepo quantityRepo;

    public static void main(String[] args) {
        SpringApplication.run(App.class, args);
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        quantityRepo.save(new Quantity(1, 200));
    }
}
