package com.dw.exercise.manager;

import com.dw.exercise.model.dto.MiniAppOpenidSession;
import com.dw.util.HttpRequest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotNull;
import java.net.MalformedURLException;

@Service
public class MiniAppManager {
    @Value("${miniapp.appid}")
    private String miniAppId;
    @Value("${miniapp.secret}")
    private String miniAppSecret;
    public MiniAppOpenidSession code2Session(@NotNull String code){
        String url = "https://api.weixin.qq.com/sns/jscode2session?appid=APPID&secret=SECRET&js_code=JSCODE&grant_type=authorization_code";
        url = url.replace("APPID", miniAppId);
        url = url.replace("SECRET", miniAppSecret);
        url = url.replace("JSCODE", code);

        String response = HttpRequest.get(url);
        Gson gson = new Gson();
        MiniAppOpenidSession result = gson.fromJson(response, MiniAppOpenidSession.class);

        return result;
    }
}
