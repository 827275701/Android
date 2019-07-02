[TOC]

# 开发环境

+ 系统环境：Win10
+ IDE：Android Studio1.5
+ 语言：JAVA
+ 第三方库：MPAndroidChart，OkHttp



# 项目结构

## 欢迎界面

| 界面功能     | 展示图示管的界面，三秒之后跳转到登录界面                     |
| ------------ | :----------------------------------------------------------- |
| Activity名称 | Android\test\app\src\main\java\com\example\lhwei\test\Welcome |
| 对应布局文件 | Android\test\app\src\main\res\layout\welcome                 |

**主要函数介绍**

| 函数命                                    | 函数功能                                               |
| ----------------------------------------- | ------------------------------------------------------ |
| onCreate                                  | 每个Activity创建时调用的函数（自动调用，之后不再阐述） |
| handler.sendEmptyMessageDelayed(0, 3000); | 设置一个3面的定时任务                                  |
| gotoLogin                                 | 跳转到等录界面                                         |
| onKeyDown                                 | 对返回键监听并重写                                     |



## 登录界面

| 界面功能     | 登录                                                         |
| ------------ | :----------------------------------------------------------- |
| Activity名称 | Android\test\app\src\main\java\com\example\lhwei\test\MainActivity |
| 对应布局文件 | Android\test\app\src\main\res\layout\login                   |

**主要函数介绍**

| 函数命                  | 函数功能                                                     |
| ----------------------- | ------------------------------------------------------------ |
| ButtonClickListener     | 对等录按钮进行监听，基本逻辑：     1、获取UI中输入的username和password     2、校验username和password     3、校验通过之后，创建网络连接，请求服务器     4、获取服务器返回结果，判断是否登录成功     5、判断是普通管理员还是admin     6、跳转界面 |
| check_uaername_password | 校验用户名和密码                                             |
| onKeyDown               | 对返回键监听并重写，连续两次返回键就会推出程序               |



## 普通管理员

### 主界面

| 界面功能     | 查看当前博览室当前室内信息                                   |
| ------------ | :----------------------------------------------------------- |
| Activity名称 | Android\test\app\src\main\java\com\example\lhwei\test\ChooseSpot1 |
| 对应布局文件 | Android\test\app\src\main\res\layout\choose_spot1            |

**主要函数介绍**

| 函数命                             | 函数功能                                             |
| ---------------------------------- | ---------------------------------------------------- |
| get_room_name_and_room_id          | 将从服务器获取取得name和id  设置到对应的字符串数组中 |
| initSpinner                        | 下拉菜单监听函数                                     |
| get_room                           | 获取服务器内的博览室信息                             |
| getPersonInf                       | 发送HTTP请求去服务其数据库获取用户信息               |
| ButtonClickListener_T              | 对温度的历史按钮进行监听                             |
| ButtonClickListener_H              | 对湿度的历史按钮进行监听                             |
| ButtonClickListener_G              | 对有害气体浓度的历史按钮进行监听                     |
| ButtonClickListener_PersonInfo     | 对个人信息按钮进行监听                               |
| ButtonClickListener_ChangePassword | 对修改密码按钮进行监听                               |
| ButtonClickListener_ExitLogin      | 对推出登录按钮进行监听                               |



### 查看温度历史数据

| 界面功能     | 查看历史温度曲线                                             |
| ------------ | :----------------------------------------------------------- |
| Activity名称 | Android\test\app\src\main\java\com\example\lhwei\test\THistort |
| 对应布局文件 | Android\test\app\src\main\res\layout\history                 |

**主要函数介绍**

| 函数命       | 函数功能                                   |
| ------------ | ------------------------------------------ |
| setXAxis     | 设置温度折线图的X轴                        |
| setYAxis     | 设置温度折线图的Y轴                        |
| setChartData | 将从服务器获取温度的历史数据设置到折线图中 |
| showData_T   | 从服务器获取温度的历史数据                 |



### 查看湿度历史数据

| 界面功能     | 查看历史湿度曲线                                             |
| ------------ | :----------------------------------------------------------- |
| Activity名称 | Android\test\app\src\main\java\com\example\lhwei\test\HHistort |
| 对应布局文件 | Android\test\app\src\main\res\layout\history                 |

**主要函数介绍**

| 函数命       | 函数功能                                   |
| ------------ | ------------------------------------------ |
| setXAxis     | 设置湿度折线图的X轴                        |
| setYAxis     | 设置湿度折线图的Y轴                        |
| setChartData | 将从服务器获取湿度的历史数据设置到折线图中 |
| showData_H   | 从服务器获取湿度的历史数据                 |



### 查看有害气体浓度历史数据

| 界面功能     | 查看历史有害气体浓度曲线                                     |
| ------------ | :----------------------------------------------------------- |
| Activity名称 | Android\test\app\src\main\java\com\example\lhwei\test\GHistort |
| 对应布局文件 | Android\test\app\src\main\res\layout\history                 |

**主要函数介绍**

| 函数命       | 函数功能                                           |
| ------------ | -------------------------------------------------- |
| setXAxis     | 设置有害气体浓度折线图的X轴                        |
| setYAxis     | 设置有害气体浓度折线图的Y轴                        |
| setChartData | 将从服务器获取有害气体浓度的历史数据设置到折线图中 |
| showData_G   | 从服务器获取有害气体浓度的历史数据                 |



### 查看个人信息

| 界面功能     | 展示个人信息                                                 |
| ------------ | :----------------------------------------------------------- |
| Activity名称 | Android\test\app\src\main\java\com\example\lhwei\test\PersonInfo |
| 对应布局文件 | Android\test\app\src\main\res\layout\person_info             |

**主要函数介绍**

