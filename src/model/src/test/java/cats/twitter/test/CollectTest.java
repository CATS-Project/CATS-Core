package cats.twitter.test;

import cats.twitter.collect.Collect;
import cats.twitter.model.User;

import java.util.Date;
import java.util.Optional;


public class CollectTest {
  
    public static void main (String[] args) throws InterruptedException {
        User u = new User();
        u.setConsumerKey("f1v6o1OqKirD7mW9SyXkdkxuA");
        u.setConsumerSecret("NlKdSd5M5YTV9Nboi4DxqYcFETVVoPG6w8kikPpCGUGXu9gRed");
        u.setToken("143592749-zTa2WnOPSy9VkezFHG4TGiji0lwK6qV6wYWeSwXH");
        u.setTokenSecret("ZD50oGcS5qFguEz71kqWD77xNtlQr50p1zrKqIgIEHKkv");

        /*u.setConsumerKey("xtGwc4JrLzYNjIXGc9vdkOo0R");
        u.setConsumerSecret("ZNHTAykWHV4bbpJsfDccUNhkaDZ4ElLbyk62Iu4tXbYw44Nuek");
        u.setToken("143592749-1D6jLyq68xpRoLmd6di1zd2YsHVmlBeYthxXyrny");
        u.setTokenSecret("sNzp1Jqo2mhlFTl6ennnamsQzYP1tOp0FBTzRoulaQipe");*/

        /*double lat = 53.186288;
        double longitude = -8.043709;
        double lat1 = lat - 4;
        double longitude1 = longitude - 8;
        double lat2 = lat + 4;
        double longitude2 = longitude + 8;
        double[][] tmp = {{longitude1, lat}, {longitude2, lat2}};*/
        double longitude1 = 41.38156626488825;
        double lat1 = -7.548828125;
        double longitude2 = 52.1767885655741;
        double lat2 = 10.451171875;
        double[][] tmp = {{lat1, longitude1}, {lat2, longitude2}};
        Optional<double[][]> bb = Optional.ofNullable(tmp);
        Date d = new Date(115, 9, 21, 11, 48);
        String[] tmpString = null;
        Optional<String[]> keywords =  Optional.ofNullable(tmpString);
        Long[] tmpLong = null;
        Optional<Long[]> follow = Optional.ofNullable(tmpLong);
        Collect test = new Collect(u, "unknown", 10, keywords, null, follow, bb, null, null, null);
        test.run();

   }
}
