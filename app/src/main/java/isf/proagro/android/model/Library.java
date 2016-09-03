package isf.proagro.android.model;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

import java.util.List;

/**
 * Created by eddyhugues on 15-05-17.
 */
@Root(name = "library")
public class Library {
    @ElementList(entry="category", inline=true)
    private List<Category> categories;

    public List<Category> getCategories() {
        return categories;
    }
}
