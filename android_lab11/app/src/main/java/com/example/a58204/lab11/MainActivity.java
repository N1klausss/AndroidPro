package com.example.a58204.lab11;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a58204.lab11.Service.GithubService;
import com.example.a58204.lab11.adapter.CardAdapter;
import com.example.a58204.lab11.model.Github;

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

public class MainActivity extends AppCompatActivity {

    final private String target_url = "https://api.github.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final EditText username = (EditText) findViewById(R.id.user_name);
        Button clear = (Button) findViewById(R.id.clear);
        Button fetch = (Button) findViewById(R.id.fetch);
        final ProgressBar progressBar = (ProgressBar) findViewById(R.id.pro);
        final RecyclerView mRecycleView = (RecyclerView) findViewById(R.id.mrecyclerview);


        final List<Github> data = new ArrayList<>();
        final CardAdapter<Github> cardAdapter = new CardAdapter<Github>(this,R.layout.recycler_item, data) {
            @Override
            public void convert(ViewHolder holder, Github item) {
                TextView userName = holder.getView(R.id.user);
                TextView id = holder.getView(R.id.id);
                TextView blog = holder.getView(R.id.blog);
                userName.setText(item.getLogin().toString());
                id.setText(item.getId().toString());
                blog.setText(item.getBlog().toString());
            }
        };
        mRecycleView.setLayoutManager(new LinearLayoutManager(this));
        mRecycleView.setAdapter(cardAdapter);
        cardAdapter.setOnItemClickListener(new CardAdapter.OnItemClickListener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(MainActivity.this,ReposActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("name",username.getText().toString().toLowerCase());
                intent.putExtras(bundle);
                startActivity(intent);
            }

            @Override
            public void onLongClick(int position) {
                cardAdapter.removeItem(position);
                Toast.makeText(MainActivity.this,"删除成功！",Toast.LENGTH_SHORT).show();
            }
        });



        Retrofit retrofit = createRetrofit(target_url);
        final GithubService service = retrofit.create(GithubService.class);
        fetch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);
                service.getUser(username.getText().toString().toLowerCase())
                        .subscribeOn(Schedulers.newThread())
                        .observeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Subscriber<Github>() {
                            @Override
                            public void onCompleted() {
                                System.out.println("完成传输");
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onError(Throwable e) {
                                Toast.makeText(MainActivity.this,e.hashCode()+"请确认您搜索的用户存在",Toast.LENGTH_SHORT).show();
                                Log.e("Github-Demo",e.getMessage());
                                progressBar.setVisibility(View.GONE);
                            }

                            @Override
                            public void onNext(Github github) {
                                cardAdapter.addData(github);
                            }
                        });
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username.setText("");
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
