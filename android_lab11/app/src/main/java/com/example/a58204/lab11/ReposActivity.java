package com.example.a58204.lab11;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.a58204.lab11.Service.GithubService;
import com.example.a58204.lab11.adapter.CardAdapter;
import com.example.a58204.lab11.model.Repos;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * Created by 58204 on 2017/12/23.
 */

public class ReposActivity extends AppCompatActivity {
    final private String target_url = "https://api.github.com/";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repos);

        final List<Repos> data = new ArrayList<>();

        final Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String userName = bundle.getString("name");

        final ProgressBar progressBar = (ProgressBar)findViewById(R.id.pro1);
        final RecyclerView mrecyclerview = (RecyclerView)findViewById(R.id.repos_list);
        final CardAdapter<Repos> cardAdapter = new CardAdapter<Repos>(this,R.layout.repos_item, data) {
            @Override
            public void convert(ViewHolder holder, Repos item) {
                TextView repos_name = holder.getView(R.id.repos_name);
                TextView language= holder.getView(R.id.repos_language);
                TextView description = holder.getView(R.id.repos_descrip);
                repos_name.setText((item.getName()==null) ? "" : item.getName().toString());
                language.setText((item.getLanguage()==null) ? "" : item.getLanguage().toString());
                description.setText((item.getDescription()==null) ? "" : item.getDescription().toString());
            }

        };
        mrecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mrecyclerview.setAdapter(cardAdapter);

        Retrofit retrofit = createRetrofit(target_url);
        GithubService service = retrofit.create(GithubService.class);
        service.getRepos(userName)
                .subscribeOn(Schedulers.newThread())
                .observeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<List<Repos>>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("完成传输");
                        mrecyclerview.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e("Github-Demo",e.getMessage());
                    }

                    @Override
                    public void onNext(List<Repos> reposes) {
                        cardAdapter.addListData(reposes);
                    }
                });
    }


    private static OkHttpClient createOkHttp()
    {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(30,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }


    private static  Retrofit createRetrofit(String baseUrl)
    {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(createOkHttp())
                .build();
    }
}
