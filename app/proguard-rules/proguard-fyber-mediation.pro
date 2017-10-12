# Fyber Mediation
#-dontwarn com.fyber.**
-keep class com.fyber.mediation.MediationConfigProvider {
    public static *;
}
-keep class com.fyber.mediation.MediationAdapterStarter {
    public static *;
}
-keepclassmembers class com.fyber.ads.videos.mediation.RewardedVideoMediationJSInterface {
    void setValue(java.lang.String);
}
