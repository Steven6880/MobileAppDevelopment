# MobileAppDevelopment
For Course COMP6239

ApplicationID:com.example.comp6239

# 2020.4.6更新
- 开始MVP模式开发
MVP模式，即Model-View-Presenter
Model:数据处理部分
View：逻辑处理部分
Presenter：界面显示部分

View -> Presenter 发出请求
Presenter -> Model 处理数据
Model -> Presenter 处理返回数据
Presenter -> View 返回数据

在View和Model之间进行解耦

此处有图，请注意

# 2020.4.6更新
- 开始主界面搭建
按键动画及功能都在MainActivity中实现，并没哟在xml中体现。使用了ButterKnife绑定了id后，使用OnClick功能实现。

1. 主界面的TopBar使用FrameLayout封装，调用material.appbar.AppBarLayout来实现。
左侧是头像，中间是描述，右侧是搜索按钮。
为了营造一种整体的效果，topbar和手机导航栏是一体设计的，但是在topbar中规定了导航栏的大小，避免内容被手机导航栏遮住
头像单独封装，方便后面进行扩展功能,有默认头像加载。因为每个人头像大小不同，在统一显示时，设置了剪切。
字体使用了自定style中的Title风格。
为了美观，在后期测试过程中，头像的padding设置为4。搜索的padding设置为10.整个topbar的左边和右边各留有4pd的间距。中间的字体设置为白色。添加了阴影效果，为了防止4.4及以下版本不兼容，添加了ApiTarget属性，及低于Lollipop的版本不解析。

2. 主体也是用FrameLayout封装
调用app:layout_behavior="@string/appbar_scrolling_view_behavior"使其靠在topbar的下面。
在上方52dp，刚好是底部navigation的距离，可以防止重叠

3. 浮动按钮
为了方便操作，引入FloatActionButton，来自net.qiujuer.genius.ui.widget.FloatActionButton。
引用了主体部分的id，使其相对于主体在底部靠右停靠。依然在底部navigation的上方，防止重叠，同时，对于右边和下边增加间距，可以使得其美观
使用marginEnd可以直接使其对右停靠，如果使用marginRight,则需要同时设置margingLeft.
在测试过程中，发现图片太大而且不好识别，所以将图片改为白色，padding设置为20，对图片进行挤压。

4. 底部导航
使用了com.google.android.material.bottomnavigation.BottomNavigationView包。
高度设定为52，靠bottom固定。
新增lang库，期中包含了string格式的xml.在common中引入，增加了底部导航栏的字符格式。
在xml中，以menu定义。三个图形以 'ifRoom' 形式显示，意思是，界面可以放得下时，则显示，放不下时，则只显示按下的按钮的图像，其他图形将会被挤压缩小。
在后期调试中，背景设置为白色，增加了阴影，z轴高度提高了8dp。
在功能上，除了显示对应的内容以外，还监听按钮的text，在点击时将Title设置为对应得text。


Issues:
1. 图片都太大，而且字体设置为白色会更好识别
解决方案：增加图片两侧padding

使用了两个style文件，在Android5.0以上时，会使用stylev21.xml，去除阴影等。在Android5.0以下时，会使用style.xml，而此时，Android也没有阴影。


另外更新：全局设定：
在AndroidMainfest.xml中规定只能全凭使用，并加入style中的自定主题色。

另外更新：svg到xml的转换
- icon的获取，从baotu.net获取版权的icon包
- 使用VectorAsset将svg转换为xml

每日更新：
截止目前添加的库：
    firebaseVersion = '16.0.4';
    butterknifeVersion = '10.2.1';
    appcompatVersion = '1.0.2';
    recyclerviewVersion = '1.1.0';
    geniusVersion = '2.1.1';
    circleimageviewVersion = '2.1.0';
    materialVersion = '1.1.0-alpha09';
    glideVersion = '4.11.0';
    lang(自己添加)
截止目前添加的包：
    style
    language
    color