![](Pictures\logo.png)

> Author：Amy
>
> Version：9.0.2

[TOC]

### 一、引言

---

#### 1.1HTML概念

> ​	  网页，是网站中的一个页面，通常是网页是构成网站的基本元素，是承载各种网站应用的平台。通俗的说，网站就是由网页组成的。通常我们看到的网页都是以htm或html后缀结尾的文件，俗称 HTML文件。
>



### 二、HTML简介

------

#### 2.1 什么是HTML

> HTML全称：Hyper Text Markup Language(超文本标记语言)
>
> - 超文本:页面内可以包含图片、链接，甚至音乐、程序等非文字元素
> - 标记:标签，不同的标签实现不同的功能
> - 语言:人与计算机的交互工具  



#### 2.2 HTML能做什么

> HTML使用标记标签来描述网页,展示信息给用户
>



#### 2.3 HTML书写规范

> - HTML标签是以尖括号包围的关键字
> - HTML标签通常是成对出现的，有开始就有结束 
> - HTML通常都有属性，格式:属性＝‘属性值’(多个属性之间空格隔开) 
> - HTML标签不区分大小写,建议全小写

### 三、HTML基本标签

------

#### 3.1 结构标签

```html
<html>:根标签       
    <head>：网页头标签
   		<title></title>:页面的标题      
    </head>      
    <body></body>：网页正文
</html>
```

|   属性名   |                代码                |             描述             |
| :--------: | :--------------------------------: | :--------------------------: |
|    text    |    < body text="#f00"></ body>     | 设置网页正文中所有文字的颜色 |
|  bgcolor   |   < body bgcolor="#00f"></ body>   |       设置网页的背景色       |
| background | < body background="1.png"></ body> |       设置网页的背景图       |

> 颜色的表示方式：                        
>
> - 第一种方式：用表示颜色的英文单词，例，red green blue                 
>
> - 第二种方式：用16进制表示颜色，例，#000000  #ffffff  #325687   #377405

#### 3.2 排版标签	

> - 可用于实现简单的页面布局
>
>
> - 注释标签：<!--注释-->                
> - 换行标签：< br>
> - 段落标签：< p>文本文字</ p>   
>   - 特点：段与段之间有空行                       
>   - 属性：align对齐方式(left、center、right)          
> - 水平线标签:< hr/>                      
>   - 属性:   
>     - ​    width：水平线的长度(两种:第一种:像素表示；第二种，百分比表示) 
>     - ​    size: 水平线的粗细 (像素表示，例如：10px)                             
>     - ​    color:水平线的颜色                                  
>     - ​    align:水平线的对齐方式

#### 3.3 块标签

> 使用CSS+DIV是现下流行的一种布局方式

| 标签 |      代码       |              描述              |
| :--: | :-------------: | :----------------------------: |
| div  |  < div></ div>  |   行级块标签，独占一行，换行   |
| span | < span></ span> | 行内块标签，所有内容都在同一行 |

#### 3.4 基本文字标签

>  font标签处理网页中文字的显示方式

| 属性名 |             代码             |                 描述                 |
| :----: | :--------------------------: | :----------------------------------: |
|  size  |   < font size="7"></ font>   | 用于设置字体的大小，最小1号，最大7号 |
| color  | < font color="#f00"></ font> |          用于设置字体的颜色          |
|  face  | < font face="宋体"></ font>  |          用于设置字体的样式          |

#### 3.5 文本格式化标签

> 使用标签实现标签的样式处理

|  标签  |        代码         |   描述   |
| :----: | :-----------------: | :------: |
|   b    |      < b></ b>      | 粗体标签 |
| strong | < strong></ strong> |   加粗   |
|   em   |     < em></ em>     | 强调字体 |
|   i    |      < i></ i>      |   斜体   |
| small  |  < small></ small>  | 小号字体 |
|  big   |    < big></ big>    | 大号字体 |
|  sub   |    < sub></ sub>    | 上标标签 |
|  sup   |    < sup></ sup>    | 下标标签 |
|  del   |    < del></ del>    |  删除线  |

#### 	3.6 标题标签

