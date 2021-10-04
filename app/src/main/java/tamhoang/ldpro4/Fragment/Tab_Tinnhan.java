package tamhoang.ldpro4.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.TabHost;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import tamhoang.ldpro4.MyFragmentPagerAdapter;
import tamhoang.ldpro4.R;

public class Tab_Tinnhan extends Fragment implements TabHost.OnTabChangeListener, ViewPager.OnPageChangeListener {
    int i = 0;
    private TabHost tabHost;
    View v;
    private ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        this.v = inflater.inflate(R.layout.frag_mo_report, container, false);
        initializeTabHost(savedInstanceState);
        initializeViewPager();
        this.tabHost.setCurrentTab(0);
        return this.v;
    }

    private void initializeViewPager() {
        ArrayList<Fragment> fragments = new ArrayList<>();
        fragments.add(new Frag_Suatin());
        fragments.add(new Frag_NoRP3());
        MyFragmentPagerAdapter myViewpagerAdapter = new MyFragmentPagerAdapter(getChildFragmentManager(), fragments);
        ViewPager viewPager2 = (ViewPager) this.v.findViewById(R.id.viewPager);
        this.viewPager = viewPager2;
        viewPager2.setAdapter(myViewpagerAdapter);
        this.viewPager.setOnPageChangeListener(this);
    }

    private void initializeTabHost(Bundle args) {
        TabHost tabHost2 = (TabHost) this.v.findViewById(R.id.tabhost);
        this.tabHost = tabHost2;
        tabHost2.setup();
        TabHost.TabSpec tabSpec1 = this.tabHost.newTabSpec("Sửa tin");
        tabSpec1.setIndicator("Sửa tin");
        tabSpec1.setContent(new FakeContent(getActivity()));
        TabHost.TabSpec tabSpec2 = this.tabHost.newTabSpec("Tin nhắn");
        tabSpec2.setIndicator("Tin chi tiết");
        tabSpec2.setContent(new FakeContent(getActivity()));
        this.tabHost.addTab(tabSpec1);
        this.tabHost.addTab(tabSpec2);
        this.tabHost.setOnTabChangedListener(this);
    }

    public void onTabChanged(String tabId) {
        this.viewPager.setCurrentItem(this.tabHost.getCurrentTab());
        HorizontalScrollView hScrollView = (HorizontalScrollView) this.v.findViewById(R.id.hScrollView);
        View tabView = this.tabHost.getCurrentTabView();
        hScrollView.smoothScrollTo(tabView.getLeft() - ((hScrollView.getWidth() - tabView.getWidth()) / 2), 0);
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrollStateChanged(int arg0) {
    }

    @Override // android.support.v4.view.ViewPager.OnPageChangeListener
    public void onPageScrolled(int arg0, float arg1, int arg2) {
    }

    @Override
    public void onPageSelected(int position) {
        this.tabHost.setCurrentTab(position);
    }

    /* access modifiers changed from: package-private */
    public class FakeContent implements TabHost.TabContentFactory {
        private final Context mContext;

        public FakeContent(Context context) {
            this.mContext = context;
        }

        public View createTabContent(String tag) {
            View v = new View(this.mContext);
            v.setMinimumHeight(0);
            v.setMinimumWidth(0);
            return v;
        }
    }
}