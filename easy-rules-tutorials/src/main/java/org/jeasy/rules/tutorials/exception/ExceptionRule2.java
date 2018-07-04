package org.jeasy.rules.tutorials.exception;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.core.BasicRule;

/**
 * Created by yonching on 7/4/18.
 */
public class ExceptionRule2 extends BasicRule {
    public boolean evaluate(Facts facts)  {
        throw new RuleException("rule exception");
    }


    public void execute(Facts facts) throws Exception {
        throw new NullPointerException();
    }
}
