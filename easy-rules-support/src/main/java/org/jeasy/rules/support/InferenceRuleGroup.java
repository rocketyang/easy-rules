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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 *
 * 推理规则
 * @author rocket (rocket.yang@qq.com)
 *
 */
public class InferenceRuleGroup extends CompositeRule {

	private static final Logger LOGGER = LoggerFactory.getLogger(InferenceRuleGroup.class);

	private Rule conditionalRule;

	/**
	 * create a loop rule group
	 */
	public InferenceRuleGroup() {
	}

	/**
	 * Create a loop rule group.
	 *
	 * @param name
	 *            of the loop rule
	 */
	public InferenceRuleGroup(String name) {
		super(name);
	}

	/**
	 * Create a loop rule group.
	 *
	 * @param name
	 *            of the loop rule
	 * @param description
	 *            of the loop rule
	 */
	public InferenceRuleGroup(String name, String description) {
		super(name, description);
	}

	/**
	 * Create a loop rule group.
	 *
	 * @param name
	 *            of the loop rule
	 * @param description
	 *            of the loop rule
	 * @param priority
	 *            of the composite rule
	 */
	public InferenceRuleGroup(String name, String description, int priority) {
		super(name, description, priority);
	}

	/**
	 * a path rule will trigger all candidate rule if the path rule's condition is
	 * true.
	 * 
	 * @param factsThe
	 *            facts.date
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
		Integer maxLoop = facts.get("maxLoop");
		if (maxLoop == null) {
			LOGGER.info("maxLoop is not initialized");
			return;
		}
		if (!conditionalRule.evaluate(facts)) {
			LOGGER.info("condition rule evaluate to false and escape the loop body");
			return;
		}
		do {
			conditionalRule.execute(facts);
			candidates = this.selectCandidates(facts);
			for (Rule rule : candidates) {
				rule.execute(facts);
			}
			maxLoop--;
			facts.put("maxLoop", maxLoop);
		} while (!candidates.isEmpty() && maxLoop > 0);
		
		facts.remove("maxLoop");
	}

	/**
	 * select rules that meet the condition
	 * 
	 * @param facts
	 *            The facts
	 * @return rules that evaluated to true
	 */
	private List<Rule> selectCandidates(Facts facts) throws Exception {

		List<Rule> candidates = new ArrayList<Rule>();
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
