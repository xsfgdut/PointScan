# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

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


#指定压缩级别
-optimizationpasses 5

#不跳过非公共的库的类成员
-dontskipnonpubliclibraryclassmembers

#混淆时采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*

#把混淆类中的方法名也混淆了
-useuniqueclassmembernames

#优化时允许访问并修改有修饰符的类和类的成员
-allowaccessmodification

#将文件来源重命名为“SourceFile”字符串
-renamesourcefileattribute SourceFile
#保留行号
-keepattributes SourceFile,LineNumberTable
#避免混淆泛型
-keepattributes Signature
#不混淆注解
-keepattributes *Annotation*

#保持所有实现 Serializable 接口的类成员
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}

#Fragment不需要在AndroidManifest.xml中注册，需要额外保护下
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.app.Fragment
#java.lang.NoSuchFieldException: No field mTabStrip in class Landroid/support/design/widget/TabLayout;
#java.lang.NoSuchFieldException: No field mTextView in class Landroid/support/design/widget/TabLayout$h;
-keep class android.support.design.widget.**
-keep class android.support.design.widget.**{*;}
# 保持测试相关的代码
-dontnote junit.framework.**
-dontnote junit.runner.**
-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**

#release时不输出log
#-assumenosideeffects class android.util.Log {
#public static *** d(...);
#public static *** v(...);
#public static *** i(...);
#public static *** e(...);
#public static *** w(...);
#}

# #  ######## greenDao混淆  ###########greendao3.2.0,此是针对3.2.0，如果是之前的，可能需要更换下包名
# # --------------------------------------------
#(实体类所在的包)
-keep class com.nexwise.pointscan.bean.*{ *; }
-keep class freemarker.** { *; }
-dontwarn freemarker.**
-keep class org.greenrobot.greendao.**{*;}
-dontwarn org.greenrobot.greendao.**
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {public static java.lang.String TABLENAME;}
-keep class **$Properties

-keep class org.greenrobot.greendao.**{*;}
-keep public interface org.greenrobot.greendao.**
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class net.sqlcipher.database.**{*;}
-keep public interface net.sqlcipher.database.**
-dontwarn net.sqlcipher.database.**
-dontwarn org.greenrobot.greendao.**

-keep class com.umeng.** {*;}

-keepclassmembers class * {
   public <init> (org.json.JSONObject);
}

-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep public class com.nexwise.pointscan.R$*{
public static final int *;
}
-keep  class net.sqlcipher.** {*;}
-keep  class net.sqlcipher.database.** {*;}

# 将.class信息中的类名重新定义为"Proguard"字符串

-renamesourcefileattribute Proguard

# 并保留源文件名为"Proguard"字符串，而非原始的类名 并保留行号

-keepattributes SourceFile,LineNumberTable

#3D 地图
-keep class com.amap.api.mapcore.**{*;}
-keep class com.amap.api.maps.**{*;}
-keep class com.autonavi.amap.mapcore.*{*;}
#定位
-keep class com.amap.api.location.**{*;}
-keep class com.loc.**{*;}
-keep class com.amap.api.fence.**{*;}
-keep class com.autonavi.aps.amapapi.model.**{*;}
# 搜索
-keep class com.amap.api.services.**{*;}
# 2D地图
-keep class com.amap.api.maps2d.**{*;}
-keep class com.amap.api.mapcore2d.**{*;}
# 导航
-keep class com.amap.api.navi.**{*;}
-keep class com.autonavi.**{*;}

-keep class java.io.**{*;}



