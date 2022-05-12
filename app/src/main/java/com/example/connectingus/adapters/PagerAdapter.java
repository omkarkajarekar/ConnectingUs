package com.example.connectingus.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.connectingus.fragments.CameraFragment;
import com.example.connectingus.fragments.ChatsFragment;

public class PagerAdapter extends FragmentStateAdapter {
    public PagerAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0:
                return new CameraFragment();
            default:
                return new ChatsFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
