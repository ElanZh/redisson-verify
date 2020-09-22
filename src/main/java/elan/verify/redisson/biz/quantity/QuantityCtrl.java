package elan.verify.redisson.biz.quantity;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("quantity")
public class QuantityCtrl {
    private final QuantityRepo quantityRepo;
    private final QuantityService quantityService;

    public QuantityCtrl(QuantityRepo quantityRepo, QuantityService quantityService) {
        this.quantityRepo = quantityRepo;
        this.quantityService = quantityService;
    }

    @GetMapping("value/{id}")
    public Integer value(@PathVariable Integer id) {
        if (id == null) {
            return null;
        }
        return quantityRepo.findValueById(id);
    }


    @GetMapping("subtractValue/nonelock/{id}")
    public Boolean subtractValueNonelock(@PathVariable Integer id) {
        if (id == null) {
            return null;
        }
        return quantityService.subtractValueNonelock(id);
    }

    @GetMapping("subtractValue/redlock/{id}")
    public Boolean subtractValueRedlock(@PathVariable Integer id) {
        if (id == null) {
            return null;
        }
        return quantityService.subtractValueRedlock(id);
    }

    @GetMapping("subtractValue/dblock/{id}")
    public Boolean subtractValueDblock(@PathVariable Integer id) {
        if (id == null) {
            return null;
        }
        return quantityService.subtractValueDblock(id);
    }

}
