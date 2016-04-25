import cats.twitter.webapp.dto.Query;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.simpleframework.http.Request;
import org.simpleframework.http.Response;
import org.simpleframework.http.core.Container;

import java.io.IOException;
import java.io.PrintStream;

public class ServerMock implements Container {

    private final OnStatus onStatus;
    private final ObjectMapper objectMapper;

    public interface OnStatus{
        void onInit(Query query);
    }

    public ServerMock(OnStatus queryTest) {
        onStatus = queryTest;

        objectMapper = new ObjectMapper();
    }

    @Override
    public void handle(Request request, Response response) {

        response.setValue("Content-Type", "application/json");

        String path = request.getAddress().getPath().getPath();
        Assert.assertEquals(path,"/init");
        if (path.equals("/init")) {
            try {
                onStatus.onInit(objectMapper.readValue(request.getContent(),Query.class));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        PrintStream body = null;
        try {
            body = response.getPrintStream();
            body.println(request.getContent());
            body.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
