package org.jeasy.rules.tutorials.sample;

import org.jeasy.rules.api.Facts;
import org.jeasy.rules.api.Rules;
import org.jeasy.rules.api.RulesEngine;
import org.jeasy.rules.core.DefaultRulesEngine;
import org.jeasy.rules.core.RulesEngineParameters;
import org.jeasy.rules.tutorials.fizzbuzz.BuzzRule;
import org.jeasy.rules.tutorials.fizzbuzz.NonFizzBuzzRule;
import org.jeasy.rules.tutorials.helloworld.HelloWorldRule;

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
        rules.register(new HelloWorldRule());
        rules.register(new BuzzRule());
        rules.register(new NonFizzBuzzRule());

        Sample sample = new Sample();
        //sample.setSampleId("1");
        // fire rules
        Facts facts = new Facts();
        facts.put("sample", sample);
        try {
            sampleEngine.fire(rules, facts);
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}

