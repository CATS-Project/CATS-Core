package cats.twitter.model;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Nathanael on 02/02/2016.
 */
@Entity
public class Result {

    static private final SimpleDateFormat format = new SimpleDateFormat("EEE, d MMMM yyyy HH:mm", Locale.ENGLISH);

    public enum TypeRes{
        FILE,HTML,NONE,ERROR;
        public boolean isFile(){return equals(FILE);}
        public boolean isError(){return equals(ERROR);}
        public boolean isHTML(){return equals(HTML);}
        public boolean isCorpus(){return equals(NONE);}
    }

    @Id
    @SequenceGenerator(name="result_seq",
            sequenceName="result_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="result_seq")
    private Long id;
    private Date date;

    @Enumerated(EnumType.STRING)
    private TypeRes type;

    @Column(length = 10485760)
    private String result;

    public long getId() {
        return id;
    }

    public void setType(TypeRes type) {
        this.type = type;
    }

    public TypeRes getType() {
        return type;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getDate() {
        return format.format(date);
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
