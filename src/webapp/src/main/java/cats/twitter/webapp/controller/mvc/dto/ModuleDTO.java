package cats.twitter.webapp.controller.mvc.dto;

import cats.twitter.model.Module;
import cats.twitter.model.Params;

import java.util.ArrayList;
import java.util.List;

public class ModuleDTO {

    private String name;
    private String endpoint;
    private String description;
    private List<String> nameParameter;
    private List<String> displayNameParameter;
    private List<String> typeParameter;
    private List<String> returns;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getNameParameter() {
        return nameParameter;
    }

    public void setNameParameter(List<String> nameParameter) {
        this.nameParameter = nameParameter;
    }

    public List<String> getDisplayNameParameter() {
        return displayNameParameter;
    }

    public void setDisplayNameParameter(List<String> displayNameParameter) {
        this.displayNameParameter = displayNameParameter;
    }

    public List<String> getTypeParameter() {
        return typeParameter;
    }

    public void setTypeParameter(List<String> typeParameter) {
        this.typeParameter = typeParameter;
    }

    public List<String> getReturns() {
        return returns;
    }

    public void setReturns(List<String> returns) {
        this.returns = returns;
    }

    public Module toModule() {
        Module module = new Module();
        module.setEndpoint(endpoint);
        module.setName(name);
        module.setDescription(description);
        module.setReturns(returns);
        List<Params> paramses = new ArrayList<>();
        if(displayNameParameter != null){
            for (int i = 0; i < displayNameParameter.size(); i++) {
                paramses.add(new Params(displayNameParameter.get(i),nameParameter.get(i),typeParameter.get(i)));
            }
        }
        module.setParams(paramses);

        return module;
    }
}
