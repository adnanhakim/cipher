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
import com.developer.cryptography.MainActivity;
import com.developer.cryptography.R;
import com.google.android.material.textfield.TextInputLayout;

public class DecryptFragment extends Fragment {

    private FragmentDecryptListener listener;
    private View view;
    private TextInputLayout tilCipher, tilKey1, tilKey2;
    private Button btnDecrypt;
    private TextView tvPlain;
    private ImageButton ibCopy;

    private Context context;
    private String cipher;
    int key1, key2;

    public DecryptFragment(Context context) {
        this.context = context;
    }

    public interface FragmentDecryptListener {
        void addOnDecryptInputSentListener(String input, String key1, @Nullable String key2);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_decrypt, container, false);
        init();
        btnDecrypt.setOnClickListener(v -> {
            affine();
        });
        return view;
    }

    private void init() {
        tilCipher = view.findViewById(R.id.tilDecryptCipherText);
        tilKey1 = view.findViewById(R.id.tilDecryptKey1);
        tilKey2 = view.findViewById(R.id.tilDecryptKey2);
        btnDecrypt = view.findViewById(R.id.btnDecrypt);
        tvPlain = view.findViewById(R.id.tvDecryptPlain);
        ibCopy = view.findViewById(R.id.ibDecryptCopy);

        ibCopy.setOnClickListener(v -> {
            ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clipData = ClipData.newPlainText("PLAIN", tvPlain.getText().toString());
            clipboardManager.setPrimaryClip(clipData);
        });
    }

    private void getData() {
        cipher = tilCipher.getEditText().getText().toString().toUpperCase().trim();
        key1 = Integer.valueOf(tilKey1.getEditText().getText().toString().trim());
        key2 = Integer.valueOf(tilKey2.getEditText().getText().toString().trim());
    }

    private void affine() {
        getData();
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
            String plain = affine.decrypt(cipher, key1, key2);
            tvPlain.setText(plain);
            listener.addOnDecryptInputSentListener(plain, String.valueOf(key1), String.valueOf(key2));
        }
    }

    public void updateData(String input, String key1, String key2) {
        tilCipher.getEditText().setText(input);
        tilKey1.getEditText().setText(key1);
        tilKey2.getEditText().setText(key2);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof FragmentDecryptListener) {
            listener = (FragmentDecryptListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement FragmentDecryptListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
