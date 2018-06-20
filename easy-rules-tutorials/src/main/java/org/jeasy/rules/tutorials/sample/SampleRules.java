package org.jeasy.rules.tutorials.sample;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RulesEngineParameters;

/**
 * Created by yonching on 6/20/18.
 */
public class SampleRules {
    public static void main(String[] args) {
        // create rules engine
        RulesEngineParameters parameters = new RulesEngineParameters().skipOnFirstAppliedRule(true);
        RulesEngine sampleEngine = new DefaultRulesEngine(parameters);

        // create rules
        Rules rules = new Rules();
        rules.register(new SampleRule());
        rules.register(new SampleRule2());

        Sample sample = new Sample();
        sample.setSampleId("1");
        // fire rules
        Facts facts = new Facts();
        facts.put("sample", sample);
        sampleEngine.fire(rules, facts);
    }
}

