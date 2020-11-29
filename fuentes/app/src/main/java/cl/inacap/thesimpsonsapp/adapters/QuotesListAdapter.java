package cl.inacap.thesimpsonsapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

import cl.inacap.thesimpsonsapp.R;
import cl.inacap.thesimpsonsapp.dto.Quote;

public class QuotesListAdapter extends ArrayAdapter<Quote> {

    private Activity activity;
    private List<Quote> quotesList;

    public QuotesListAdapter(@NonNull Activity context, int resource, @NonNull List<Quote> objects) {
        super(context, resource, objects);
        this.activity = context;
        this.quotesList = objects;

    }

    @Nullable
    @Override
    public Quote getItem(int position) {
        return quotesList.get(position);
    }

    @Override
    public int getCount() {
        return quotesList.size();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = this.activity.getLayoutInflater();
        View view = inflater.inflate(R.layout.quotes_list, null, true);
        TextView character = view.findViewById(R.id.character_quote);
        TextView quote = view.findViewById(R.id.frase_quote);
        ImageView image = view.findViewById(R.id.imagen_quote);
        Quote frase = this.quotesList.get(position);
        character.setText(frase.getCharacter());
        quote.setText(frase.getQuote());
        Picasso.get().load(frase.getImage()).resize(256, 256).centerCrop().into(image);
        return view;
    }
}
