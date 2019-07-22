package com.developer.cryptography.Ciphers;

import com.developer.cryptography.MainActivity;

public class AutoKey {

    public String encrypt(String plain, int primaryKey) {
        int dynamicKey = primaryKey;
        char[] plainArr = plain.toCharArray();
        StringBuilder cipher = new StringBuilder();
        for(int i = 0; i < plain.length(); i++) {
            String text = String.valueOf(plainArr[i]);
            if(!text.equals(" ")) {
                cipher = cipher.append(MainActivity.alphabetList.get((MainActivity.alphabetList.indexOf(String.valueOf(plainArr[i])) + dynamicKey) % 26));
                dynamicKey = MainActivity.alphabetList.indexOf(String.valueOf(plainArr[i]));
            }
            else
                cipher = cipher.append(" ");
        }
        return cipher.toString();
    }

    public String decrypt(String cipher, int primaryKey) {
        StringBuilder plain = new StringBuilder();
        char[] cipherArr = cipher.toCharArray();
        int dynamicKey = primaryKey, index;
        for(int i = 0; i < cipher.length(); i++) {
            String text = String.valueOf(cipherArr[i]);
            if(!text.equals(" ")) {
                index = MainActivity.alphabetList.indexOf(String.valueOf(cipherArr[i])) - dynamicKey;
                dynamicKey = index;
                while (index < 0)
                    index += 26;
                plain = plain.append(MainActivity.alphabetList.get(index % 26));
            }
            else
                plain = plain.append(" ");

        }
        return plain.toString();
    }

}
