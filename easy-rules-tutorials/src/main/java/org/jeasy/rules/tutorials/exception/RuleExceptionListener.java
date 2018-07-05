package org.jeasy.rules.tutorials.exception;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.UndeclaredThrowableException;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.RuleListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by yonching on 7/4/18.
 */
public class RuleExceptionListener implements RuleListener {

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleExceptionListener.class);

    @Override
    public boolean beforeEvaluate(final Rule rule, final Facts facts) {
        return true;
    }

    @Override
    public void afterEvaluate(final Rule rule, final Facts facts, final boolean evaluationResult) {
        final String ruleName = rule.getName();
        if (evaluationResult) {
            LOGGER.info("Rule '{}' triggered", ruleName);
        } else {
            LOGGER.info("Rule '{}' has been evaluated to false, it has not been executed", ruleName);
        }
    }

    @Override
    public void beforeExecute(final Rule rule, final Facts facts) {

    }

    @Override
    public void onSuccess(final Rule rule, final Facts facts) {
        LOGGER.info("Rule '{}' performed successfully", rule.getName());
    }

    @Override
    public void onFailure(final Rule rule, final Facts facts, final Exception exception) {
    	Throwable ruleException = exception;
    	if (exception instanceof UndeclaredThrowableException && exception.getCause() instanceof InvocationTargetException) {
    		ruleException = exception.getCause().getCause();
    	} 	
    	if (ruleException instanceof RuleException) {
    		facts.put("exception", (RuleException)ruleException);
    	}
    }
}
