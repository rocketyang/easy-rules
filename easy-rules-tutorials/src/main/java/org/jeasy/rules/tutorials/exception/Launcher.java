/**
 * The MIT License
 *
 *  Copyright (c) 2017, Mahmoud Ben Hassine (mahmoud.benhassine@icloud.com)
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
package org.jeasy.rules.tutorials.exception;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.RulesEngineParameters;
import org.jeasy.rules.core.SpringRulesEngine;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class Launcher {
	
    private static final Logger LOGGER = LoggerFactory.getLogger(Launcher.class);

    public static void main(String[] args) {

        RulesEngineParameters parameters = new RulesEngineParameters().skipOnFirstFailedRule(true);

        // create a rules engine and fire rules on known facts
        RulesEngine rulesEngine = new SpringRulesEngine(parameters);
        rulesEngine.getRuleListeners().add(new RuleExceptionListener());

        // create facts
        Facts facts = new Facts();

        // create rules
        Rules rules = new Rules();
        rules.register(new ExceptionRule());


        rulesEngine.fire(rules, facts);

        //Exception e = (Exception)facts.get("exception");
        LOGGER.info(facts.toString());
     
    }
}