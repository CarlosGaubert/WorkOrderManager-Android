package com.kendito.ote.Personal_Organizador;

import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.kendito.ote.R;
import com.kendito.ote.databinding.ActivityVerOrdenesPersonalBinding;
import com.kendito.ote.verMisOrdenes.VerMisOrdenesFragment;

public class VerOrdenesPersonal extends AppCompatActivity {

    public static VerOrdenesFragment verOrdenesFragment;
    public static VerMisOrdenesFragment verMisOrdenesFragment;

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private ActivityVerOrdenesPersonalBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVerOrdenesPersonalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        binding.container.setAdapter(mSectionsPagerAdapter);

        binding.container.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(binding.tabs));
        binding.tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(binding.container));

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_ordenes_ycliente, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_refresh) {
            if (verMisOrdenesFragment != null) {
                verMisOrdenesFragment.obtenerOrdenes();
            }
            if (getCurrentFocus() != null) {
                Snackbar.make(getCurrentFocus(), Html.fromHtml("<font color=\"#ffffff\">Datos Actualizados</font>"), Snackbar.LENGTH_SHORT).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static class PlaceholderFragment extends Fragment {
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {}

        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            return inflater.inflate(R.layout.fragment_ver_ordenes_personal, container, false);
        }
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                verOrdenesFragment = new VerOrdenesFragment();
                return verOrdenesFragment;
            } else {
                verMisOrdenesFragment = new VerMisOrdenesFragment();
                return verMisOrdenesFragment;
            }
        }

        @Override
        public int getCount() {
            return 2;
        }
    }
}
