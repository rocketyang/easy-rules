package org.jeasy.rules.tutorials.sample;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Fact;
import org.jeasy.rules.annotation.Rule;

/**
 * Created by yonching on 6/20/18.
 */
@Rule
public class SampleRule2 {
    @Condition
    public boolean validate(@Fact("sample") Sample sample) {
        //
        return !sample.getSampleId().isEmpty();
    }

    @Action
    public void doSomething() throws Exception {
        try {
            countDicount();
        } catch(Exception e) {
            throw e;
        }
    }

    /**
     * 计算优惠
     */
    private void countDicount() throws Exception {
        System.out.println("计算优惠");
        throw new Exception();
    }


}