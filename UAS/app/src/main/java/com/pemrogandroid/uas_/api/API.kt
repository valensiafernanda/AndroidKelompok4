package com.pemrogandroid.uas_.api

import com.pemrogandroid.uas_.model.BookItem
import com.pemrogandroid.uas_.model.BooksResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface API {
    @GET("volumes")
    suspend fun searchBooks(
        @Query("q") query: String,
        @Query("maxResults") maxResults: Int,
        @Query("key") apiKey: String
    ): Response<BooksResponse>

    @GET("volumes/{id}")
    suspend fun getBookDetails(
        @Path("id") id: String,
        @Query("key") apiKey: String
    ): Response<BookItem>
}

