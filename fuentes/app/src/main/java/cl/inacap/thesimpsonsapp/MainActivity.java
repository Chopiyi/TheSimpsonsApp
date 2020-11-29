package cl.inacap.thesimpsonsapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import cl.inacap.thesimpsonsapp.adapters.QuotesListAdapter;
import cl.inacap.thesimpsonsapp.dto.Quote;

public class MainActivity extends AppCompatActivity {

    private Spinner spinner;
    private Button button_solicitar;
    private ListView lv_quotes;
    private QuotesListAdapter adapter;
    private RequestQueue queue;
    private List<Quote> quoteList = new ArrayList<>();
    private ArrayAdapter<CharSequence> spinnerAdapter;
    private ImageView imagen_toolbar;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.toolbar = findViewById(R.id.toolbar);
        this.setSupportActionBar((Toolbar) toolbar);
        this.imagen_toolbar = findViewById(R.id.imagen_toolbar);
        Picasso.get().get().load("https://upload.wikimedia.org/wikipedia/commons/thumb/4/44/Logo_The_Simpsons.svg/500px-Logo_The_Simpsons.svg.png")
                .resize(260, 121).into(this.imagen_toolbar);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.spinner = findViewById(R.id.spinner_frases);
        this.button_solicitar = findViewById(R.id.boton_solicitar);
        this.lv_quotes = findViewById(R.id.lista_quotes);
        this.spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.arrayCantidad, android.R.layout.simple_spinner_item);
        this.spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        this.spinner.setAdapter(spinnerAdapter);
        this.adapter = new QuotesListAdapter(this, R.layout.quotes_list, quoteList);
        this.lv_quotes.setAdapter(adapter);
        this.queue = Volley.newRequestQueue(this);
        this.button_solicitar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int cantidadSeleccionada = spinner.getSelectedItemPosition();
                if(cantidadSeleccionada != 0){
                    quoteList.clear();
                    JsonArrayRequest request = new JsonArrayRequest("https://thesimpsonsquoteapi.glitch.me/quotes?count=" + spinner.getSelectedItem().toString(), new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            try {
                                for (int i = 0; i < cantidadSeleccionada; i++){
                                    Quote quote = new Quote();
                                    quote.setQuote(response.getJSONObject(i).get("quote").toString());
                                    quote.setCharacter(response.getJSONObject(i).get("character").toString());
                                    quote.setImage(response.getJSONObject(i).get("image").toString());
                                    quoteList.add(quote);
                                }
                            } catch (JSONException ex){
                                quoteList = null;
                                System.out.println(ex);
                            } finally {
                                adapter.notifyDataSetChanged();
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            quoteList = null;
                            System.out.println(error);
                            adapter.notifyDataSetChanged();
                        }
                    });
                    queue.add(request);
                } else {
                    Toast.makeText(MainActivity.this, "Por favor seleccione una cantidad", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}