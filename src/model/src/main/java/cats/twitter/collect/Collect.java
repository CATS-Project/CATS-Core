/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cats.twitter.collect;

import cats.twitter.model.*;
import cats.twitter.repository.CorpusRepository;
import cats.twitter.repository.TweetRepository;
import twitter4j.FilterQuery;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.conf.ConfigurationBuilder;

import java.util.*;

/**
 *
 * @author Anthony Deseille
 */
public class Collect extends Observable implements ICollect, Runnable {
    private ConfigurationBuilder cb;
    private FilterQuery filter;
    private Corpus corpus;
    private User user;
    private TwitterStream twitterStream;
    private Calendar dateEnd;
    private int duree;



    private List<Tweet> tweets;

    private CorpusRepository corpusRepository;
    private TweetRepository tweetRepository;

    public Collect(User user, String name, int duree, Optional<String[]> keywords, Optional<String[]> followsString, Optional<Long[]> follows, Optional<double[][]> location, Optional<String> lang, CorpusRepository repository, TweetRepository tweetRepository){
        this.tweetRepository = tweetRepository;
        this.corpusRepository = repository;
        tweets  = new ArrayList<>();
        corpus = new Corpus();
        corpus.setName(name);
        corpus.setDuree(duree);
        corpus.setKeyWords(keywords.isPresent() ? keywords.get() : null);
        corpus.setFollows(followsString.isPresent() ? followsString.get() : null);
        corpus.setLocation(location.isPresent() ? location.get() : null);
        corpus.setUser(user);
        corpus.setLang(lang.isPresent() ? lang.get() : null);
        corpus = corpusRepository.save(corpus);

        this.user = user;
        this.duree = duree;

        filter = new FilterQuery();
        if(keywords.isPresent())
            filter.track(keywords.get());
        if(location.isPresent())
            filter.locations(location.get());
        if(follows.isPresent())
        {
            long[] tmp = new long[follows.get().length];
            for(int i=0; i<follows.get().length; i++)
            tmp[i] = follows.get()[i];
            filter.follow(tmp);
        }
    }
    
    @Override
    public boolean runCollect() {

        dateEnd = Calendar.getInstance();
        dateEnd.add(Calendar.DAY_OF_YEAR,duree);
        cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true);
        cb.setOAuthConsumerKey(user.getConsumerKey());
        System.out.println("CONSUMER KEY "+user.getConsumerKey());
        cb.setOAuthConsumerSecret(user.getConsumerSecret());
        System.out.printf("CONSUMER SECRET "+user.getConsumerSecret());
        cb.setOAuthAccessToken(user.getToken());
        System.out.printf("TOKEN"+user.getToken());
        cb.setOAuthAccessTokenSecret(user.getTokenSecret());
        System.out.printf("TOKEN SECRET " + user.getTokenSecret());

        twitterStream = new TwitterStreamFactory(cb.build()).getInstance();

        setStatus(State.WAITING_FOR_CONNECTION);

        StatusListener listener = new StatusListener() {

            @Override
            public void onStatus(Status status) {

                if (!corpus.getState().equals(State.INPROGRESS)){
                    setStatus(State.INPROGRESS);
                }
                if (status.getCreatedAt().after(dateEnd.getTime())){
                    shutdown();

                }
                else if(corpus.getLang() == null || corpus.getLang().equals(status.getLang()))
                {
                    Tweet t = new Tweet();
                    t.setText(status.getText().replace("\r", "\n"));
                    t.setAuthor(status.getUser().getId());
                    t.setId(status.getId());
                    t.setDate(status.getCreatedAt());
                    if(status.getGeoLocation() != null)
                        t.setLocation(status.getGeoLocation().toString());
                    t.setName(status.getUser().getName());
                    t.setDescriptionAuthor(status.getUser().getDescription());
                    t.setLang(status.getLang());
                    t.setCorpus(corpus);
                    if(tweetRepository != null)
                        tweetRepository.save(t);
                }

            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice sdn) {
                System.out.println(sdn);
            }

            @Override
            public void onTrackLimitationNotice(int i) {
                corpus.setLimitationNotice(i);
                corpus = corpusRepository.save(corpus);

            }

            @Override
            public void onScrubGeo(long l, long l1) {
                System.out.println(l + "" + l1);
            }

            @Override
            public void onStallWarning(StallWarning sw) {
                System.out.println(sw);
            }

            @Override
            public void onException(Exception excptn) {
                corpus.setErrorMessage(excptn.getMessage());
                setStatus(State.ERROR);
                excptn.printStackTrace();
            }
        };
        twitterStream.addListener(listener);
        twitterStream.filter(filter);

        return false;
    }

    private void setStatus(State status){
        corpus.setState(status);
        corpus = corpusRepository.save(corpus);
    }


    public Corpus getCorpus(){
        return corpus;
    }

    public void shutdown(){
        setStatus(State.SHUTDOWN);
        twitterStream.shutdown();
        setChanged();
        notifyObservers(corpus.getState());
    }

    @Override
    public void run() {
        runCollect();
    }

    public State isInProgress(){
        return corpus.getState();
    }

    public enum State{
        INPROGRESS,
        WAITING_FOR_CONNECTION,
        ERROR,
        SHUTDOWN;
        public boolean isError(){return equals(ERROR);}
        public boolean isInprogress(){return equals(INPROGRESS);}
        public boolean isShutdown(){return equals(SHUTDOWN);}
        public boolean isWaitingForConnection(){return equals(WAITING_FOR_CONNECTION);}
    }


}

