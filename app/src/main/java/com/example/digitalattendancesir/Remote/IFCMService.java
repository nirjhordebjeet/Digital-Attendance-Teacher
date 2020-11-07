package com.example.digitalattendancesir.Remote;

import com.example.digitalattendancesir.Model.DataMessage;
import com.example.digitalattendancesir.Model.FCMResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface IFCMService {
    @Headers({"Content-Type:application/json", "Authorization:key=AAAA9g0f26Q:APA91bGxmMwWKtSpy1jsYmkv-MtMJmZJ7dzR2ELVd4KFKsxofMWSFBE-k0LhvDQrKleVR2M_XLZV8L0Tzx7eeb16oooDpvfWdFsWJs4bBsmC1VkSEGOAmJ8wpnfwMxsmWA1g2JuaKOcu"})
    @POST("fcm/send")
    Call<FCMResponse> sendMessage(@Body DataMessage body);
}
