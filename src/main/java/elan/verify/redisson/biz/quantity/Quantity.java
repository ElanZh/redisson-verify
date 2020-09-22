package elan.verify.redisson.biz.quantity;


import javax.persistence.*;

@lombok.Data
@lombok.NoArgsConstructor
@lombok.AllArgsConstructor
@Entity
public class Quantity {
    @Id
    private Integer id;

    @Column(nullable = false)
    private Integer value;

    public Quantity(Integer id) {
        this.id = id;
    }
}


