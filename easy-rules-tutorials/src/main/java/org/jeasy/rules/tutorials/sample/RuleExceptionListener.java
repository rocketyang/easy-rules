package org.jeasy.rules.tutorials.sample;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;

/**
 * Created by yonching on 7/3/18.
 */
public class RuleExceptionListener implements RuleListener{
    @Override
    public boolean beforeEvaluate(Rule rule, Facts facts) {
        return true;
    }

    @Override
    public void afterEvaluate(Rule rule, Facts facts, boolean evaluationResult) {

    }

    @Override
    public void beforeExecute(Rule rule, Facts facts) {

    }

    @Override
    public void onSuccess(Rule rule, Facts facts) {

    }

    @Override
    public void onFailure(Rule rule, Facts facts, Exception exception) {
        facts.put("exception", exception);
    }
}
