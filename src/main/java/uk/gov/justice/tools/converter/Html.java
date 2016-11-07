package uk.gov.justice.tools.converter;


public class Html {
    private String htmlAsText;

    private String filePath;

    public Html(String htmlAsText, String filePath) {
        this.htmlAsText = htmlAsText;
        this.filePath = filePath;
    }

    public String getHtmlAsText() {
        return htmlAsText;
    }

    public String getFilePath() {
        return filePath;
    }
}
