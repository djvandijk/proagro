package isf.proagro.android.model;

import org.simpleframework.xml.Attribute;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Ignore;

/**
 * Created by tito on 01/09/16.
 */
@Root(name = "category")
public class Category extends RealmObject {

    @Attribute(required = true)
    private String name;

    @ElementList(entry="booklet", inline=true)
    @Ignore
    private List<String> xmlBookletNames;
    public List<String> getXmlBookletNames() { return xmlBookletNames; }
    public void setXmlBookletNames(List<String> xmlBookletNames) { this.xmlBookletNames = xmlBookletNames; }

    // Realm does not support lists of primitive types so we join the booklet names into a single string
    private String bookletNames;

    private RealmList<Booklet> booklets;
    private String language;

    public String getName() {
        return name;
    }
    public void setName(String name) { this.name = name; }

    public RealmList<Booklet> getBooklets() {
        return booklets;
    }
    public void setBooklets(RealmList<Booklet> booklets) { this.booklets = booklets; }

    public String getBookletNames() { return bookletNames; }
    public void setBookletNames(String bookletNames) { this.bookletNames = bookletNames; }

    public String getLanguage() { return language; }
    public void setLanguage(String language) { this.language = language; }
}