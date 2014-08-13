package isf.proagro.reader;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Scrappy on 26-11-13.
 */
public class Booklet {

    public String Title;

    public String Description;

    public String Pdf;

    public String Cover;

    public Booklet(String title, String description, String pdf, String cover)
    {
        Title = title;
        Description = description;
        Pdf = pdf;
        Cover = cover;
    }

    public Bitmap GetCover() {
        return GetBitmapFromPath(BookletManager.LibraryPath+Cover);
    }

    public static Bitmap GetBitmapFromPath(String path)
    {
        Bitmap bmp = null;
        try {
            InputStream in = new FileInputStream(path);
            bmp = BitmapFactory.decodeStream(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bmp;
    }

    public static Booklet FromXmlStream(InputStream stream)
    {
        try
        {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(stream, null);
            String title= "";
            String description = "";
            String pdf = "";
            String cover = "";

            parser.next();
            if(parser.getName().equalsIgnoreCase("booklet"))
            {
                while (parser.next() != XmlPullParser.END_TAG)
                {
                    if (parser.getEventType() != XmlPullParser.START_TAG) { continue;}
                    if(parser.getName().equalsIgnoreCase("title"))
                    {
                        parser.next();
                        title = parser.getText();
                        parser.next();
                    }
                    else if(parser.getName().equalsIgnoreCase("description"))
                    {
                        parser.next();
                        description = parser.getText();
                        parser.next();
                    }
                    else if(parser.getName().equalsIgnoreCase("pdf"))
                    {
                        parser.next();
                        pdf = parser.getText();
                        parser.next();
                    }
                    else if(parser.getName().equalsIgnoreCase("cover"))
                    {
                        parser.next();
                        cover = parser.getText();
                        parser.next();
                    }
                }
            }

            return new Booklet(title, description, pdf, cover);
        }
        catch (XmlPullParserException e)
        {
            e.printStackTrace();
        }
        catch(IOException ioException)
        {
            System.out.println(ioException.getMessage());
        }
        return null;
    }
}
