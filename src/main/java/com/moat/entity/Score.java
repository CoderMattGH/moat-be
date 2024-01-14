package com.moat.entity;

import javax.persistence.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * An Entity class representing a Score.
 */
@Entity
@Table(name = "Score")
@NamedQueries({
        @NamedQuery(name=Score.FIND_ALL, query="select s from Score s"),
        @NamedQuery(name=Score.FIND_TOP_TEN, query="select s from Score s ORDER BY s.score DESC")
})
public class Score implements Serializable {
    static Logger logger = LoggerFactory.getLogger(Score.class);

    public static final String FIND_ALL = "Score.findAll";
    public static final String FIND_TOP_TEN = "Score.findTopTen";
    
    private final static int NAME_MIN_LENGTH = 5;
    private final static int NAME_MAX_LENGTH = 10;

    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE)
    @Column(name = "ID")
    private Long id;

    @Column(name = "SCORE")
    private int score;

    @Column(name = "NICKNAME")
    private String nickname;

    public Score() {
    }

    public Score(int score, String nickname) {
        setScore(score);
        setNickname(nickname);
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getScore() {
        return this.score;
    }

    public void setScore(int score) {
        if (score < 0)
            throw new IllegalArgumentException("Score cannot be below 0.");
        else
            this.score = score;
    }

    public String getNickname() {
        return this.nickname;
    }

    /**
     * Sets the Nickname.  This method will try and validate the Nickname.  If the Nickname is
     * considered invalid, then the function will throw an IllegalArgumentException error.
     * @param nickname A String representing the Nickname.
     * @throws IllegalArgumentException When the Nickname is not considered valid.
     */
    public void setNickname(String nickname) {
        // Validate nickname.
        if (nickname == null || nickname == "")
            throw new IllegalArgumentException("Nickname cannot be null or empty.");

        if (nickname.length() < NAME_MIN_LENGTH || nickname.length() > NAME_MAX_LENGTH)
            throw new IllegalArgumentException("Nickname is the incorrect length.");

        String regex = String.format("^[a-zA-Z0-9]{%d,%d}$", NAME_MIN_LENGTH, NAME_MAX_LENGTH);

        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(nickname);

        if(!matcher.find())
            throw new IllegalArgumentException("Nickname contains invalid characters.");

        nickname = nickname.toUpperCase();

        this.nickname = nickname;
    }
}
