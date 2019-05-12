package app.psychic.network;


import java.util.List;

import app.psychic.models.Question;
import app.psychic.models.Response;
import app.psychic.models.Statistics;
import app.psychic.models.User;
import app.psychic.utils.Constants;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface NetworkService {

    @FormUrlEncoded
    @POST(Constants.REGISTER)
    Call<Response<User>> registerUser(@Field("email") String email, @Field("name") String name, @Field("phone") String phone, @Field("password") String password);

    @FormUrlEncoded
    @POST(Constants.LOGIN)
    Call<Response<User>> loginUser(@Field("email") String email, @Field("password") String password);

    @FormUrlEncoded
    @POST(Constants.UPDATE_SCORE)
    Call<Response> updateScore(@Field("email") String email, @Field("score") String score);

    @FormUrlEncoded
    @POST(Constants.PUT_STATISTICS)
    Call<Response> putStatistics(@Field("type") String type, @Field("score") String score);

    @GET(Constants.GET_QUESTIONS)
    Call<Response<List<Question>>> getQuestions(@Query("type") String type);

    @GET(Constants.GET_STATISTICS)
    Call<Response<List<Statistics>>> getStatistics();
}
