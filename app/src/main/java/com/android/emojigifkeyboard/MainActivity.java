package com.android.emojigifkeyboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.kevalpatel2106.emoticongifkeyboard.EmoticonGIFKeyboardFragment;
import com.kevalpatel2106.emoticongifkeyboard.emoticons.Emoticon;
import com.kevalpatel2106.emoticongifkeyboard.emoticons.EmoticonSelectListener;
import com.kevalpatel2106.emoticongifkeyboard.gifs.Gif;
import com.kevalpatel2106.emoticongifkeyboard.gifs.GifSelectListener;
import com.kevalpatel2106.emoticongifkeyboard.widget.EmoticonEditText;
import com.kevalpatel2106.emoticongifkeyboard.widget.EmoticonTextView;
import com.kevalpatel2106.emoticonpack.ios.IosEmoticonProvider;
import com.kevalpatel2106.gifpack.giphy.GiphyGifProvider;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private EmoticonGIFKeyboardFragment mEmoticonGIFKeyboardFragment;

    /**
     * Manually toggle soft keyboard visibility
     *
     * @param context calling context
     */
    public static void toggleKeyboardVisibility(Context context) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null)
            inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final AppCompatImageView gifImageView = findViewById(R.id.selected_git_iv);

        //Set the emoticon text view.
        final EmoticonTextView textView = findViewById(R.id.selected_emoticons_tv);
        /*
          Set the custom emoticon icon provider. If you don't set any icon provider here, library
          will render system emoticons. Here we are setting iOS emoticons icon pack.
         */
        textView.setEmoticonProvider(IosEmoticonProvider.create());


        //Set the emoticon edit text.
        final EmoticonEditText editText = findViewById(R.id.selected_emoticons_et);
        /*
          Set the custom emoticon icon provider. If you don't set any icon provider here, library
          will render system emoticons. Here we are setting Android 8.0 emoticons icon pack.
         */
        editText.setEmoticonProvider(IosEmoticonProvider.create());


        //Set emoticon configuration.
        EmoticonGIFKeyboardFragment.EmoticonConfig emoticonConfig = new EmoticonGIFKeyboardFragment.EmoticonConfig()

                /*
                  Set the custom emoticon icon provider. If you don't set any icon provider here, library
                  will render system emoticons. Here we are setting Windows 10 emoticons icon pack.
                 */
                .setEmoticonProvider(IosEmoticonProvider.create())

                /*
                  Set the emoticon select listener. This will notify you when user selects any emoticon from
                  list or user preses back button.
                  NOTE: The process of removing last character when user preses back space will handle
                  by library if your edit text is in focus.
                 */
                .setEmoticonSelectListener(new EmoticonSelectListener() {

                    @Override
                    public void emoticonSelected(Emoticon emoticon) {
                        //Do something with new emoticon.
                        Log.d(TAG, "emoticonSelected: " + emoticon.getUnicode());
                        editText.append(emoticon.getUnicode(),
                                editText.getSelectionStart(),
                                editText.getSelectionEnd());
                    }

                    @Override
                    public void onBackSpace() {
                        //Do something here to handle backspace event.
                        //The process of removing last character when user preses back space will handle
                        //by library if your edit text is in focus.
                    }
                });

        //Create GIF config
        EmoticonGIFKeyboardFragment.GIFConfig gifConfig = new EmoticonGIFKeyboardFragment
                /*
                  Set the desired GIF provider. Here we are using GIPHY to provide GIFs.
                  Create Giphy GIF provider by passing your key.
                  It is required to set GIF provider before adding fragment into container.
                 */
                .GIFConfig(GiphyGifProvider.create(this, "564ce7370bf347f2b7c0e4746593c179"))

                /*
                  Implement GIF select listener. This will notify you when user selects new GIF.
                 */
                .setGifSelectListener(new GifSelectListener() {
                    @Override
                    public void onGifSelected(@NonNull Gif gif) {
                        //Do something with the selected GIF.
                        Log.d(TAG, "onGifSelected: " + gif.getGifUrl());

                    }
                });



        /*
          Create instance of emoticon gif keyboard by passing emoticon and gif config. If you pass null
          to emoticon config, emoticon functionality will be disabled. Also, if you pass gif config
          as null, GIF functionality will be disabled.
         */
        mEmoticonGIFKeyboardFragment = EmoticonGIFKeyboardFragment
                .getNewInstance(findViewById(R.id.keyboard_container), emoticonConfig, gifConfig);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.keyboard_container, mEmoticonGIFKeyboardFragment)
                .commit();
        mEmoticonGIFKeyboardFragment.open(); //Open the fragment by default while initializing.


        //Set smiley button to open/close the emoticon gif keyboard
        findViewById(R.id.emoji_open_close_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mEmoticonGIFKeyboardFragment.toggle();
                toggleKeyboardVisibility(MainActivity.this);
            }
        });

        //Send button
        findViewById(R.id.emoji_send_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(editText.getText());
                editText.setText("");
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (mEmoticonGIFKeyboardFragment == null || !mEmoticonGIFKeyboardFragment.handleBackPressed())
            super.onBackPressed();
    }
}
