package isf.proagro.reader;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

/**
 * Created by Scrappy on 13-12-13.
 */
public class UpdateLibraryActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Button updateHomeButton = (Button) findViewById(R.id.updateHomeButton);
        updateHomeButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v)
            {
                finish();
            }
        });

        UpdateLibraryTask task = new UpdateLibraryTask(this);
        task.execute();
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
