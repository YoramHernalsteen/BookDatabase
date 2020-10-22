package be.thomasmore.bookdb;

import org.springframework.web.bind.annotation.RequestParam;

public class AuthorForm {
    private String authorName;
    private String authorKnownFor;
    private String authorInfo;

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorKnownFor() {
        return authorKnownFor;
    }

    public void setAuthorKnownFor(String authorKnownFor) {
        this.authorKnownFor = authorKnownFor;
    }

    public String getAuthorInfo() {
        return authorInfo;
    }

    public void setAuthorInfo(String authorInfo) {
        this.authorInfo = authorInfo;
    }
}
