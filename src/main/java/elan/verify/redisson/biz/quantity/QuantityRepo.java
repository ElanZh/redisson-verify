package elan.verify.redisson.biz.quantity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
public interface QuantityRepo extends JpaRepository<Quantity, Integer>, JpaSpecificationExecutor<Quantity> {

    @Query("select q.value from Quantity q where q.id = ?1")
    Integer findValueById(int id);

    /**
     * update 操作自动加锁
     */
    @Modifying
    @Transactional
    @Query("update Quantity q set q.value = q.value - 1 where q.id = ?1 and q.value > 0")
    int subtractQuantityById(int id);
}
