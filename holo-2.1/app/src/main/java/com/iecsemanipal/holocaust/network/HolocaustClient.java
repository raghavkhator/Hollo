package com.iecsemanipal.holocaust.network;

import com.iecsemanipal.holocaust.models.AttendanceRequest;
import com.iecsemanipal.holocaust.models.EventCreationRequest;
import com.iecsemanipal.holocaust.models.GetEventsRequest;
import com.iecsemanipal.holocaust.models.GetEventsResponse;
import com.iecsemanipal.holocaust.models.RegistrationRequest;
import com.iecsemanipal.holocaust.models.RegistrationResponse;
import com.iecsemanipal.holocaust.models.ServerResponse;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * Created by anurag on 21/8/17.
 *
 */
public class HolocaustClient {
    private static Retrofit retrofit = null;
    private static final String BASE_URL = "https://api.iecsemanipal.com/";

    public static APIInterface getHolocaustInterface(){
        if (retrofit == null){
            retrofit = new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create()).build();
        }
        return retrofit.create(APIInterface.class);
    }


    public interface APIInterface{
        @POST("member/insert")
        Call<RegistrationResponse> createMember(@Body RegistrationRequest request);

        @POST("occurrences/create")
        Call<ServerResponse> createEvent(@Body EventCreationRequest request);

        @POST("occurrences/getAll")
        Call<GetEventsResponse> getEvents(@Body GetEventsRequest request);

        @POST("attendance/add")
        Call<ServerResponse> markAttendance(@Body AttendanceRequest request);
    }
}
