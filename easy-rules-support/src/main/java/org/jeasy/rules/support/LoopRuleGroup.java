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

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rule;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * 
 * A loop rule group is a composite rule where the rule with the highest
 * priority acts as a loop condition: if the rule with the highest priority
 * evaluates to true, then we try to evaluate the rest of the rules and execute
 * the ones that evaluate to true until no candidate can be chosen
 *
 * @author rocket (rocket.yang@qq.com)
 *
 */
public class LoopRuleGroup extends CompositeRule {

	private Rule conditionalRule;
	/**
	 * create a loop rule group 
	 */
	public LoopRuleGroup() {
	}

	/**
	 * Create a loop rule group.
	 *
	 * @param name of the loop rule
	 */
	public LoopRuleGroup(String name) {
		super(name);
	}

	/**
	 * Create a loop rule group.
	 *
	 * @param name of the loop rule
	 * @param description of the loop rule
	 */
	public LoopRuleGroup(String name, String description) {
		super(name, description);
	}

	/**
	 * Create a loop rule group.
	 *
	 * @param name of the loop  rule
	 * @param description of the loop rule
	 * @param priority of the composite rule
	 */
	public LoopRuleGroup(String name, String description, int priority) {
		super(name, description, priority);
	}

	/**
	 * a path rule will trigger all candidate rule if the path rule's condition is true.
	 * 
	 * @param factsThe facts.date
	 * @return true if the path rules condition is true.
	 */
	@Override
	public boolean evaluate(Facts facts) {

		conditionalRule = this.getRuleWithHighestPriority();
		return conditionalRule.evaluate(facts);
	}

	/**
	 * When a conditional rule group is applied, all rules that evaluated to true
	 * are performed in their natural order, but with the conditional rule (the one
	 * with the highest priority) first.
	 *
	 * @param facts
	 *            The facts.
	 *
	 * @throws Exception
	 *             thrown if an exception occurs during actions performing
	 */
	@Override
	public void execute(Facts facts) throws Exception {
		
		List<Rule> candidates = null;
		//设置循环上限，防止程序bug导致死循环
		Integer maxLoop = facts.get("maxLoop") == null ? 1000 : (Integer) facts.get("maxLoop");
		do {
			candidates = this.selectCandidates(facts);
			for (Rule rule : candidates) {
				rule.execute(facts);
			}
			this.conditionalRule.execute(facts);
			maxLoop--;
		} while (!candidates.isEmpty() && maxLoop > 0);

	}
    /**
     * select rules that meet the condition
     * @param facts The facts
     * @return rules that evaluated to ture 
     */
	private List<Rule> selectCandidates(Facts facts) {

		List<Rule> candidates = new ArrayList<Rule>();
		if (!conditionalRule.evaluate(facts)) {
			return candidates;
		}
		for (Rule rule : this.rules) {
			if (rule != conditionalRule && rule.evaluate(facts)) {
				candidates.add(rule);
			}
		}

		return candidates;
	}

	private Rule getRuleWithHighestPriority() {
 
        List<Rule> copy = sortRules();
        // make sure that we only have one rule with the highest priority
        Rule highest = copy.get(0);
        if (copy.size() > 1 && copy.get(1).getPriority() == highest.getPriority()) {
           throw new IllegalArgumentException("Only one rule can have highest priority");
        }
        return highest;
	}

    private List<Rule> sortRules() {
        List<Rule> copy = new ArrayList<Rule>(rules);
        Collections.sort(copy, new Comparator<Rule>() {
            @Override
            public int compare(Rule o1, Rule o2) {
                Integer i2 = o2.getPriority();
                return i2.compareTo(o1.getPriority());
            }
        });
        return copy;
    }

}
