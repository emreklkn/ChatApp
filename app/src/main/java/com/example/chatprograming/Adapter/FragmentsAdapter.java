package com.example.chatprograming.Adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.chatprograming.Fragments.CallsFragment;
import com.example.chatprograming.Fragments.ChatsFragment;
import com.example.chatprograming.Fragments.StatusFragment;

public class FragmentsAdapter extends FragmentPagerAdapter {

    public FragmentsAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) //* fragmentin getireceği durumlar fragment dediğimiz sohbet arama ve durumlar sekmesi
        {
            case 0: return new ChatsFragment();
            case 1: return new StatusFragment();
            case 2: return new CallsFragment();
            default: return new ChatsFragment();



        }
    }

    @Override
    public int getCount() {
        return 3;
    } //* 3 adet fragment döndürüyor

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) { //* sayfaların ismini başlığını getiti
        String title = null;
        if(position ==0)
        {
            title ="Sohbetler";
        }
        if(position ==1)
        {
            title ="Durumlar";
        }
        if(position ==2)
        {
            title ="Aramalar";
        }



        return title;
    }
}
