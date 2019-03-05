package com.example.a58204.lab11.Service;
import com.example.a58204.lab11.model.Github;
import com.example.a58204.lab11.model.Repos;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * Created by 58204 on 2017/12/23.
 */
public interface GithubService {
    @GET("users/{user}")
    Observable<Github> getUser(@Path("user") String user);

    @GET("users/{user}/repos")
    Observable<List<Repos>> getRepos(@Path("user") String user);
}
