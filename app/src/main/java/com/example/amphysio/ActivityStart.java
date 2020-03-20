package com.example.amphysio;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import java.util.ArrayList;
import java.util.List;

public class ActivityStart extends AppCompatActivity {
  private int[] tabIcons = {R.drawable.nurse, R.drawable.hospital};
  private TabLayout tabLayout;
  private ViewPager viewPager;

  class ViewPagerAdapter extends FragmentPagerAdapter {
    private final List<Fragment> mFragmentList = new ArrayList();
    private final List<String> mFragmentTitleList = new ArrayList();

    public ViewPagerAdapter(FragmentManager manager) {
      super(manager);
    }

    public Fragment getItem(int position) {
      return (Fragment) this.mFragmentList.get(position);
    }

    public int getCount() {
      return this.mFragmentList.size();
    }

    public void addFragment(Fragment fragment, String title) {
      this.mFragmentList.add(fragment);
      this.mFragmentTitleList.add(title);
    }

    public CharSequence getPageTitle(int position) {
      return (CharSequence) this.mFragmentTitleList.get(position);
    }
  }

  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.start_layout);

    ImageButton button = (ImageButton)
            findViewById(R.id.user_profile_photo);

    this.viewPager = (ViewPager) findViewById(R.id.pager);
    setupViewPager(this.viewPager);
    this.tabLayout = (TabLayout) findViewById(R.id.tablayout);
    this.tabLayout.setupWithViewPager(this.viewPager);
    setupTabIcons();
  }

  private void setupViewPager(ViewPager viewPager2) {
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
    adapter.addFragment(new DoctorLoginFrag(), "Medecin");
    adapter.addFragment(new PatientLoginFrag(), "Patient");
    viewPager2.setAdapter(adapter);
  }

  private void setupTabIcons() {
    this.tabLayout.getTabAt(0).setIcon(this.tabIcons[0]);
    this.tabLayout.getTabAt(1).setIcon(this.tabIcons[1]);
  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
  }
}