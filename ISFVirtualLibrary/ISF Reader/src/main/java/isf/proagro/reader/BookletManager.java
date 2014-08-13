package isf.proagro.reader;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Configuration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Scrappy on 26-11-13.
 */
public class BookletManager {

    public static String LibraryPath;

    private static File _directoryPath;

    private static List<Booklet> _booklets = new ArrayList<Booklet>();

    //public static String _serverLibraryUrlBase = "http://www.florimel.nl/isf/library/";
    public static String _serverLibraryUrlBase = "http://isf-cameroun.org/library/";

    public static String _serverLibraryUrl;

    public static String GetServerLibraryUrl() { return _serverLibraryUrl; }

    private static String _languageCode = "en";

    public static String GetLanguageCode() { return _languageCode; }

    public static void SetLanguageCode(String languageCode)
    {
        int index = LibraryPath.lastIndexOf("/"+_languageCode+"/");
        LibraryPath = LibraryPath.substring(0, index) + "/" + languageCode+"/";
        _directoryPath = new File(LibraryPath);
        _serverLibraryUrl = _serverLibraryUrlBase + languageCode + "/";
        _languageCode = languageCode;
    }

    public static void SetLanguageCode(String languageCode, Context context)
    {
        if(languageCode.equalsIgnoreCase("en"))
        {
            Locale englishLocale = new Locale("en");
            Locale.setDefault(englishLocale);
            Configuration config = new Configuration();
            config.locale = englishLocale;
            context.getResources().updateConfiguration(config,
                    context.getResources().getDisplayMetrics());
        }
        else
        {
            Locale frenchLocale = new Locale("fr");
            Locale.setDefault(frenchLocale);
            Configuration config = new Configuration();
            config.locale = frenchLocale;
            context.getResources().updateConfiguration(config,
                    context.getResources().getDisplayMetrics());
        }
        SetLanguageCode(languageCode);
    }

    public static String FileNameWithoutExtension(String fileName)
    {
        return fileName.replaceFirst("[.][^.]+$", "");
    }

    public static void Init(Activity activity)
    {
        LibraryPath = activity.getFilesDir() + "/isf/"+ _languageCode +"/";
        _directoryPath = new File(LibraryPath);
        _serverLibraryUrl = _serverLibraryUrlBase + _languageCode + "/";
        SetLanguageCode("en", activity.getBaseContext());
        ExtractAssets(activity.getAssets());
        SetLanguageCode("fr", activity.getBaseContext());
        ExtractAssets(activity.getAssets());
        InitBooklets();
    }

    public static void ClearLibrary()
    {
        for(String bookletPath : _directoryPath.list())
        {
            File f = new File(LibraryPath +bookletPath);
            f.delete();
        }
        _booklets.clear();
    }

    public static String encodeURIComponent(String s) {
        String result;

        try {
            result = URLEncoder.encode(s, "UTF-8")
                    .replaceAll("\\+", "%20")
                    .replaceAll("\\%21", "!")
                    .replaceAll("\\%27", "'")
                    .replaceAll("\\%28", "(")
                    .replaceAll("\\%29", ")")
                    .replaceAll("\\%7E", "~");
        } catch (UnsupportedEncodingException e) {
            result = s;
        }

        return result;
    }

    private static void ExtractAssets(AssetManager assetManager)
    {
        if(!_directoryPath.exists())
        {
            _directoryPath.mkdirs();
        }

        try
        {
            String[] libraryAssets = assetManager.list("library/"+ _languageCode);

            for(String libraryAsset : libraryAssets)
            {
                if(libraryAsset.toLowerCase().endsWith(".xml"))
                {
                    File xml = new File(LibraryPath + libraryAsset);
                    if(!xml.exists())
                    {
                        CopyAssetFile(FileNameWithoutExtension(libraryAsset) + ".pdf", assetManager);
                        CopyAssetFile(FileNameWithoutExtension(libraryAsset) + ".jpg", assetManager);
                        CopyAssetFile(FileNameWithoutExtension(libraryAsset)+".xml", assetManager);
                    }
                }
            }
        }
        catch(IOException ioException)
        {
            System.out.println(ioException.getMessage());
        }
    }

    private static void CopyFile(InputStream in, OutputStream out) throws IOException
    {
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            out.write(buffer, 0, read);
        }
    }
      
    private static void CopyAssetFile(String fileName, AssetManager assetManager)
    {
        try
        {
            InputStream inputStream = assetManager.open("library/" + _languageCode + "/" + fileName);
            File outputFile = new File(LibraryPath + fileName);
            outputFile.createNewFile();
            OutputStream outputStream = new FileOutputStream(outputFile);
            CopyFile(inputStream, outputStream);
            inputStream.close();
            outputStream.close();
        }
        catch(IOException ioException)
        {
            System.out.println(ioException.getMessage());
        }
    }

    public static void InitBooklets()
    {
        _booklets = new ArrayList<Booklet>();

        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.toLowerCase().endsWith(".xml");
            }
        };

        for(String bookletPath : _directoryPath.list(filter))
        {
            Booklet booklet = InitBooklet(LibraryPath +bookletPath);
            _booklets.add(booklet);
        }
    }

    private static Booklet InitBooklet(String bookletPath)
    {
        try
        {
            InputStream stream = new FileInputStream(bookletPath);
            Booklet booklet = Booklet.FromXmlStream(stream);

            if(booklet != null)
            {
                File pdfFile = new File(LibraryPath + booklet.Pdf);
                File coverFile = new File(LibraryPath + booklet.Cover);

                if(pdfFile.exists() && coverFile.exists())
                {
                    return booklet;
                }
            }
        }
        catch(IOException ioException)
        {
            System.out.println(ioException.getMessage());
        }
        return null;
    }

    public static List<Booklet> GetAvailableBooklets()
    {
        return _booklets;
    }
}
