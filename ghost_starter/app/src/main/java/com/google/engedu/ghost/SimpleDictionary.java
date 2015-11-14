package com.google.engedu.ghost;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix)
    {
        if(prefix.isEmpty())
        {
            Random R = new Random();
            int ind = R.nextInt(words.size());
            return words.get(ind);
        }
        else
        return binarySearch(prefix,0,words.size());

    }

    private String binarySearch(String prefix, int l, int r) {
        int mid = (l+r)/2;
        boolean exists = words.get(mid).startsWith(prefix);
        if(l == r && !exists)
            return null;
        if(exists)
            return words.get(mid);
        else
        {
             String left = binarySearch(prefix,l,mid-1);
             if(left != null)
                 return left;
             String right = binarySearch(prefix,mid+1,r);
            return right;

        }
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        return null;
    }
}
