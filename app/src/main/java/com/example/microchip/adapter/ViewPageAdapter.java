package com.example.microchip.adapter;

import android.os.Bundle;
import android.util.SparseArray;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.microchip.fragment.StatisticFragment;
import com.example.microchip.fragment.CartFragment;
import com.example.microchip.fragment.InvoiceFragment;
import com.example.microchip.fragment.ProductFragment;
import com.example.microchip.fragment.ProfileFragment;

public class ViewPageAdapter extends FragmentStateAdapter {
    private final SparseArray<Fragment> registeredFragments = new SparseArray<>();
    public ViewPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }
    private final Bundle dataBundle = new Bundle();
    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment;
        if (position == 0) {
            fragment = new ProductFragment();
        } else if (position == 1) {
            fragment = new CartFragment();
            fragment.setArguments(dataBundle);
        } else if (position == 2) {
            fragment = new InvoiceFragment();
        }else if (position == 3) {
            fragment = new StatisticFragment();
        }else {
            fragment = new ProfileFragment();
        }
        return fragment;
    }


    @Override
    public int getItemCount() {
        return 5;
    }
}
