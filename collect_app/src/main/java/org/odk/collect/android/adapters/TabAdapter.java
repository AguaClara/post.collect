package org.odk.collect.android.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.odk.collect.android.fragments.QRScannerFragment;
import org.odk.collect.android.fragments.ShowQRCodeFragment;

public class TabAdapter extends FragmentStateAdapter {
    public TabAdapter(FragmentActivity fa) {
        super(fa);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new QRScannerFragment();
            case 1:
                return new ShowQRCodeFragment();
            default:
                // should never reach here
                return null;
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}