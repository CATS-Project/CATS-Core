//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.junit.Assert.assertNull;
//
//import java.io.IOException;
//import java.net.InetSocketAddress;
//import java.net.SocketAddress;
//import java.util.*;
//import java.util.logging.Level;
//import java.util.logging.Logger;
//
//import cats.twitter.collect.Collect;
//import cats.twitter.model.*;
//import cats.twitter.repository.UserRepository;
//import cats.twitter.webapp.controller.module.ApiController;
//import cats.twitter.webapp.controller.module.ModuleController;
//import cats.twitter.webapp.controller.module.ModuleControllerRest;
//import cats.twitter.webapp.dto.Result;
//import org.junit.*;
//import org.junit.runner.RunWith;
//import org.simpleframework.http.core.Container;
//import org.simpleframework.http.core.ContainerServer;
//import org.simpleframework.transport.Server;
//import org.simpleframework.transport.connect.Connection;
//import org.simpleframework.transport.connect.SocketConnection;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.context.annotation.Import;
//import org.springframework.test.context.ContextConfiguration;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import cats.twitter.repository.CorpusRepository;
//import cats.twitter.repository.ModuleRepository;
//import cats.twitter.webapp.dto.Query;
//import cats.twitter.webapp.service.ModuleService;
//import org.springframework.web.context.request.WebRequest;
//
//@RunWith(SpringJUnit4ClassRunner.class)
//@ContextConfiguration(locations = { "classpath:bean.xml" })
//public class ModuleTest implements ServerMock.OnStatus {
//    Server server;
//
//	@Autowired
//	ModuleRepository moduleRepository;
//	@Autowired
//	CorpusRepository corpusRepository;
//    @Autowired
//    UserRepository userRepository;
//
//    @Autowired
//    ModuleService moduleService;
//
//    @Autowired
//    ApiController controller;
//
//    private User user;
//    private Module module;
//    private Tweet tweet;
//
//
//    @Test
//	public void initTest()
//	{
//        String value1 = "value1";
//        String value2 = "value2";
//		moduleService.register(module);
//
//        Map<String,String> params = new HashMap<>();
//        params.put("toto",value1);
//        params.put("tata",value2);
//
//        moduleService.send(module,params,user.getCorpus().get(0));
//	}
//
//    @Before
//    public void construct() throws IOException {
//        int port = 2324;
//        ServerMock mock = new ServerMock(this);
//        server = new ContainerServer(mock);
//        Connection connection = new SocketConnection(server);
//        SocketAddress address = new InetSocketAddress(port);
//        connection.connect(address);
//
//        module = new Module();
//        module.setName("mock");
//        module.setEndpoint("http://localhost:"+port);
//        module.setParams(Arrays.asList(new Params("toto","toto","bool"),new Params("tata","tata","string")));
//        module.setReturns(Arrays.asList("HTML"));
//    }
//
//    @Before
//    public void constructData(){
//        /*user = new User();
//        user.setLogin("login");
//        user.setPassword("password");
//
//
//        Tweet tweet = new Tweet();
//        tweet.setName("NAME");
//        tweet.setAuthor(99999L);
//        tweet.setText("TEXT");
//
//        Corpus corpus = new Corpus();
//        corpus.setTweets(Collections.singletonList(tweet));
//        tweet.setCorpus(corpus);
//
//        user.setCorpus(Collections.singletonList(corpus));
//        corpus.setUser(user);
//        user = userRepository.save(user);
//
//        this.tweet=user.getCorpus().get(0).getTweets().get(0);*/
//    }
//
//
//    @After
//    public void destroyData(){
//        userRepository.delete(user);
//        moduleRepository.delete(module);
//    }
//
//    @After
//    public void destruct() throws IOException {
//        server.stop();
//    }
//
//    @Override
//    public void onInit(Query query) {
//        assertEquals(query.getParams().size(),2);
//        assertNotNull(query.getParams().get("toto"));
//        assertNotNull(query.getParams().get("tata"));
//
//        List<Tweet> tweets = controller.api(query.getToken(),null,null);
//        assertEquals(tweets.size(),1);
//        Tweet localTweet = tweets.get(0);
//        assertEquals(tweet.getId(),localTweet.getId());
//        assertEquals(tweet.getAuthor(),localTweet.getAuthor());
//        assertEquals(tweet.getText(),localTweet.getText());
//
//        Request request = moduleService.postResult(new Result(query.getToken(), "ok"));
//
//        assertEquals(request.getResult(cats.twitter.model.Result.TypeRes.HTML),"ok");
//        assertNull(request.getToken());
//    }
//
//}
