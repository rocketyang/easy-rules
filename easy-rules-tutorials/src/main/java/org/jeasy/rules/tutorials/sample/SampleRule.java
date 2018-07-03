package org.jeasy.rules.tutorials.sample;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

/**
 * Created by yonching on 6/20/18.
 */
@Rule
public class SampleRule {
    @Condition
    public boolean condition(@Fact("sample") Sample sample) {
        //
        return sample.getExist() && sample.getAvailable() && sample.getInventory() && !sample.getRepetition();
    }

    @Action
    public void action() throws Exception {
        throw new NullPointerException();
        /**try {
            subInventory();
            saveDiscount();
            saveSample();
        } catch(Exception e) {
            throw e;
        } */
    }

    /**
     * 减库存
     */
    private void subInventory() {
        System.out.println("减库存");
    }

    /**
     * 记录返样优惠表信息
     */
    private void saveDiscount() {
        System.out.println("记录返样优惠表信息");
    }

    /**
     * 插入样品订单表信息
     */
    private void saveSample() {
        System.out.println("插入样品订单表信息");
    }
}
