package cats.twitter.webapp.service;

import cats.twitter.model.Corpus;
import cats.twitter.model.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;

/**
 * Created by Nathanael on 23/10/2015.
 */
public interface CSVService {
    Corpus importCSV(InputStream stream, User user, String name) throws IOException;
    void exportCSV(User user, long id, Writer writer) throws IOException;


}
