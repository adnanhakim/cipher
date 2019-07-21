package com.developer.cryptography.Ciphers;

import com.developer.cryptography.MainActivity;

public class Affine {

    public String encrypt(String plain, int mulKey, int addKey) {
        StringBuilder cipher = new StringBuilder();
        char[] plainArr = plain.toCharArray();
        for (int i = 0; i < plainArr.length; i++) {
            String letter = String.valueOf(plainArr[i]);
            if (!letter.equals(" ")) {
                int index = MainActivity.alphabetList.indexOf(letter);
                int newIndex = ((index * mulKey) + addKey) % 26;
                cipher.append(MainActivity.alphabetList.get(newIndex));
            } else cipher.append(" ");
        }
        return cipher.toString();
    }

    public String decrypt(String cipher, int mulKey, int addKey) {
        StringBuilder plain = new StringBuilder();
        char[] cipherArr = cipher.toCharArray();
        for (int i = 0; i < cipherArr.length; i++) {
            String letter = String.valueOf(cipherArr[i]);
            if (!letter.equals(" ")) {
                int index = MainActivity.alphabetList.indexOf(letter);
                int newIndex = ((index - addKey) * MainActivity.inverseMap.get(mulKey)) % 26;
                while (newIndex < 0) {
                    newIndex += 26;
                }
                plain.append(MainActivity.alphabetList.get(newIndex));
            } else plain.append(" ");
        }
        return plain.toString();
    }
}
