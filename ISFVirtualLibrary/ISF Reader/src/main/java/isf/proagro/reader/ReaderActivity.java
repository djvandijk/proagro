package isf.proagro.reader;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.radaee.pdf.Document;
import com.radaee.reader.PDFReader;
import com.radaee.util.PDFFileStream;

/**
 * Created by Scrappy on 26-11-13.
 */
public class ReaderActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reader);
        Bundle b = getIntent().getExtras();
        String pdfName = b.getString("pdf");

        try
        {
            PDFReader reader = (PDFReader) findViewById(R.id.mainReader);
            String pathToPDF = BookletManager.LibraryPath+pdfName;

            PDFFileStream stream = new PDFFileStream();
            stream.open(pathToPDF);
            Document doc = new Document();
            int result = doc.OpenStream(stream, "dummy");

            switch( result )
            {
                case -1://need input password
                    break;
                case -2://unknown encryption
                    break;
                case -3://damaged or invalid format
                    break;
                case -10://access denied or invalid file path
                    break;
                case 0://succeeded, and continue
                    break;
                default://unknown error
                    break;
            }
            reader.PDFOpen(doc, true, null);
        }
        catch(Exception ex)
        {
            System.out.println("schade");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.reader, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch(id)
        {
            case R.id.action_change_language:
                return true;
            case R.id.action_home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
