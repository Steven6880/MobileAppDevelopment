# MobileAppDevelopment
- For Course COMP6239

- ApplicationID:com.example.comp6239

- All password for MUBU is comp6239

# 2020.4.17 Update
- 在调用bottom_navigation时，如果只对按钮的动作进行add，则会在主屏幕上一直更新并且不会消失.担心后期加载内容过多时会对软件性能造成负担。所以新建了NavHelper来解决加载和重用问题。
其中最核心的便是将界面存储在缓存中，以便高速加载，并且不需要重新建立。
fragment的detach方法是将fragment从布局中移出，但是会加入缓存队列，可以重用。attach方法则是将fragment从缓存中取出，放入当前的fragment，这样可以大大减少新建fragment带来的资源消耗。
处理完成后会通过接口返回MainActivity。
在首次进入程序时，并不会触发直接显示主界面的title，在数据启动时，程序自动触发home键，就可以显示主界面了.
对于floatactionbutton，只希望在events界面中显示，在其他界面中是不需要的。为了美观，对其添加了动画，先y轴旋转，加了弹性差值器，动画总时间是480ms。

# Login and Register
### RegisterFragment:   https://mubu.com/doc/3kG5D5YE2mw

> Code:

https://github.com/Steven6880/MobileAppDevelopment/blob/master/comp6239/comp6239/app/src/main/java/com/jiuwfung/comp6239/welcome/fragments/RegisterFragment.java

> XML Code:

https://github.com/Steven6880/MobileAppDevelopment/blob/master/comp6239/comp6239/app/src/main/res/layout/fragment_register.xml

### WelcomeActivity:   https://mubu.com/doc/7FQc31T5xSw

>Code:

https://github.com/Steven6880/MobileAppDevelopment/blob/master/comp6239/comp6239/app/src/main/java/com/jiuwfung/comp6239/WelcomeActivity.java

> XML Code:

https://github.com/Steven6880/MobileAppDevelopment/blob/master/comp6239/comp6239/app/src/main/res/layout/activity_welcome.xml

# Implements 
> appcompat:1.1.0

> constraintlayout:1.1.3

> legacy-support-v4:1.0.0

> firebase-core:17.0.0

> firebase-analytics:17.0.0

> firebase-auth:17.0.0

> firebase-database:17.0.0

> firebase-ui-storage:6.2.1

> navigation-fragment:2.2.2

> navigation-ui:2.2.2

> firebase-storage:19.1.1

> junit:junit:4.12

> androidx.test.ext:junit:1.1.1

> espresso-core:3.2.0

> butterknife:10.2.1

> butterknife-compiler:10.2.1

> material:1.1.0-alpha09

> genius:ui:2.1.1

> genius:res:2.1.1

> circleimageview:2.1.0

> glide:4.11.0

> glide:compiler:4.11.0

> matisse:0.5.2

> easypermissions:3.0.0

> ucrop:2.2.5-native
