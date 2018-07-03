/**
 * 
 */
package org.jeasy.rules.core;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @author rocket (rocket.yang@qq.com)
 * 
 *  A custom rule hook for rules executing life circle, we do not use DefaultRuleListener, 
 *  because of our specific business scene.
 */
public class RuleExceptionListener implements RuleListener {

	private static final Logger LOGGER = LoggerFactory.getLogger(RuleExceptionListener.class);

    @Override
    public boolean beforeEvaluate(final Rule rule, final Facts facts) {
        return true;
    }

    @Override
    public void afterEvaluate(final Rule rule, final Facts facts, final boolean evaluationResult) {
    	// TODO 

    }

    @Override
    public void beforeExecute(final Rule rule, final Facts facts) {
    	// TODO 
    }

    @Override
    public void onSuccess(final Rule rule, final Facts facts) {
    	// TODO 
    }

    @Override
    public void onFailure(final Rule rule, final Facts facts, final Exception exception) {
        facts.put("exception", exception);
    }

}
