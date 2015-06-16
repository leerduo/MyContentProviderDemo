package me.chenfuduo.mycontentproviderdemo;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
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
    String mSearchString;
    ListView listView;
    // Defines a list of columns to retrieve from the Cursor and load into an output row
    String[] mWordListColumns =
            {
                    UserDictionary.Words.WORD,   // Contract class constant containing the word column name
                    UserDictionary.Words.LOCALE  // Contract class constant containing the locale column name
            };
    // Defines a list of View IDs that will receive the Cursor columns for each row
    int[] mWordListItems = {R.id.word, R.id.locale};
    private SimpleCursorAdapter mCursorAdapter;
    Cursor mCursor;
    // Defines a new Uri object that receives the result of the insertion
    Uri mNewUri;
    final ContentValues mNewValues = new ContentValues();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.list);

        Button btnQueryUserDic = (Button) findViewById(R.id.queryUserDic);
        btnQueryUserDic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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

                EditText etQueryInput = (EditText) findViewById(R.id.etInputQueryWords);
                mSearchString = etQueryInput.getText().toString();

                String mSortOrder = " word ASC";

                // Remember to insert code here to check for invalid or malicious input.

                // If the word is the empty string, gets everything

                if (TextUtils.isEmpty(mSearchString)) {
                    // Setting the selection clause to null will return all words
                    mSelectionClause = null;
                    mSelectionArgs = null;

                } else {
                    // Constructs a selection clause that matches the word that the user entered.
                    mSelectionClause = UserDictionary.Words.WORD+" = ?";

                    // Moves the user's input string to the selection arguments.
                    mSelectionArgs[0] = mSearchString;

                }


                // Does a query against the table and returns a Cursor object
                                // The sort order for the returned rows
                mCursor = getContentResolver().query(
                         UserDictionary.Words.CONTENT_URI,  // The content URI of the words table
                         mProjection,                       // The columns to return for each row
                         mSelectionClause,                   // Either null, or the word the user entered
                         mSelectionArgs,                    // Either empty, or the string the user entered
                        mSortOrder);

                Log.i("Test", UserDictionary.Words.CONTENT_URI.toString());

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
                    Log.e("Test", "查询失败" + "mCursor.getCount()---->" + mCursor.getCount());
                } else {
                    // Insert code here to do something with the results
                    while (mCursor.moveToNext()) {
                        String newWord = mCursor.getString(index);
                        Log.e("Test", "newWord--->" + newWord);
                    }

                    // Creates a new SimpleCursorAdapter
                    mCursorAdapter = new SimpleCursorAdapter(
                            getApplicationContext(),               // The application's Context object
                            R.layout.word_item,                  // A layout in XML for one row in the ListView
                            mCursor,                               // The result from the query
                            mWordListColumns,                      // A string array of column names in the cursor
                            mWordListItems,                        // An integer array of view IDs in the row layout
                            0);                                    // Flags (usually none are needed)

                    // Sets the adapter for the ListView
                    listView.setAdapter(mCursorAdapter);

                }
            }
        });

        Button btnInsertUserDic = (Button) findViewById(R.id.insertUserDic);
        btnInsertUserDic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    /*
                     * Sets the values of each column and inserts the word. The arguments to the "put"
                     * method are "column name" and "value"
                     */
                mNewValues.put(UserDictionary.Words.APP_ID, "example.user");
                mNewValues.put(UserDictionary.Words.LOCALE, "en_US");
                mNewValues.put(UserDictionary.Words.WORD, "insert");
                mNewValues.put(UserDictionary.Words.FREQUENCY, "100");

                mNewUri = getContentResolver().insert(
                        UserDictionary.Words.CONTENT_URI,   // the user dictionary content URI
                        mNewValues                          // the values to insert
                );

            }
        });

        Button btnUpdateUserPic = (Button) findViewById(R.id.updateUserDic);
        btnUpdateUserPic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Defines an object to contain the updated values
                ContentValues mUpdateValues = new ContentValues();
                /*
                *       Sets the updated value and updates the selected words.
                 */
                mUpdateValues.put(UserDictionary.Words.WORD, "insert2");

               getContentResolver().update(
                        UserDictionary.Words.CONTENT_URI,   // the user dictionary content URI
                        mUpdateValues,                       // the columns to update
                        null,                    // the column to select on
                        null                      // the value to compare to
                );
            }
        });

        Button btnDeleteUserDic = (Button) findViewById(R.id.deleteUserDic);

        btnDeleteUserDic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              getContentResolver().delete(
                        UserDictionary.Words.CONTENT_URI,   // the user dictionary content URI
                        null,                    // the column to select on
                        null                     // the value to compare to
                );
            }
        });

    }

}
