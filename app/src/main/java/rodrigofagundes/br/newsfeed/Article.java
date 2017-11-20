package rodrigofagundes.br.newsfeed;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by rmfagundes on 20/11/2017.
 */

public class Article {
    private String title;
    private String section;
    private String author;
    private Date publishedOn;
    private String link;

    public Article(String title, String section, String author, Date publishedOn, String link) {
        this.title = title;
        this.section = section;
        this.author = author;
        this.publishedOn = publishedOn;
        this.link = link;
    }

    public String getTitle() {
        return title;
    }
    public String getSection() {
        return section;
    }
    public String getAuthor() {
        return author;
    }
    public Date getPublishedOn() {
        return publishedOn;
    }
    public String getLink() { return link; }
}
