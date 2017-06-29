package com.example.android.myapplication.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.myapplication.R;
import com.example.android.myapplication.model.Movie;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetailActivity extends AppCompatActivity {

    /**
     * Variable Declaration
     */

    private ImageView backdropImage;
    private TextView title;
    private ImageView posterImage;
    private TextView originalTitle;
    private TextView voteAverage;
    private TextView releasedDate;
    private TextView synopsis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        /**
         * Object reference with layout id
         */
        backdropImage = (ImageView) findViewById(R.id.tv_detail_backdrop_image);
        title = (TextView) findViewById(R.id.tv_detail_title);
        posterImage = (ImageView) findViewById(R.id.tv_detail_poster_image);
        originalTitle = (TextView) findViewById(R.id.tv_detail_original_title);
        voteAverage = (TextView) findViewById(R.id.tv_detail_vote_average);
        releasedDate = (TextView) findViewById(R.id.tv_detail_released_date);
        synopsis = (TextView) findViewById(R.id.tv_detail_synopsis);

        /**
         * getIntent from MainActivity
         */
        Bundle data = getIntent().getExtras();
        if ( data != null) {
            Movie movie = data.getParcelable("movie");
            Picasso.with(this).load(movie.getBackdropPath()).into(backdropImage);
            title.setText(movie.getTitle());
            Picasso.with(this).load(movie.getPosterPath()).into(posterImage);
            originalTitle.setText(movie.getOriginalTitle());
            voteAverage.setText(movie.getVoteAverage().toString() + " / 10 ");
            releasedDate.setText(getParsedDate(movie.getReleaseDate()));
            synopsis.setText(movie.getOverview());
            setTitle(movie.getTitle());
        } else {
            finish();
        }
    }

    /**
     * Make (up button) behaviour like (back button)
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * Parsed released DATE format
     * @param stringDate
     * @return
     */
    public String getParsedDate(String stringDate) {
        String parsedResult = null;
        DateFormat fromFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat toFormat = new SimpleDateFormat("MMM dd, yyyy");
        try {
            Date parseDate = fromFormat.parse(stringDate);
            parsedResult = toFormat.format(parseDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return parsedResult;
    }

}
