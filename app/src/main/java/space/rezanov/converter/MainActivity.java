package space.rezanov.converter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView first_input;//создаем экземпляр объекта представления
    TextView second_input;

    double first_input_double;
    String first_input_string;

    double second_input_double;
    String second_input_string;

    Elements Currency;
    String currency_string;
    double currency_double;

    TextView currency_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new CalculateCurrency().execute();

        first_input = findViewById(R.id.firstInput);  //и выполняем его захват из макета
        second_input = findViewById(R.id.secondInput);
        currency_text = findViewById(R.id.currencyText);

        first_input.addTextChangedListener(new TextWatcher(){ //слушает изменения в поле ввода №1
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable s){
                first_input_string = first_input.getText().toString();
                if(!first_input_string.isEmpty()) {
                    try {
                        second_input_double = Double.valueOf(first_input_string);
                        second_input.setText(String.valueOf(second_input_double / currency_double));
                    } catch (Exception e1) {
                    }
                } else second_input.setText("");
            }
        });


//
//        second_input.addTextChangedListener(new TextWatcher(){ //слушает изменения в поле ввода №2
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//            }
//
//            @Override
//            public void afterTextChanged(Editable s){
//                second_input_string = second_input.getText().toString();
//                    first_input_double = Double.valueOf(second_input_string) * currency_double;
//                    first_input.setText("first_input_double");
//            }
//        });


    }

    public class CalculateCurrency extends AsyncTask<String, Void, String>{
        @Override
        protected String doInBackground(String... strings) {
            Document siteConvert;
            try {
                siteConvert = Jsoup.connect("https://www.cbr.ru/scripts/XML_daily.asp").get();
                Currency = siteConvert.select("Valute#R01670>Value");
                currency_string = Currency.text().replaceAll(",",".");
                currency_double = Double.valueOf(currency_string)/10;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            currency_text.setText((String.valueOf(currency_double)));
        }
    }

}
