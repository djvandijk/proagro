package isf.proagro.android.model;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

import io.realm.RealmObject;

/**
 * Created by eddyhugues on 15-05-17.
 */
@Root(name = "booklet")
public class Booklet extends RealmObject {

    @Element
    private String title;
    @Element
    private String description;
    @Element
    private String pdf;
    @Element
    private String cover;
    @Element
    private String language;

    private String pdfPath;

    private boolean isFavorite = false;

    private Category category;

    public String getPdfPath() {
        return pdfPath;
    }

    public void setPdfPath(String pdfPath) {
        this.pdfPath = pdfPath;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getPdf() {
        return pdf;
    }

    public String getCover() {
        return cover;
    }

    public String getLanguage() {
        return language;
    }

    public Category getCategory() { return category; }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }

    public void setCategory(Category category) { this.category = category; }
}