> 随着数字增大文字逐渐变小，字体是加粗的，内置字号，默认占据一行

| 标签 |    代码     |       描述        |
| :--: | :---------: | :---------------: |
|  h1  | < h1></ h1> | 1号标题，最大字号 |
|  h2  | < h2></ h2> |      2号标题      |
|  h3  | < h3></ h3> |      3号标题      |
|  h4  | < h4></ h4> |      4号标题      |
|  h5  | < h5></ h5> |      5号标题      |
|  h6  | < h6></ h6> | 6号标题，最小字号 |

#### 3.7 列表标签(清单标签)

> 无序列表：使用一组无序的符号定义， < ul>< /ul>

```html
<ul type="circle">
    <li></li>
</ul>
```

| 属性值 |   描述   |         用法举例          |
| :----: | :------: | :-----------------------: |
| circle |  空心圆  | < ul type="circle">< /ul> |
|  disc  |  实心圆  |  < ul type="disc">< /ul>  |
| square | 黑色方块 | < ul type="square">< /ul> |

> 有序列表：使用一组有序的符号定义，  < ol>< /ol>

```html
<ol type="a" start="1">
    <li></li>
</ol>
```

| 属性值 |     描述     |       用法举例        |
| :----: | :----------: | :-------------------: |
|   1    |   数字类型   | < ul type="1">< /ul>  |
|   A    | 大写字母类型 | < ul type="A" >< /ul> |
|   a    | 小写字母类型 | < ul type="a">< /ul>  |
|   I    |  大写古罗马  | < ul type="I">< /ul>  |
|   i    |  小写古罗马  | < ul type="i">< /ul>  |

> 列表嵌套：无序列表与有序列表相互嵌套使用

```html
代码举例：
	<ol>
		<li></li>
        <li></li>
        <li>
        	<ul>
                <li></li>
            </ul>
        </li>
	</ol>
```

#### 3.8 图形标签

> 在页面指定位置处中引入一幅图片， < img />

| 属性名 |         描述         |
| :----: | :------------------: |
|  src   |    引入图片的地址    |
| width  |      图片的宽度      |
| height |      图片的高度      |
| border |      图片的边框      |
| align  |  与图片对齐显示方式  |
|  alt   |       提示信息       |
| hspace |  在图片左右设定空白  |
| vspace | 在图片的上下设定空白 |

#### 3.9 链接标签

> - 在页面中使用链接标签跳转到另一页面
>
>   - 标签： < a href="">< /a>
>
>   - 属性：href:跳转页面的地址(跳转到外网需要添加协议)   
> - 设置跳转页面时的页面打开方式，target属性
>   - _blank在新窗口中打开
>   - _self在原空口中打开

> - 指向同一页面中指定位置
>   - 定义位置： < a name="名称">< /a>
>   - 指向： < a href="#名称">< /a>

#### 3.10 表格标签

> 普通表格(table,tr,td)

```
<table>
	<tr>
		<td></td>
	</tr>
</table>
```

> 表格的列标签(th)：内容有加粗和居中效果

```html
<table>
	<tr>
		<th></th>
	</tr>
</table>
```

> 表格的列合并属性(colspan)：在同一行内同时合并多个列

```
<table>
	<tr>
		<td colspan=""></td>
	</tr>
</table>
```

> 表格的行合并属性(rowspan)：在同一列跨多行合并

```html
<table>
	<tr rowspan="">
		<td></td>
	</tr>
</table>
```

### 四、综合案例

---

|              案例效果图              |
| :----------------------------------: |
| ![](Pictures\html基本标签案例图.png) |

### 五、HTML表单标签	

------

> html表单用于收集不同类型的用户输入数据
>

#### 5.1 form元素常用属性

> - action表示动作，值为服务器的地址，把表单的数据提交到该地址上处理
> - method:请求方式：get 和post
>   - get:
>     - 地址栏,请求参数都在地址后拼接 path?name="张三"&password="123456"
>     - 不安全
>     - 效率高
>     - get请求大小有限制，不同浏览器有不同，但是大约是2KB；一般情况用于查询数据
>   - post：
>     - 地址栏：请求参数单独处理。
>     - 安全可靠些
>     - 效率低
>     - post请求大小理论上无限；一般用于插入删除修改等操作     
> - enctype:表示是表单提交的类型
>   -  默认值：application/x-www-form-urlencoded  普通表单
>   - multipart/form-data  多部分表单(一般用于文件上传)​	