| 函数命          | 函数功能                           |
| --------------- | ---------------------------------- |
| get_person_info | 从服务器获取用户信息，并设置到界面 |



### 修改密码

| 界面功能     | 登录                                                         |
| ------------ | :----------------------------------------------------------- |
| Activity名称 | Android\test\app\src\main\java\com\example\lhwei\test\ChangePassword |
| 对应布局文件 | Android\test\app\src\main\res\layout\change_password         |

**主要函数介绍**

| 函数命              | 函数功能                           |
| ------------------- | ---------------------------------- |
| ChectoutPassword    | 检查输入密码是否符合密码的校验规则 |
| ButtonClickListener | 对确认按钮进行监听                 |



## Admin

### 添加用户

| 界面功能     | 添加普通管理员                                               |
| ------------ | :----------------------------------------------------------- |
| Activity名称 | Android\test\app\src\main\java\com\example\lhwei\test\AdminUserManagement |
| 对应布局文件 | Android\test\app\src\main\res\layout\admin_user_mangement    |

**主要函数介绍**

| 函数命                     | 函数功能                 |
| -------------------------- | ------------------------ |
| initSpinner                | 监听并获取下拉菜单的内容 |
| ButtonClickListener_submit | 监听提交按钮             |



### 删除用户

| 界面功能     | 删除普通管理员                                               |
| ------------ | :----------------------------------------------------------- |
| Activity名称 | Android\test\app\src\main\java\com\example\lhwei\test\AdminDeleteUser |
| 对应布局文件 | Android\test\app\src\main\res\layout\admin_delete_user       |

**主要函数介绍**

| 函数命                     | 函数功能     |
| -------------------------- | ------------ |
| ButtonClickListener_submit | 监听提交按钮 |



### 查询用户列表

| 界面功能     | 查询所有用户                                                 |
| ------------ | :----------------------------------------------------------- |
| Activity名称 | Android\test\app\src\main\java\com\example\lhwei\test\AdminChangeOtherInfo |
| 对应布局文件 | Android\test\app\src\main\res\layout\admin_change_other_info |

**主要函数介绍**

| 函数命    | 函数功能                             |
| --------- | ------------------------------------ |
| get_users | 从服务器获取用户信息，并设置到UI界面 |



### 修改用户信息

| 界面功能     | 修改用户信息                                                 |
| ------------ | :----------------------------------------------------------- |
| Activity名称 | Android\test\app\src\main\java\com\example\lhwei\test\ChangePassword |
| 对应布局文件 | Android\test\app\src\main\res\layout\change_password         |

**主要函数介绍**

| 函数命                     | 函数功能                   |
| -------------------------- | -------------------------- |
| initSpinner_w              | 初始化下拉菜单，并进行监听 |
| ButtonClickListener_select | 对查询按钮进行监听         |
| ButtonClickListener_submit | 对提交按钮进行监听         |



### 查询博览室信息

| 界面功能     | 查询博览室信息                                               |
| ------------ | :----------------------------------------------------------- |
| Activity名称 | Android\test\app\src\main\java\com\example\lhwei\test\AdminSelectRoom |
| 对应布局文件 | Android\test\app\src\main\res\layout\admin_select_room       |

**主要函数介绍**

| 函数命                    | 函数功能                 |
| ------------------------- | ------------------------ |
| initListView              | 设置ListView             |
| get_room_name_and_room_id | 获取所有博览室的name和id |
| get_rooms                 | 从服务器获取博览室信息   |



### 修改博览室信息

| 界面功能     | 修改博览室信息                                               |
| ------------ | :----------------------------------------------------------- |
| Activity名称 | Android\test\app\src\main\java\com\example\lhwei\test\AdminUpdateRoomInfo |
| 对应布局文件 | Android\test\app\src\main\res\layout\admin_update_room_info  |

**主要函数介绍**

| 函数命              | 函数功能               |
| ------------------- | ---------------------- |
| get_the_room_info   | 从服务器获取用户的信息 |
| ButtonClickListener | 对确认按钮进行监听     |



### 查看博览室室内数据

跟普通管理员使用形同的代码



### 查看日志

| 界面功能     | 查看日志                                                     |
| ------------ | :----------------------------------------------------------- |
| Activity名称 | Android\test\app\src\main\java\com\example\lhwei\test\AdminLogManagement |
| 对应布局文件 | Android\test\app\src\main\res\layout\admin_log_mangement     |

**主要函数介绍**

| 函数命  | 函数功能                         |
| ------- | -------------------------------- |
| get_log | 从服务器获取日志文件并显示到界面 |





# 怎样与服务器通信？

+ 使用HTTP协议与服务器进行通信
+ 自己构建了MyHttp类，将OkHttp的访问服务器的过程进行封装
+ 参数传对应的接口名和postBody就可以请求服务器，如：

```java
MyHttp myHttp = new MyHttp();
Response response = myHttp.connect("echo_log", postBody);
```



MyHttp.java 主要代码如下：

```java
    public Response connect(String method, String postBody) throws IOException{
        /*
        * 功能：设置服务器参数
        * 参数：method：对应cgi程序
        * parameter：传给cgi程序的参数
        */

        String urlStr = "http://60.205.188.244:9999/" + method;  //服务器地址+端口号+访问程序

        //设置请求的数据类型
        final MediaType MEDIA_TYPE_MARKDOWN
                = MediaType.parse("text/x-markdown; charset=utf-8");

        final OkHttpClient client = new OkHttpClient(); //得到okhttp的client

        //与服务建立连接，并请求数据
        Request request = new Request.Builder()
                .url(urlStr)
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, postBody))
                .build();

        //创建一个读取数据的句柄
        Response response = client.newCall(request).execute();

        return response; //返回获取到的内容，但类型是Response类型，需要墙砖为String类型
    }
```

