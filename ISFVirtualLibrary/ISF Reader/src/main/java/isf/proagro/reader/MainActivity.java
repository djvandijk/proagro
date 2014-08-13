package isf.proagro.reader;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends ActionBarActivity {

    public void OpenBooklet(Booklet booklet)
    {
        Intent switchToReader = new Intent(this, ReaderActivity.class);
        Bundle b = new Bundle();
        b.putString("pdf", booklet.Pdf);
        switchToReader.putExtras(b);
        startActivity(switchToReader);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new BookletBrowserFragment())
                    .commit();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        BookletManager.InitBooklets();
        ((BookletBrowserFragment) getSupportFragmentManager().findFragmentById(R.id.container)).UpdateBookletBrowser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        if(BookletManager.GetLanguageCode().equalsIgnoreCase("en"))
            menu.findItem(R.id.action_change_language).setIcon(R.drawable.flag_fr);
        else
            menu.findItem(R.id.action_change_language).setIcon(R.drawable.flag_en);
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
                if(BookletManager.GetLanguageCode().equalsIgnoreCase("en"))
                    BookletManager.SetLanguageCode("fr", getBaseContext());
                else
                    BookletManager.SetLanguageCode("en", getBaseContext());

                finish();
                startActivity(getIntent());
                return true;
            case R.id.action_update:
                Intent switchToUpdate = new Intent(this, UpdateLibraryActivity.class);
                startActivity(switchToUpdate);
                return true;
            case R.id.action_clear_library:
                BookletManager.ClearLibrary();
                ((BookletBrowserFragment) getSupportFragmentManager().findFragmentById(R.id.container)).UpdateBookletBrowser();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
