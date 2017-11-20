package rodrigofagundes.br.newsfeed;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by rmfagundes on 14/11/2017.
 */

public class ArticleAdapter extends ArrayAdapter<Article> {

    public ArticleAdapter(@NonNull Context context, List<Article> articles) {
        super(context, 0, articles);
    }

    @Override
    public android.view.View getView(int position, View convertView, ViewGroup parent) {
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.article_list_item,
                    parent, false);
        }

        Article currentArticle = getItem(position);

        TextView titleView = (TextView)listItemView.findViewById(R.id.article_title);
        titleView.setText(currentArticle.getTitle());

        TextView authorView = (TextView)listItemView.findViewById(R.id.article_author);
        authorView.setText(currentArticle.getAuthor());

        TextView sectionView = (TextView)listItemView.findViewById(R.id.article_section);
        sectionView.setText(currentArticle.getSection());

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm");
        TextView publishedOnView = (TextView)listItemView.findViewById(R.id.article_date_published);
        publishedOnView.setText(formatter.format(currentArticle.getPublishedOn()));

        return listItemView;
    }
}
