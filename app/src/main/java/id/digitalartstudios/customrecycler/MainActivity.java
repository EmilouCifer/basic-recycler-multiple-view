package id.digitalartstudios.customrecycler;

import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import id.digitalartstudios.customrecycler.adapter.NewsAdapterMultipleView;
import id.digitalartstudios.customrecycler.config.AppConfig;
import id.digitalartstudios.customrecycler.helper.RecyclerItemClickListener;
import id.digitalartstudios.customrecycler.model.NewsItem;
import id.digitalartstudios.customrecycler.volley.AppController;

public class MainActivity extends AppCompatActivity {

    private final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefresh;
    private List<NewsItem> newsItemList = new ArrayList<>();
    private NewsAdapterMultipleView adapter;
    private GridLayoutManager layoutManager;

    private int halaman = 1;
    private Boolean loadMore = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        swipeRefresh = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);

        adapter = new NewsAdapterMultipleView(this, newsItemList);
        layoutManager = new GridLayoutManager(this, 2);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch (adapter.getItemViewType(position)) {
                    case NewsAdapterMultipleView.TYPE_TITLE:
                        return 2;
                    case NewsAdapterMultipleView.TYPE_HEADER:
                        return 2;
                    case NewsAdapterMultipleView.TYPE_GRID:
                        return 1;
                    case NewsAdapterMultipleView.TYPE_LINEAR:
                        return 2;
                    default:
                        return 2;
                }
            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this, recyclerView, new RecyclerItemClickListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                NewsItem newsItem = newsItemList.get(position);
                Toast.makeText(getBaseContext(), newsItem.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                GridLayoutManager layoutMan = (GridLayoutManager) recyclerView.getLayoutManager();
                int itemCount = layoutMan.getItemCount();
                int lastItemPosition = layoutMan.findLastVisibleItemPosition();
                if (lastItemPosition >= (itemCount - 1)) {
                    if (loadMore) {
                        loadMore = false;
                        newsRequest();
                    }
                }
            }
        });

        swipeRefresh.setColorSchemeResources(R.color.amberStone, R.color.breathFire, R.color.blueSky);
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                halaman = 1;
                newsRequest();
            }
        });

        newsRequest();
    }

    public void newsRequest() {
        swipeRefresh.setRefreshing(true);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, AppConfig.ALL_NEWS, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, response.toString());
                try {
                    String status = response.getString("status");
                    if (status.equals("online")) {
                        if (loadMore)
                            newsItemList.clear();
                        NewsItem titleKategori = new NewsItem();
                        titleKategori.setTitle(String.format("Contoh Title ke-%s", String.valueOf(halaman)));
                        titleKategori.setLayoutType(NewsAdapterMultipleView.TYPE_TITLE);
                        newsItemList.add(titleKategori);

                        String title, content, time, source, thumb, sourceImages;
                        JSONArray jsonArray = response.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject news = (JSONObject) jsonArray.get(i);
                            title = news.getString("judul");
                            content = news.getString("konten");
                            time = news.getString("tanggal");
                            source = news.getString("url");
                            thumb = news.getString("namagambar");
                            sourceImages = news.getString("urlimage");

                            NewsItem newsItem = new NewsItem(title, content, time, source, thumb, sourceImages);
                            if (halaman == 1) {
                                if (i == 0)
                                    newsItem.setLayoutType(NewsAdapterMultipleView.TYPE_HEADER);
                                else if (i > 0 && i < 5)
                                    newsItem.setLayoutType(NewsAdapterMultipleView.TYPE_GRID);
                                else
                                    newsItem.setLayoutType(NewsAdapterMultipleView.TYPE_LINEAR);
                            } else {
                                if (i < 4)
                                    newsItem.setLayoutType(NewsAdapterMultipleView.TYPE_GRID);
                                else
                                    newsItem.setLayoutType(NewsAdapterMultipleView.TYPE_LINEAR);
                            }
                            newsItemList.add(newsItem);
                        }
                        adapter.notifyDataSetChanged();
                        halaman++;
                    } else {
                        Toast.makeText(getBaseContext(), "Batas berita ditemukan.", Toast.LENGTH_SHORT).show();
                    }
                    loadMore = true;
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(TAG, e.getMessage());
                    Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                swipeRefresh.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, error.getMessage());
                Toast.makeText(getBaseContext(), "Harap periksa konektifitas Anda.", Toast.LENGTH_SHORT).show();
                swipeRefresh.setRefreshing(false);
            }
        }) {
            @Override
            public byte[] getBody() {
                String postParameters = String.format("halaman=%s&kategori=%s", String.valueOf(halaman), AppConfig.BISNIS);
                return postParameters.getBytes();
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/x-www-form-urlencoded");

                return headers;
            }
        };

        AppController.getInstance().addToRequestQueue(request, TAG);
    }
}
