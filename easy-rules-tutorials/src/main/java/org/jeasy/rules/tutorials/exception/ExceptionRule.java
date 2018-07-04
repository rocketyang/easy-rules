package org.jeasy.rules.tutorials.exception;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Rule;

/**
 * Created by yonching on 7/4/18.
 */
@Rule
public class ExceptionRule {
    @Condition
    public boolean condition() throws Exception  {
        throw new RuleException("rule exception");
    }


    @Action
    public void action() throws Exception {
        throw new NullPointerException();

    }

}
