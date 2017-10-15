# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in /home/nicolite/Android/Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

#Slidr 开始
-dontwarn com.r0adkll.slidr
-keep class com.r0adkll.slidr.** {*;}
#Slidr 结束

#Glide 开始
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule
#Glide 结束

#腾讯mat 开始
-keep class com.tencent.stat.**  {* ;}
-keep class com.tencent.mid.**  {* ;}
#腾讯mat 结束

#腾讯bugly 开始
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}
-keep class android.support.**{*;}
#腾讯bugly 结束

#Litepal 开始
-keep class org.litepal.** {*;}
-keep class * extends org.litepal.crud.DataSupport {*;}
-keep class cn.nicolite.huthelper.model.bean.** {*;}
#Litepal 结束

#Jsoup 开始
-keep class org.jsoup.nodes.** {*;}
-keep class org.jsoup.nodes.** {*;}
-keep class org.jsoup.select.** {*;}
#Jsoup 结束

#融云 开始
-keepattributes Exceptions,InnerClasses

-keepattributes Signature

# RongCloud SDK
-keep class io.rong.** {*;}
-keep class * implements io.rong.imlib.model.MessageContent {*;}
-dontwarn io.rong.push.**
-dontnote com.xiaomi.**
-dontnote com.google.android.gms.gcm.**
-dontnote io.rong.**

# VoIP
-keep class io.agora.rtc.** {*;}

# Location
-keep class com.amap.api.**{*;}
-keep class com.amap.api.services.**{*;}

# 红包
-keep class com.google.gson.** { *; }
-keep class com.uuhelper.Application.** {*;}
-keep class net.sourceforge.zbar.** { *; }
-keep class com.google.android.gms.** { *; }
-keep class com.alipay.** {*;}
-keep class com.jrmf360.rylib.** {*;}
# 广播推送
-keep public class * extends android.content.BroadcastReceiver
-ignorewarnings
#融云 结束

