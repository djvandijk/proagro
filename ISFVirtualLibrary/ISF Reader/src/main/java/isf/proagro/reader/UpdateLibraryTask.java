package isf.proagro.reader;

import android.os.AsyncTask;
import android.util.Xml;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Scrappy on 11-12-13.
 */
public class UpdateLibraryTask extends AsyncTask<Void, UpdateProgress, Void> {

    private UpdateLibraryActivity _updateActivity;

    private TextView statusTextView;
    private TextView minorStatusTextView;
    private TextView downloadTextView;
    private LinearLayout updateBookletInfoView;
    private Button updateHomeButton;
    private TextView titleTextView;
    private ImageView updateImageView;

    public UpdateLibraryTask(UpdateLibraryActivity updateActivity)
    {
        super();
        _updateActivity = updateActivity;

        statusTextView = (TextView) _updateActivity.findViewById(R.id.updateStatus);
        minorStatusTextView = (TextView) _updateActivity.findViewById(R.id.updateMinorStatus);
        downloadTextView = (TextView) _updateActivity.findViewById(R.id.updateDownloadLabel);
        updateBookletInfoView = (LinearLayout) _updateActivity.findViewById(R.id.updateBookletInfoView);
        updateHomeButton = (Button) _updateActivity.findViewById(R.id.updateHomeButton);
        titleTextView = (TextView) _updateActivity.findViewById(R.id.updateTitleLabel);
        updateImageView = (ImageView) _updateActivity.findViewById(R.id.updateImageView);
    }

    private void hideNonStatusFields()
    {
        updateBookletInfoView.setVisibility(View.GONE);
        minorStatusTextView.setVisibility(View.INVISIBLE);
        updateHomeButton.setVisibility(View.VISIBLE);
    }

    protected void onProgressUpdate(UpdateProgress... progress) {

        switch(progress[0].Type)
        {
            case GENERIC:
                statusTextView.setText(progress[0].Message);
                break;
            case MINOR:
                minorStatusTextView.setText(progress[0].Message);
                break;
            case DOWNLOAD_PROGRESS:
                downloadTextView.setVisibility(View.VISIBLE);
                downloadTextView.setText("Downloading... " + progress[0].Message);
                break;
            case BOOKLET_INFO:
                titleTextView.setVisibility(View.VISIBLE);
                titleTextView.setText(progress[0].Message);
                break;
            case COVER:
                updateImageView.setImageBitmap(Booklet.GetBitmapFromPath(progress[0].Message));
                break;
            case FINISHED:
                hideNonStatusFields();
                statusTextView.setText("Done");
                minorStatusTextView.setText("");
                break;
            case ERROR:
                hideNonStatusFields();
                statusTextView.setText("Error during update.");
                break;
        }
    }

    @Override
    protected Void doInBackground(Void... voids) {
        boolean errorDuringUpdate = true;
        try {
            String currentLanguageCode = BookletManager.GetLanguageCode();
            BookletManager.SetLanguageCode("en");
            publishProgress(new UpdateProgress(UpdateLibraryEvent.UpdateType.GENERIC, "Retrieving English booklets."));
            DoFetchBooklets();
            BookletManager.SetLanguageCode("fr");
            publishProgress(new UpdateProgress(UpdateLibraryEvent.UpdateType.GENERIC, "Retrieving French booklets."));
            DoFetchBooklets();
            BookletManager.SetLanguageCode(currentLanguageCode);
            publishProgress(new UpdateProgress(UpdateLibraryEvent.UpdateType.FINISHED));
            errorDuringUpdate = false;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        } catch (XmlPullParserException xmlEx) {
            xmlEx.printStackTrace();
        }

        if(errorDuringUpdate) {
            publishProgress(new UpdateProgress(UpdateLibraryEvent.UpdateType.ERROR));
        }
        return null;
    }

    private void HideNonStatusFields()
    {
        updateImageView.setVisibility(View.INVISIBLE);
    }

    private void DoFetchBooklets() throws IOException, XmlPullParserException {
        publishProgress(new UpdateProgress(UpdateLibraryEvent.UpdateType.START));
        URL url = new URL(BookletManager.GetServerLibraryUrl() + "library.xml");

        InputStream stream = url.openStream();
        XmlPullParser parser = Xml.newPullParser();
        parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
        parser.setInput(stream, null);
        List<String> availableServerBooklets = new ArrayList<String>();

        parser.next();
        if(parser.getName().equalsIgnoreCase("library"))
        {
            while (parser.next() != XmlPullParser.END_TAG)
            {
                if (parser.getEventType() != XmlPullParser.START_TAG) { continue;}
                if(parser.getName().equalsIgnoreCase("booklet"))
                {
                    parser.next();
                    availableServerBooklets.add(parser.getText());
                    parser.next();
                }
            }
        }
        stream.close();

        List<String> newBooklets = new ArrayList<String>();
        for(String serverBooklet : availableServerBooklets)
        {
            File localFile = new File(BookletManager.LibraryPath+serverBooklet);
            if(!localFile.exists())
            {
                newBooklets.add(serverBooklet);
            }
        }

        int count = 1;
        for(String serverBooklet : newBooklets)
        {
            String message = "Retrieving booklet " + count++ +" of " + newBooklets.size();
            publishProgress(new UpdateProgress(UpdateLibraryEvent.UpdateType.MINOR, message));
            InstallServerBooklet(serverBooklet);
        }
    }

    private void InstallServerBooklet(String serverBooklet)
    {
        try {
            URL url = new URL(BookletManager.GetServerLibraryUrl() + BookletManager.encodeURIComponent(serverBooklet));
            InputStream xmlStream = url.openStream();
            Booklet booklet = Booklet.FromXmlStream(xmlStream);

            publishProgress(new UpdateProgress(UpdateLibraryEvent.UpdateType.BOOKLET_INFO, booklet.Title));

            DownloadFile(booklet.Cover);
            publishProgress(new UpdateProgress(UpdateLibraryEvent.UpdateType.COVER, BookletManager.LibraryPath +booklet.Cover));

            DownloadFile(booklet.Pdf);
            publishProgress(new UpdateProgress(UpdateLibraryEvent.UpdateType.PDF, BookletManager.LibraryPath + booklet.Pdf));

            DownloadFile(serverBooklet);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException ioEx) {
            ioEx.printStackTrace();
        }
    }

    private void DownloadFile(String fileName)
    {
        try
        {
            URL url = new URL(BookletManager.GetServerLibraryUrl()+BookletManager.encodeURIComponent(fileName));
            URLConnection connection = url.openConnection();
            connection.connect();
            FileOutputStream coverOutput = new FileOutputStream(BookletManager.LibraryPath +fileName);
            CopyFile(connection.getInputStream(), coverOutput, connection.getContentLength());
        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void CopyFile(InputStream in, OutputStream out, int total) throws IOException {
        int current = 0;
        publishProgress(new UpdateProgress(UpdateLibraryEvent.UpdateType.DOWNLOAD_PROGRESS, "0 %"));
        byte[] buffer = new byte[1024];
        int read;
        while((read = in.read(buffer)) != -1){
            current += read;
            out.write(buffer, 0, read);
            int progress = (int) (((float) current / total ) * 100);
            publishProgress(new UpdateProgress(UpdateLibraryEvent.UpdateType.DOWNLOAD_PROGRESS, progress + " %"));
        }
        in.close();
        out.close();
        publishProgress(new UpdateProgress(UpdateLibraryEvent.UpdateType.DOWNLOAD_PROGRESS,  "100 %"));
    }
}
