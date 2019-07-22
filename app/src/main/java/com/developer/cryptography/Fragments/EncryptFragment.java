package com.developer.cryptography.Fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.developer.cryptography.Ciphers.Affine;
import com.developer.cryptography.Ciphers.AutoKey;
import com.developer.cryptography.MainActivity;
import com.developer.cryptography.R;
import com.google.android.material.textfield.TextInputLayout;

public class EncryptFragment extends Fragment {

    private FragmentEncryptListener listener;
    private View view;
    private TextInputLayout tilPlain, tilKey1, tilKey2;
    private Button btnEncrypt;
    private TextView tvCipher;
    private ImageButton ibCopy;

    private Context context;
    private String plain;
    int key1, key2;

    public EncryptFragment(Context context) {
        this.context = context;
    }

    public interface FragmentEncryptListener {
        void addOnEncryptInputSentListener(String input, String key1, @Nullable String key2);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_encrypt, container, false);
        init();
        btnEncrypt.setOnClickListener(v -> {
            autoKey();
        });
        return view;
    }

    private void init() {
        tilPlain = view.findViewById(R.id.tilEncryptPlainText);
        tilKey1 = view.findViewById(R.id.tilEncryptKey1);
        tilKey2 = view.findViewById(R.id.tilEncryptKey2);
        btnEncrypt = view.findViewById(R.id.btnEncrypt);
        tvCipher = view.findViewById(R.id.tvEncryptCipher);
        ibCopy = view.findViewById(R.id.ibEncryptCopy);

        ibCopy.setOnClickListener(v -> {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("CIPHER", tvCipher.getText().toString());
            clipboardManager.setPrimaryClip(clipData);
        });
    }

    //Affine Cipher
    private void getAffineData() {
        plain = tilPlain.getEditText().getText().toString().toUpperCase().trim();
        key1 = Integer.valueOf(tilKey1.getEditText().getText().toString().trim());
        key2 = Integer.valueOf(tilKey2.getEditText().getText().toString().trim());
    }

    private void affine() {
        getAffineData();
        boolean correct = true;
        if (!MainActivity.primeList.contains(key1)) {
            tilKey1.getEditText().setError("Key must be relatively prime to 26");
            correct = false;
        }
        if (key2 > 25 || key2 < 0) {
            tilKey2.getEditText().setError("Key must be between 0 and 25");
            correct = false;
        }
        if (correct) {
            Affine affine = new Affine();
            String cipher = affine.encrypt(plain, key1, key2);
            tvCipher.setText(cipher);
            listener.addOnEncryptInputSentListener(cipher, String.valueOf(key1), String.valueOf(key2));
        }
    }

    //AutoKey Cipher
    private void getAutoKeyData() {
        plain = tilPlain.getEditText().getText().toString().toUpperCase().trim();
        key1 = Integer.valueOf(tilKey1.getEditText().getText().toString().trim());
    }

    private void autoKey() {
        getAutoKeyData();
        boolean correct = true;
//        if (!MainActivity.primeList.contains(key1)) {
//            tilKey1.getEditText().setError("Key must be relatively prime to 26");
//            correct = false;
//        }
//        if (key2 > 25 || key2 < 0) {
//            tilKey2.getEditText().setError("Key must be between 0 and 25");
//            correct = false;
//        }
        if (correct) {
            AutoKey autoKey = new AutoKey();
            String cipher = autoKey.encrypt(plain, key1);
            tvCipher.setText(cipher);
            listener.addOnEncryptInputSentListener(cipher, String.valueOf(key1), String.valueOf(""));
        }
    }

    public void updateData(String input, String key1, String key2) {
        tilPlain.getEditText().setText(input);
        tilKey1.getEditText().setText(key1);
        tilKey2.getEditText().setText(key2);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentEncryptListener) {
            listener = (FragmentEncryptListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentEncryptListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
