-injars      Lab3.jar
-outjars     Lab3-Opt.jar
-libraryjars <java.home>/lib/rt.jar

-target 1.8
-dontwarn scala.**
-dontwarn com.**
-dontwarn org.**
-dontwarn ch.**
-verbose
-optimizations code/*, field/*, method/*
-dontobfuscate
-dontusemixedcaseclassnames
-dontnote
-ignorewarnings


-keep public class genetic.main.UserMain {
    public static void main(java.lang.String[]);
}

-keep public class genetic.main.UserMain$ {
    public static void main(java.lang.String[]);
}

-keep public class genetic.**
