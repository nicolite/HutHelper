package cn.nicolite.huthelper.network;

import cn.nicolite.huthelper.network.api.CareerTalkAPI;
import cn.nicolite.huthelper.network.api.ElectricAPI;
import cn.nicolite.huthelper.network.api.ExamAPI;
import cn.nicolite.huthelper.network.api.ExpLessonAPI;
import cn.nicolite.huthelper.network.api.FeedBackAPI;
import cn.nicolite.huthelper.network.api.GradeAPI;
import cn.nicolite.huthelper.network.api.LoginAPI;
import cn.nicolite.huthelper.network.api.LostAndFoundAPI;
import cn.nicolite.huthelper.network.api.MarketAPI;
import cn.nicolite.huthelper.network.api.MessageAPI;
import cn.nicolite.huthelper.network.api.SayAPI;
import cn.nicolite.huthelper.network.api.SyllabusAPI;
import cn.nicolite.huthelper.network.api.TimeAxisAPI;
import cn.nicolite.huthelper.network.api.UpdateAPI;
import cn.nicolite.huthelper.network.api.UploadAPI;
import cn.nicolite.huthelper.network.api.UserAPI;
import cn.nicolite.huthelper.network.api.VideoAPI;
import cn.nicolite.huthelper.network.api.VoteAPI;
import cn.nicolite.huthelper.network.api.WeatherAPI;
import cn.nicolite.huthelper.network.retrofit.RetrofitUtils;
import retrofit2.Retrofit;

/**
 * 接口工具类
 * Created by nicolite on 17-10-14.
 */

public class APIUtils {

    private static LoginAPI loginAPI;
    private static MarketAPI marketAPI;
    private static SayAPI sayAPI;
    private static WeatherAPI weatherAPI;
    private static TimeAxisAPI dateLineAPI;

    private static Retrofit retrofit = RetrofitUtils.getInstance().retrofit();
    private static UpdateAPI updateAPI;
    private static FeedBackAPI feedBackAPI;
    private static MessageAPI messageAPI;
    private static UserAPI userAPI;
    private static UploadAPI uploadAPI;
    private static ElectricAPI electricAPI;
    private static VoteAPI voteAPI;
    private static ExamAPI examAPI;
    private static ExpLessonAPI expLessonAPI;
    private static CareerTalkAPI careerTalkAPI;
    private static LostAndFoundAPI lostAndFoundAPI;
    private static GradeAPI gradeAPI;
    private static VideoAPI videoAPI;
    private static SyllabusAPI syllabusAPI;

    public static LoginAPI getLoginAPI() {

        if (loginAPI == null) {
            loginAPI = retrofit.create(LoginAPI.class);
        }
        return loginAPI;
    }

    public static MarketAPI getMarketAPI() {
        if (marketAPI == null) {
            marketAPI = retrofit.create(MarketAPI.class);
        }
        return marketAPI;
    }

    public static SayAPI getSayAPI() {
        if (sayAPI == null) {
            sayAPI = retrofit.create(SayAPI.class);
        }
        return sayAPI;
    }

    public static WeatherAPI getWeatherAPI() {
        if (weatherAPI == null) {
            weatherAPI = retrofit.create(WeatherAPI.class);
        }
        return weatherAPI;
    }

    public static TimeAxisAPI getDateLineAPI() {
        if (dateLineAPI == null) {
            dateLineAPI = retrofit.create(TimeAxisAPI.class);
        }
        return dateLineAPI;
    }

    public static UpdateAPI getUpdateAPI() {
        if (updateAPI == null) {
            updateAPI = retrofit.create(UpdateAPI.class);
        }
        return updateAPI;
    }

    public static FeedBackAPI getFeedBackAPI() {
        if (feedBackAPI == null) {
            feedBackAPI = retrofit.create(FeedBackAPI.class);
        }
        return feedBackAPI;
    }

    public static MessageAPI getMessageAPI() {
        if (messageAPI == null) {
            messageAPI = retrofit.create(MessageAPI.class);
        }
        return messageAPI;
    }

    public static UserAPI getUserAPI() {
        if (userAPI == null) {
            userAPI = retrofit.create(UserAPI.class);
        }
        return userAPI;
    }

    public static UploadAPI getUploadAPI() {
        if (uploadAPI == null) {
            uploadAPI = retrofit.create(UploadAPI.class);
        }
        return uploadAPI;
    }

    public static ElectricAPI getElectricAPI() {
        if (electricAPI == null) {
            electricAPI = retrofit.create(ElectricAPI.class);
        }
        return electricAPI;
    }

    public static VoteAPI getVoteAPI() {
        if (voteAPI == null) {
            voteAPI = retrofit.create(VoteAPI.class);
        }
        return voteAPI;
    }

    public static ExamAPI getExamAPI() {
        if (examAPI == null) {
            examAPI = retrofit.create(ExamAPI.class);
        }
        return examAPI;
    }

    public static ExpLessonAPI getExpLessonAPI() {
        if (expLessonAPI == null) {
            expLessonAPI = retrofit.create(ExpLessonAPI.class);
        }
        return expLessonAPI;
    }

    public static CareerTalkAPI getCareerTalkAPI() {
        if (careerTalkAPI == null) {
            careerTalkAPI = retrofit.create(CareerTalkAPI.class);
        }
        return careerTalkAPI;
    }

    public static LostAndFoundAPI getLostAndFoundAPI() {
        if (lostAndFoundAPI == null) {
            lostAndFoundAPI = retrofit.create(LostAndFoundAPI.class);
        }
        return lostAndFoundAPI;
    }

    public static GradeAPI getGradeAPI() {
        if (gradeAPI == null) {
            gradeAPI = retrofit.create(GradeAPI.class);
        }
        return gradeAPI;
    }

    public static VideoAPI getVideoAPI() {
        if (videoAPI == null) {
            videoAPI = retrofit.create(VideoAPI.class);
        }
        return videoAPI;
    }

    public static SyllabusAPI getSyllabusAPI() {
        if (syllabusAPI == null) {
            syllabusAPI = retrofit.create(SyllabusAPI.class);
        }
        return syllabusAPI;
    }
}
