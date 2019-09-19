package space.rezanov.converter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import space.rezanov.converter.FragmentCalculate;
import space.rezanov.converter.History;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private AppBarLayout appBarLayout;
    private ViewPager viewPager;

    Elements Currency;
    String currency_string;
    static double currency_double;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CalculateCurrency().execute();

        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBar);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.AddFragment(new FragmentCalculate(),"Расчеты");
        adapter.AddFragment(new History(),"История");
        viewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);




    }

    public class CalculateCurrency extends AsyncTask<Void, Void, Double>{
        @Override
        protected Double doInBackground(Void... voids) {
            Document siteConvert;
            try {
                siteConvert = Jsoup.connect("https://www.cbr.ru/scripts/XML_daily.asp").get();
                Currency = siteConvert.select("Valute#R01670>Value");
                currency_string = Currency.text().replaceAll(",",".");
                currency_double = Double.valueOf(currency_string)/10;
                return currency_double;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Double value_doInBackground) {
            FragmentCalculate.currency_text.setText((String.valueOf(value_doInBackground)));
        }
    }
}
