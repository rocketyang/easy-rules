/**
 * The MIT License
 *
 *  Copyright (c) 2018, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
 *
 *  Permission is hereby granted, free of charge, to any person obtaining a copy
 *  of this software and associated documentation files (the "Software"), to deal
 *  in the Software without restriction, including without limitation the rights
 *  to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 *  copies of the Software, and to permit persons to whom the Software is
 *  furnished to do so, subject to the following conditions:
 *
 *  The above copyright notice and this permission notice shall be included in
 *  all copies or substantial portions of the Software.
 *
 *  THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 *  IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 *  FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 *  AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 *  LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 *  OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 *  THE SOFTWARE.
 */
package org.jeasy.rules.support;

import org.jeasy.rules.annotation.Action;
import org.jeasy.rules.annotation.Condition;
import org.jeasy.rules.annotation.Priority;
import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;
import org.jeasy.rules.api.Rules;

import org.jeasy.rules.core.SpringRulesEngine;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class LoopRuleGroupTest {

    @Mock
    private Rule rule1, rule2, conditionalRule;

    private Facts facts = new Facts();
    private Rules rules = new Rules();

    private SpringRulesEngine rulesEngine = new SpringRulesEngine();

    private LoopRuleGroup loopRuleGroup;

    @Before
    public void setUp() {
        when(rule1.evaluate(facts)).thenReturn(false);
        when(rule1.getPriority()).thenReturn(2);
        when(rule2.evaluate(facts)).thenReturn(true);
        when(rule2.getPriority()).thenReturn(3);
        when(rule2.compareTo(rule1)).thenReturn(1);
        when(conditionalRule.compareTo(rule1)).thenReturn(1);
        when(conditionalRule.compareTo(rule2)).thenReturn(1);
        when(conditionalRule.getPriority()).thenReturn(100);
        loopRuleGroup = new LoopRuleGroup();
    }


    @Test
    public void rulesMustNotBeExecutedIfConditionalRuleEvaluatesToFalse() throws Exception {
        // Given
        when(conditionalRule.evaluate(facts)).thenReturn(false);
        loopRuleGroup.addRule(rule1);
        loopRuleGroup.addRule(rule2);
        loopRuleGroup.addRule(conditionalRule);
        rules.register(loopRuleGroup);

        // When
        rulesEngine.fire(rules, facts);

        // Then
        /*
         * The composing rules should not be executed
         * since the conditional rule evaluate to FALSE
         */
        verify(conditionalRule, times(1)).evaluate(facts);
        // primaryRule should not be executed
        verify(conditionalRule, never()).execute(facts);
        //Rule 1 should not be executed
        verify(rule1, never()).execute(facts);
        //Rule 2 should not be executed
        verify(rule2, never()).execute(facts);
    }

    @Test
    public void rulesMustBeExecutedForThoseThatEvaluateToTrueIfConditionalRuleEvaluatesToTrue() throws Exception {
        // Given
        when(conditionalRule.evaluate(facts)).thenReturn(true);
        loopRuleGroup.addRule(rule1);
        loopRuleGroup.addRule(rule2);
        loopRuleGroup.addRule(conditionalRule);
        rules.register(loopRuleGroup);

        // When
        rulesEngine.fire(rules, facts);

        // Then
        /*
         * Some of he composing rules should be executed
         * since the conditional rule evaluate to TRUE
         */

        // primaryRule should be executed
        verify(conditionalRule, times(1)).execute(facts);
        //Rule 1 should not be executed
        verify(rule1, never()).execute(facts);
        //Rule 2 should be executed
        verify(rule2, times(1000)).execute(facts);
    }

    @Test
    public void whenARuleIsRemoved_thenItShouldNotBeEvaluated() throws Exception {
        // Given
        when(conditionalRule.evaluate(facts)).thenReturn(true);
        loopRuleGroup.addRule(rule1);
        loopRuleGroup.addRule(rule2);
        loopRuleGroup.addRule(conditionalRule);
        loopRuleGroup.removeRule(rule2);
        rules.register(loopRuleGroup);

        // When
        rulesEngine.fire(rules, facts);

        // Then
        // primaryRule should be executed
        verify(conditionalRule, times(1)).execute(facts);
        //Rule 1 should not be executed
        verify(rule1, times(1)).evaluate(facts);
        verify(rule1, never()).execute(facts);
        // Rule 2 should not be evaluated nor executed
        verify(rule2, never()).evaluate(facts);
        verify(rule2, never()).execute(facts);

    }

    @Test
    public void testCompositeRuleWithAnnotatedComposingRules() throws Exception {
        // Given
        when(conditionalRule.evaluate(facts)).thenReturn(true);
        MyRule rule = new MyRule(1);
        loopRuleGroup = new LoopRuleGroup("myLoopRule");
        loopRuleGroup.addRule(rule);
        when(conditionalRule.compareTo(any(Rule.class))).thenReturn(1);
        loopRuleGroup.addRule(conditionalRule);
        rules.register(loopRuleGroup);

        // When
        rulesEngine.fire(rules, facts);

        // Then
        verify(conditionalRule, times(1)).execute(facts);
        assertThat(rule.isExecuted()).isTrue();
    }

 
    
    @Test
    public void testLoopCompositeRules() throws Exception {
    	 MyRule myRule = new MyRule(1);
    	 MyOtherRule myOtherRule = new MyOtherRule(2);
    	 MyConditionRule myConditionRule = new MyConditionRule(100);
    	 AccumulateRule accumulateRule = new AccumulateRule(4);
    	 facts.put("loops", 10);
         facts.put("maxLoop", 10);
    	 loopRuleGroup = new LoopRuleGroup("myLoopRule", "loop rule description");
    	 loopRuleGroup.addRule(myRule);
    	 loopRuleGroup.addRule(myOtherRule);
    	 loopRuleGroup.addRule(myConditionRule);
    	 loopRuleGroup.addRule(accumulateRule);
    	 rules.register(loopRuleGroup);
    	 rulesEngine.fire(rules, facts);
    	 assertThat(myRule.isExecuted()).isTrue();
    	 assertThat(myOtherRule.isExecuted()).isFalse();
    	 assertThat(facts.get("sum")).isEqualTo(10);
    }

    @org.jeasy.rules.annotation.Rule
    public class MyRule {
    	
    	private int priority;
        boolean executed;
        public MyRule(int priority) {
            this.priority = priority;
        }
        @Condition
        public boolean when() {
            return true;
        }

        @Action
        public void then() {
            executed = true;
        }

        @Priority
        public int priority() {
            return this.priority;
        }

        public boolean isExecuted() {
            return executed;
        }

    }


    @org.jeasy.rules.annotation.Rule
    public class MyOtherRule {
        boolean executed;
        private int priority;

        public MyOtherRule(int priority) {
            this.priority = priority;
        }

        @Condition
        public boolean when() {
            return false;
        }

        @Action
        public void then() {
            executed = true;
        }

        @Priority
        public int priority() {
            return priority;
        }

        public boolean isExecuted() {
            return executed;
        }

    }

    
    @org.jeasy.rules.annotation.Rule
    public class MyConditionRule {
    	
        private int priority;

        public MyConditionRule(int priority) {
            this.priority = priority;
        }

        @Condition
        public boolean when(Facts facts) {
        	Integer loops = facts.get("loops");
        	return loops != null && loops > 0;
        }

        @Action
        public void then(Facts facts) {
        	Integer loops = facts.get("loops");
        	facts.put("loops", loops -1);
        }

        @Priority
        public int priority() {
            return priority;
        }
    }
    
    @org.jeasy.rules.annotation.Rule
    public class AccumulateRule {
    	
        private int priority;

        public AccumulateRule(int priority) {
            this.priority = priority;
        }

        @Condition
        public boolean when(Facts facts) {
        	return true;
        }

        @Action
        public void then(Facts facts) {
        	Integer sum = facts.get("sum");
        	sum = sum == null ? 1 : sum +1;
        	facts.put("sum",sum);
        }

        @Priority
        public int priority() {
            return priority;
        }
    }
}
