/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cats.twitter.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Type;

import javax.persistence.*;

import java.util.Date;

/**
 *
 * @author Anthony Deseille
 */

@Entity
public class Tweet {

    @Id
    @SequenceGenerator(name="tweet_seq",
            sequenceName="tweet_seq",
            allocationSize=1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator="tweet_seq")
    private Long id;
    
    private Long author;

    @JsonIgnore
    @ManyToOne
    private Corpus corpus;



    @ManyToOne
    private SubCorpus subCorpus;

    @Type(type="text")
    @Column(name = "text")
    private String text;
    
    private String location;

    @Type(type="text")
    @Column(name = "descriptionAuthor")
    private String descriptionAuthor;
    
    private String name;

    private Date date;

    private String lang;

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public Long getAuthor() {
        return author;
    }

    public void setAuthor(Long author) {
        this.author = author;
    }

    @JsonIgnore
    public User getUser() {
        return corpus.getUser();
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLocation() {
        return location;
    }

    public Tweet() {
    }

    public Long getId() {
        return id;
    }

    public String getDescriptionAuthor() {
        return descriptionAuthor;
    }

    public void setDescriptionAuthor(String description) {
        this.descriptionAuthor = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getText() {
        return text;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }



    @JsonIgnore
    public Corpus getCorpus() {
        return corpus;
    }

    public void setCorpus(Corpus corpus) {
        this.corpus = corpus;
    }

    public SubCorpus getSubCorpus() {
        return subCorpus;
    }

    public void setSubCorpus(SubCorpus subCorpus) {
        this.subCorpus = subCorpus;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
