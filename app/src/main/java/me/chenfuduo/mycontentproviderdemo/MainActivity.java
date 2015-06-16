package me.chenfuduo.mycontentproviderdemo;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.UserDictionary;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    // A "projection" defines the columns that will be returned for each row
    String[] mProjection =
            {
                    UserDictionary.Words._ID,    // Contract class constant for the _ID column name
                    UserDictionary.Words.WORD,   // Contract class constant for the word column name
                    UserDictionary.Words.LOCALE  // Contract class constant for the locale column name
            };

    // Defines a string to contain the selection clause
    String mSelectionClause = null;

    // Initializes an array to contain selection arguments
    String[] mSelectionArgs = {""};


    String mSearchString;

    ListView listView;

    // Defines a list of columns to retrieve from the Cursor and load into an output row
    String[] mWordListColumns =
            {
                    UserDictionary.Words.WORD,   // Contract class constant containing the word column name
                    UserDictionary.Words.LOCALE  // Contract class constant containing the locale column name
            };
    // Defines a list of View IDs that will receive the Cursor columns for each row
    int[] mWordListItems = { R.id.word, R.id.locale};


    private SimpleCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EditText etQueryInput = (EditText) findViewById(R.id.etInputQueryWords);
        mSearchString = etQueryInput.getText().toString();

        // Remember to insert code here to check for invalid or malicious input.

        // If the word is the empty string, gets everything

        if (TextUtils.isEmpty(mSearchString)) {
            // Setting the selection clause to null will return all words
            mSelectionClause = null;
            mSelectionArgs[0] = "";

        } else {
            // Constructs a selection clause that matches the word that the user entered.
            mSelectionClause = UserDictionary.Words.WORD + " = ?";

            // Moves the user's input string to the selection arguments.
            mSelectionArgs[0] = mSearchString;

        }
        Button btnQueryUserDic = (Button) findViewById(R.id.queryUserDic);
        btnQueryUserDic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "查询用户字典的按钮被点击了", Toast.LENGTH_SHORT).show();
                Cursor mCursor = getContentResolver().query(UserDictionary.Words.CONTENT_URI,
                        mProjection, mSelectionClause, mSelectionArgs, null);
                // Some providers return null if an error occurs, others throw an exception

                // Determine the column index of the column named "word"
                int index = mCursor.getColumnIndex(UserDictionary.Words.WORD);

                if (null == mCursor) {
                 /*
                 * Insert code here to handle the error. Be sure not to use the cursor! You may want to
                 * call android.util.Log.e() to log this error.
                 *
                 */
                    Log.e("Test", "cursor为空");
                    // If the Cursor is empty, the provider found no matches
                } else if (mCursor.getCount() < 1) {
                    /*
                    * Insert code here to notify the user that the search was unsuccessful. This isn't necessarily
                    * an error. You may want to offer the user the option to insert a new row, or re-type the
                    * search term.
                    */
                    Log.e("Test", "查询失败");
                } else {
                    // Insert code here to do something with the results
                    while (mCursor.moveToNext()){
                        String newWord = mCursor.getString(index);
                        Log.e("Test","newWord--->" + newWord);
                    }
                }
            }
        });

    }

}
