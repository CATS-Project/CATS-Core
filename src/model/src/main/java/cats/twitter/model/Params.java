package cats.twitter.model;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
/**
 *
 * @author Anthony Deseille
 */

@Entity
public class Params {
    static Map<String,String> convertion = new HashMap<>();
    static {
        convertion.put("bool","checkbox");
        convertion.put("number","number");
    }

    @Id
    @SequenceGenerator(name="params_seq",
            sequenceName="params_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="params_seq")
    int id;

    public Params(String displayName, String name, String type) {
        this.displayName = displayName;
        this.name = name;
        this.type = type;
    }

    public Params() {
    }

    String displayName;
    String name;
    String type;

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    @Transient
    public String getTypeHTML() {
        String htmlType = convertion.get(type);
        return htmlType != null ? htmlType : "text";
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public String toString() {
        return "Params{" +
                "id=" + id +
                ", displayName='" + displayName + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