#### 	5.2 input元素

> 作为表单中的重要元素，可根据不同type值呈现为不同状态

|  属性值  |     描述     |           代码            |
| :------: | :----------: | :-----------------------: |
|   text   |  单行文体框  |   < input type="text"/>   |
| password |    密码框    | < input type="password"/> |
|  radio   |   单选按钮   |  < input type="radio"/>   |
| checkbox |    复选框    | < input type="checkbox"/> |
|   date   |    日期框    |   < input type="date"/>   |
|   time   |    时间框    |   < input type="time"/>   |
| datetime | 日期和时间框 | < input type="datetime"/> |
|  email   | 电子邮件输入 |  < input type="email"/>   |
|  number  |   数值输入   |  < input type="number"/>  |
|   file   |   文件上传   |   < input type="file"/>   |
|  hidden  |    隐藏域    |  < input type="hidden"/>  |
|  range   |   取值范围   |  < input type="range"/>   |
|  color   |   取色按钮   |  < input type="color"/>   |
|  submit  | 表单提交按钮 |  < input type="submit"/>  |
|  button  |   普通按钮   |  < input type="button"/>  |
|  reset   |   重置按钮   |  < input type="reset"/>   |
|  image   | 图片提交按钮 |  < input type="image"/>   |

#### 	5.3  select 元素(下拉列表)

> - 单选下拉列表：< select>< /select>
>
> - 默认选中属性：selected="selected"

```html
<select>
    <option selected="selected">内容</option>
    ...
    <option></option>
</select>
```

> - 多选下拉列表属性： < select></ select>
> - 多选列表：multiple="multiple"

```html
<select multiple="multiple">
    <option></option>
</select>
```

#### 	5.4  textarea元素(文本域)

> 多行文本框： < textarea cols="列" rows="行">< /textarea>

#### 5.5 综合示例

|           案例效果图           |
| :----------------------------: |
| ![](Pictures\form表单示例.png) |



### 六、HTML框架标签

------

> - 通过使用框架，你可以在同一个浏览器窗口中显示不止一个页面。每份HTML文档称为一个框架，并且每个框架都独立于其他的框架。
> - 使用框架的缺点：
>   - 开发人员必须同时跟踪更多的HTML文档
>   - 很难打印整张页面

#### 6.1 框架结构标签frameset

> - 框架结构标签（ < frameset>< /frameset>）用于定义如何将窗口分割为框架
> - 每个 frameset 定义了一系列行或列 
> - rows/columns 的值规定了每行或每列占据屏幕的面积
>   - \<frameset rows="">< /frameset>
>   -  \<frameset cols="">< /frameset>

#### 6.2 框架标签frame	

> 每个frame引入一个html页面

```html
<frameset cols="*,*">
    <frame src="info1.html" />
    <frame src="info2.html" />
</frameset>
```

#### 6.3 基本的注意事项

> - 不能将  < body>< /body> 标签与  < frameset>< /frameset> 标签同时使用
> - 假如一个框架有可见边框，用户可以拖动边框来改变它的大小。为了避免这种情况发生，可以在< frame>标签中加入：noresize="noresize"。

#### 6.4 综合案例

|         案例效果图         |
| :------------------------: |
| ![](Pictures\框架页面.png) |



### 七、HTML的其它标签和特殊字符	

------

#### 7.1 其他标签

```html
<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
<!--该网页的描述-->
<meta http-equiv="description" content="this is my page">
 <!--该网页的编码-->
<meta http-equiv="content-type" content="text/html; charset=UTF-8">
<!-- href：引入css文件的地址-->
<link rel="stylesheet" type="text/css" href="./styles.css">
<!--src：js的文件地址-->
<script type="text/javascript" src=""></script>
```
#### 7.2 特殊字符

```html
占位符：空格 - &nbsp;
```

