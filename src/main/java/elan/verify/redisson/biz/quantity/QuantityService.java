package elan.verify.redisson.biz.quantity;

import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class QuantityService {
    private final RedissonClient redissonClient;
    private final QuantityRepo quantityRepo;
    private final EntityManager entityManager;

    public QuantityService(RedissonClient redissonClient, QuantityRepo quantityRepo, EntityManager entityManager) {
        this.redissonClient = redissonClient;
        this.quantityRepo = quantityRepo;
        this.entityManager = entityManager;
    }

    /**
     * 无锁减量
     *
     * @return 数量
     */
    @org.springframework.transaction.annotation.Transactional(rollbackFor = {Exception.class})
    public boolean subtractValueNonelock(Integer id) {
        Quantity quantity = quantityRepo.findById(id).get();
        Integer value = quantity.getValue();
        if (value > 0){
            quantity.setValue(value - 1);
            Integer stock = quantityRepo.saveAndFlush(quantity).getValue();
            log.info("剩余：" + stock);
            return true;
        }
        return false;
    }

    /**
     * redis锁减量
     *
     * @return 数量
     */
    public boolean subtractValueRedlock(Integer id) {
        RLock lock = redissonClient.getLock(String.valueOf(id));
        boolean result = false;
        boolean locked = false;
        try {
            locked = lock.tryLock(100, 30, TimeUnit.SECONDS);
            if (locked) {
                Quantity quantity = quantityRepo.findById(id).get();
                Integer value = quantity.getValue();
                if (value > 0){
                    quantity.setValue(value - 1);
                    Integer stock = quantityRepo.save(quantity).getValue();
                    log.info("剩余：" + stock);
                    result = true;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (locked) {
                lock.unlock();
            }
            return result;
        }
    }

    /**
     * 库锁减量
     *
     * @return 数量
     */
    @org.springframework.transaction.annotation.Transactional(rollbackFor = {Exception.class})
    public boolean subtractValueDblock(Integer id) {
        boolean result = false;
        Quantity quantity = entityManager.find(Quantity.class, id, LockModeType.PESSIMISTIC_WRITE);
        Integer value = quantity.getValue();
        if (value > 0){
            quantity.setValue(value - 1);
            Integer stock = quantityRepo.save(quantity).getValue();
            log.info("剩余：" + stock);
            result = true;
        }
        return result;
    }
}
