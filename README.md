#Hais_Android_Utils
	该工具类有app、ui、view、util 四个包。
	app 有一些Base 的 Application 和 Activity， 以后会增加 adapter 等。。。
	ui 有一些 弹窗 的东西， 以后会增加 Dialog 的封装等。。。
	view 是一些简单的 控件 扩展。
	util 是一些 常用的开发工具包。
	项目依赖 Gson、Vollery 。


**[有建议或疑问请到 [http://hais.pw](http://hais.pw/hais_android_utils.html) 留言]**	

#使用步骤：
	1、Application 继承AppApplication。
	2、Activity 继承 AppBaseActivity。
	3、到pw.hais.utils 中的 UtilConfig 配置所有工具类的信息。
	4、到此就能使用 Hais_Android_Utils 的所有功能。

============================================================
##一、pw.hais.app包：
	1、AppApplication
		(1)含有Activity 的统一管理， 和初始化工具。 退出App调用 exitApp() 方法。
		(2)有异常日记捕抓，捕抓类也可以上传日期到服务器。
	
	2、AppBaseActivity
		特点：在activity中，如果xml中定义的ID，跟Activity中变量的名字一样，可免去findViewById
			如：
				XML中有一 TextView 的id 为  text_hello
				Activity中有一 TextView 类型的 text_hello 的属性
				那么就可以直接 使用 text_hello，而不需要  findViewById。

		(1)属性
			tag 属性：获取每个activity的名字作为 tag 方便打Log。
			context 属性： 当前activity 的上下文，方便 设置监听器等。
			gson 属性： 整个APP中的gson。
			loadDialog 属性： 显示，隐藏进度条，并可以附带 文字。
		(2)方法
			findView(id) 方法：免强转findViewById(id)
			findViewAndSetOnClick(id) 方法：类似findView(id)，并带有设置监听器为当前activity。


##一、pw.hais.ui包：
	1、LoadProgressDialog
		菊花显示。默认在 AppBaseActivity 中统一实例化，在所有继承 AppBaseActivity的类中，可以直接使用 
		loadDialog.show(); 	//显示菊花
		loadDialog.show("数据加载中.."); 	//显示带文字的菊花
		loadDialog.dismiss();	//关闭菊花
		
	2、BasePopupWindow
		(1)创建 PopupWindow 的时候 继承 BasePopupWindow。
		(2)在 PopupWindow 构造方法中 setLayout(R.layout.failure_window, listener); //listener是当窗口关闭时的监听，可为null
		(3) drawEnd 方法 是 当 PopupWindow绘制完成的回掉，可以在里面 findViewById， 和设置监听器等各种操作。
		(4)BasePopupWindow。 的showCenter(View v); 方法，是让窗口显示在View 的中间
		(5)注：由于PopupWindow的限制，不能 在activity未绘制完成的时候调用，一般在 oncreate 中 实例化，  点击按钮等世界后 show();

	3、BaseDialog	[预留]
	
##二、pw.hais.view包
	1、ButtonFocus
		扩展于Button使用方法跟Button 类似，只是增加了 按钮的点击效果。
		使用场景在于，当设置 一张图片为 按钮的背景时，失去了点击的效果的时候。
		
	2、ImagesFocus
		扩展于ImageView使用方法跟ImageView 类似，只是增加了点击效果。
		使用场景在于，使用imageView当作 button 来使用的时候，失去了点击的效果的时候。
	
	3、StrokeTextView
		扩展于TextView使用方法跟TextView 类似，只是给 TextView 增加了描边。
		
	4、TextIntChangeView
		扩展于TextView使用方法跟TextView 类似，当 setChange(100); 的时候，数值会动态改变。

	5、TextHtmlImageView
		扩展于TextView使用方法跟TextView 类似，当 setHtml("html字符串"); 的时候，会自动加载 html中的图片。
		
##三、pw.hais.utils.http包
	1、Http，基于 Vollery
		//图片请求
		(1)方法 loadImage(ImageView v,String url) ，根据Url加载图片到 view，并缓存。
		(2)cacheImageBitmap(String url)	，缓存图片
		(3)getImageBitmap(String url, final ListenerImage listenerImage)，根据url 下载网论图片,缓存图片
		(4)loadLocalImage(String url, ImageView v)	，	根据url 加载本地图片
		
		//Http请求
		(1)getString(int method, String url, Map<String, String> parameter,Listener<String> listener)
		(2)getJsonObject(int method, String httpUrl, Map<String, String> parameter,Listener<JSONObject> listener)
		(3)getObject(int method, String httpUrl, Map<String, String> parameter,Listener<对象实体类> listener)
		
##四、pw.hais.utils.sqlite包
	Sqlite类是 对于orm的简单封装。以下是使用步骤
	1、在 Model 类 的ID中 加上注解，auto设置自增，默认为自增
		@Id(auto=true) private int id;
	2、不需要加入  数据库操作的这么 注解
		@NoDB private int id;
	3、在需要使用的地方这么使用
		DBUtil.save(u);
		DBUtil.findById(User.class,"1");
	4、只支持简单数据类型。。
	
##五、pw.hais.utils包
	1、ApkInfoUtil 用于获取 当前APK 的 包名、版本号、版本名称、程序名称。
	2、AppInfoUtil 用于获取系统的 IMEI码、mac地址、CPU序列号、自定义用户唯一标识
	3、AppNetInfoUtil 用于判断  网络是否连接、否是wifi连接、否是移动网络、wifi是否打开、Gps是否打开、打开网络设置界面
	4、AppSystemUtil 用于 打开并安装APK文件、卸载程序、判断服务是否运行、停止服务
	5、CrashUtil 异常捕抓信息。在AppApplication已经默认启用。
	6、DownTime 由于自带的 CountDownTimer 的 calan函数失效，所以产生该工具类，用法一样。
	7、EmptyUtil 检查是否为空，字符串是否为空、列表是否为空、数组是否为空、对象是否为空、Map 是否为空
	8、GenericsUtils 反射活取父类属性。
	9、ImageUtil 可以Drawable转化为Bitmap、Bitmap转化为Drawable、Bitmap转换到Byte[]、保存图片到SD卡、从SD卡加载图片
	10、KeyBoardUtil 打开软键盘、关闭软键盘
	11、L  用于 打印Log，和 Toast 。 【在发布APP的时候可以到 UtilConfig 类配置 关闭所有Log 的输出】
	12、Md5Utils  MD5的加密。。。
	13、ReaderAssetsUtil 从Assets文件夹中获取文件并读取数据
	14、SDCardUtils 判断SDCard是否可用、 获取SD卡路径、获取SD卡的剩余容量、获取系统存储路径
	15、SPUtils 简化SharedPreferences 的封装，可以存储、读取任何Object对象，或 List
	16、ScreenUtil 获得屏幕高度、 获得屏幕宽度、获得状态栏的高度、获取屏幕截图、获取屏幕尺寸与密度.
	17、TimeUtil 将时间戳转为日期、转换中文对应的时段、剩余秒数转换
	18、UtilConfig 整个工具类的配置。
	19、ViewHolderUtils 加载视图，并反射控件，一般在 adapter 中使用。
	
		
