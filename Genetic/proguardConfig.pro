-injars      Lab2.jar
-outjars     Lab2-Opt.jar
-libraryjars <java.home>/lib/rt.jar

-target 1.8
-dontwarn scala.**
-dontwarn com.**
-dontwarn org.**
-dontwarn ch.**
-verbose
-dontoptimize
-dontobfuscate
-dontusemixedcaseclassnames
-dontnote
-ignorewarnings


-keep public class main.UserMain {
    public static void main(java.lang.String[]);
}
-keep public class main.UserMain$ {
    public static void main(java.lang.String[]);
}