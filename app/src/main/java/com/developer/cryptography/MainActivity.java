package com.developer.cryptography;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.util.SparseIntArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.developer.cryptography.Adapters.ViewPagerAdapter;
import com.developer.cryptography.Fragments.DecryptFragment;
import com.developer.cryptography.Fragments.EncryptFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements EncryptFragment.FragmentEncryptListener, DecryptFragment.FragmentDecryptListener {

    // UI Elements
    private Button btnEncrypt, btnDecrypt;
    private ImageView ivEncryptDash, ivDecryptDash;
    private ViewPager viewPager;

    private EncryptFragment encryptFragment;
    private DecryptFragment decryptFragment;

    public static List<String> alphabetList;
    public static List<Integer> primeList;
    public static SparseIntArray inverseMap;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        setUp();
        setUpFragments();
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbarMain);
        btnEncrypt = findViewById(R.id.btnMainEncrypt);
        btnDecrypt = findViewById(R.id.btnMainDecrypt);
        ivEncryptDash = findViewById(R.id.ivMainEncryptDash);
        ivDecryptDash = findViewById(R.id.ivMainDecryptDash);
        viewPager = findViewById(R.id.viewPagerMain);

        setSupportActionBar(toolbar);
    }

    private void setUp() {
        alphabetList = new ArrayList<>();
        for (int i = 'A'; i <= 'Z'; i++) {
            alphabetList.add(String.valueOf((char) i));
        }

        primeList = new ArrayList<>();
        for (int i = 1; i < 26; i++) {
            if (gcd(i, 26) == 1) {
                primeList.add(i);
            }
        }

        inverseMap = new SparseIntArray();
        for (int i = 0; i < primeList.size(); i++) {
            int key = primeList.get(i);
            for (int j = 0; j < primeList.size(); j++) {
                int value = primeList.get(j);
                if (((key * value) % 26) == 1) {
                    inverseMap.put(key, value);
                    break;
                }
            }
        }

        Log.d(TAG, "setUp: HashMap: " + inverseMap);
    }

    public static int gcd(int num1, int num2) {
        while (num1 != num2) {
            if (num1 > num2) {
                num1 -= num2;
            } else {
                num2 -= num1;
            }
        }
        return num1;
    }

    private void setUpFragments() {
        encryptFragment = new EncryptFragment(this);
        decryptFragment = new DecryptFragment(this);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(encryptFragment);
        adapter.addFragment(decryptFragment);
        viewPager.setAdapter(adapter);

        selectEncrypt();
        viewPager.setCurrentItem(0);
        setUpTabs();

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0)
                    selectEncrypt();
                else
                    selectDecrypt();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void setUpTabs() {
        btnEncrypt.setOnClickListener(v -> {
            selectEncrypt();
            viewPager.setCurrentItem(0);
        });

        btnDecrypt.setOnClickListener(v -> {
            selectDecrypt();
            viewPager.setCurrentItem(1);
        });
    }

    private void selectEncrypt() {
        ivEncryptDash.setVisibility(View.VISIBLE);
        ivDecryptDash.setVisibility(View.INVISIBLE);
    }

    private void selectDecrypt() {
        ivEncryptDash.setVisibility(View.INVISIBLE);
        ivDecryptDash.setVisibility(View.VISIBLE);
    }

    @Override
    public void addOnEncryptInputSentListener(String input, String key1, @Nullable String key2) {
        decryptFragment.updateData(input, key1, key2);
    }

    @Override
    public void addOnDecryptInputSentListener(String input, String key1, @Nullable String key2) {
        encryptFragment.updateData(input, key1, key2);
    }
}
