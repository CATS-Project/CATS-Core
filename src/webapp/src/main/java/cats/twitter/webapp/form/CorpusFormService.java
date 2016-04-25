package cats.twitter.webapp.form;

import java.util.Map;
import java.util.Set;

public class CorpusFormService {

    private Map<String,String> formList;

    public void setFormList(Map<String,String> formList) {
        this.formList = formList;
    }

    public Set<Map.Entry<String,String>> getFormList() {
        return formList.entrySet();
    }
}
