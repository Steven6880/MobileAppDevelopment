# MobileAppDevelopment
For Course COMP6239

ApplicationID:com.example.comp6239

# 2020.4.17更新
- 在调用bottom_navigation时，如果只对按钮的动作进行add，则会在主屏幕上一直更新并且不会消失.担心后期加载内容过多时会对软件性能造成负担。所以新建了NavHelper来解决加载和重用问题。
其中最核心的便是将界面存储在缓存中，以便高速加载，并且不需要重新建立。
fragment的detach方法是将fragment从布局中移出，但是会加入缓存队列，可以重用。attach方法则是将fragment从缓存中取出，放入当前的fragment，这样可以大大减少新建fragment带来的资源消耗。
处理完成后会通过接口返回MainActivity。
在首次进入程序时，并不会触发直接显示主界面的title，在数据启动时，程序自动触发home键，就可以显示主界面了.
对于floatactionbutton，只希望在events界面中显示，在其他界面中是不需要的。为了美观，对其添加了动画，先y轴旋转，加了弹性差值器，动画总时间是480ms。

# 2020.5.19更新
- 将注册功能完全实现


每日更新：
依赖文件：
> firebase-core:17.0.0'

> firebase-analytics:17.0.0'

> firebase-auth:17.0.0'

> firebase-database:17.0.0'

> firebaseVersion = '16.0.4';

> butterknifeVersion = '10.2.1';

> material:1.1.0-alpha09

> glide:4.11.0

> matisse:0.5.2

> easypermissions:3.0.0

> ucrop:2.2.5-native

> navigation-fragment:2.2.2
