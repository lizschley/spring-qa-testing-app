package spring_qa_testing_app.cucumber.cucumberglue.testObjects;

import io.cucumber.spring.ScenarioScope;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ScenarioScope
public class ScenarioContext {
    private Map<String, Object> scenarioContext;
    public ScenarioContext() {
        scenarioContext = new HashMap<>();
    }
    public void setContext(String key, Object value) {
        scenarioContext.put(key, value);
    }
    public Object getContext(String key) {
        return scenarioContext.get(key);
    }
}
