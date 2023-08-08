![](Pictures\logo.png)

>Author：Amy
>
>Version：9.0.2

[toc]

### 一 、引言

---

#### 1.1CSS概念

>​         层叠样式表(英文全称：Cascading Style Sheets)是一种用来表现HTML（标准通用标记语言的一个应用）或XML（标准通用标记语言的一个子集）等文件样式的计算机语言。CSS不仅可以静态地修饰网页，还可以配合各种脚本语言动态地对网页各元素进行格式化。

### 二、 CSS简介

---

#### 2.1 什么是CSS

> - CSS :全称：Cascading Style Sheets  层叠样式表,定义如何显示HTML元素
> - 多个样式可以层层覆盖叠加，如果不同的css样式对同一html标签进行修饰，样式有冲突的
>   应用优先级高的，不冲突的共同作用

#### 2.2 CSS能干什么

> - 修饰美化html网页。
> - 外部样式表可以提高代码复用性从而提高工作效率。
> - html内容与样式表现分离，便于后期维护。

#### 2.3 CSS书写规范

> CSS 规则由两个主要的部分构成：选择器，以及一条或多条声明
>
> - 选择器通常是您需要改变样式的 HTML 元素。
> - 每条声明由一个属性和一个值组成。

#### 2.4 基础语法

> 选择器｛属性：值;属性:值….. ｝

|            语法示例            |
| :----------------------------: |
| ![](Pictures\css_selector.gif) |



> 注意事项：
>
> - 请使用花括号来包围声明
> - 如果值为若干单词，则要给值加引号
> - 多个声明之间使用分号;分开
> - css对大小不敏感，如果涉及到与html文档一起使用时，class与id名称对大小写敏感

### 三、 CSS导入方式

------

#### 3.1 内嵌方式(内联方式)

>把CSS样式嵌入到html标签当中，类似属性的用法

```html
 <div style="color:blue;font-size:50px">This is my HTML page. </div>
```

#### 3.2 内部方式

>在head标签中使用style标签引入css

```css
<style type=“text/css”> //告诉浏览器使用css解析器去解析
	div{color:red;font-size:50px}
</style>
```

#### 3.3 外部方式

>将css样式抽成一个单独文件，使用者直接引用

```
创建单独文件 div.css
内容示例：div{color:green;font-size:50px}
引用语句写在head标签内部
	<link rel="stylesheet" type="text/css" href=“div.css"></link>
    rel:代表当前页面与href所指定文档的关系
    type:文件类型，告诉浏览器使用css解析器去解析
    href:css文件地址
```

#### 3.4 @import方式

>在页面中引入一个独立的单独文件

```html
  <style type="text/css">
       @import url("div.css")
  </style>
 该内容放在head标签中
```

> link和@import方式的区别：
>
> - link所有浏览器都支持，@import某些版本低的IE不支持
> - @import是等待html加载完成才加载
> - @import不支持js动态修改

### 四、 CSS选择器

---

>  主要用于选择需要添加样式的html元素

#### 4.1 基本选择器

>元素选择器： 在head中使用style标签引入在其中声明元素选择器:html标签{属性:属性值}

```html
<style type="text/css">
    span{color: red;font-size: 100px}
</style>    
```

> id选择器： 给需要修改样式的html元素添加id属性标识，在head中使用style标签引入在其中声明id选择器: #id值{属性:属性值}

```html
创建id选择器：
	<div id="s1">hello,everyone!</div>
	<div id="s2">hello,everyone!</div>
    <div id="s3">hello,everyone!</div>
根据id选择器进行html文件修饰 
    <style type="text/css">
		#s1{color: red;font-size: 100px}
		#s2{color: green;font-size: 100px}
		#s3{color: blue;font-size: 100px}
    </style>
```

> class选择器：给需要修改样式的html元素添加class属性标识，在head中使用style标签引入在其中声明class选择器:  .class名{属性:属性值}

```html
创建class选择器：
    <div class="s1">hello,everyone!</div>
    <div class="s2">hello,everyone!</div>
    <div class="s3">hello,everyone!</div>
根据id选择器进行html文件修饰：
    <style type="text/css">
        .s1{color: purple;font-size: 100px}
        .s2{color: pink;font-size: 100px}
        .s3{color: yellow;font-size: 100px}
    </style>
```

>  备注：以上基本选择器的优先级从高到低：id选择器，class选择器，元素选择器

#### 4.2 属性选择器

>- 根据元素的属性及属性值来选择元素。在head中使用style标签引入在其中声明
>- 格式为：
>  - htm标签[属性=‘属性值']{css属性:css属性值;}
>  - html标签[属性]{css属性:css属性值;}

```html
body内容：
	<form name="login" action="#" method="get">
        <font size="3">用户名：</font>
        <input type="text" name="username" value="zhangsan"><br>
		<font size="3">密码：</font>
        <input type="password" name="password" value="123456"><br/>
        <input type="submit" value="登录">
	</form>
head中书写：
    <style type="text/css">
        input[type='text'] {
            background-color: pink;
        }
        input[type='password'] {
            background-color: yellow;
        }
        font[size] {
            color: green
        }
        a[href] {
            color: blue;
        }
	</style>
```

####  4.3 伪元素选择器

>-  主要是针对a标签
>-  语法：
>   - 静止状态 a:link{css属性}
>   - 悬浮状态 a:hover{css属性}
>   - 触发状态 a:active{css属性}
>   - 完成状态  a:visited{css属性}

```
代码：
	<a href="https://hao.360.cn/">点我吧</a>
样式：
    <style type="text/css">
        <!--静止状态 -->
        a:link {color: red;}
        <!--悬浮状态 -->’
        a:hover {color: green;}
        <!--触发状态 -->
        a:active {color: yellow;}
        <!--完成状态 -->
        a:visited {color: blue;}
    </style>
```

#### 4.4 层级选择器

>  父级选择器  子级选择器….，具体示例如下：

```html
 <div id="div1">
     <div class="div11">
         <span>span1-1</span>
     </div>
     <div class="div12">
         <span>span1-2</span>
     </div>
</div>
<div class="div2">
    <div id="div22">
        <span>span2-1</span>
    </div>
    <div id="div23">
        <span>span2-2</span>
    </div>
</div>
```

```css
<style type="text/css">
    #div1 .div11{color:red;}
    #div1 .div12{color:purple;}
    .div2 #div22{color:green;}
    .div2 #div23{color:blue;}
</style>
```
### 五、 CSS属性

---

#### 5.1 文字属性

|   属性名    |           取值           |     描述     |
| :---------: | :----------------------: | :----------: |
|  font-size  |           数值           | 设置字体大小 |
| font-family |    默体、宋体、楷体等    | 设置字体样式 |
| font-style  | normal正常; italic斜体;  | 设置斜体样式 |
| font-weight | 100~900数值;bold;bolder; |   粗体样式   |

#### 5.2 文本属性

|     属性名      |                          取值                           |         描述         |
| :-------------: | :-----------------------------------------------------: | :------------------: |
|      color      |              十六进制;表示颜色的英文单词;               |     设置文本颜色     |
|   text-indent   |       5px缩进5像素;20%缩进父容器宽度的百分之二十;       | 缩进元素中文本的首行 |
| text-decoration |             none;underline;overline;blink;              |     文本的装饰线     |
|   text-align    |                    left;right;center                    |   文本水平对齐方式   |
|  word-spacing   |                     normal;固定值;                      |    单词之间的间隔    |
|   line-height   |                     normal;固定值;                      |    设置文本的行高    |
|   text-shadow   | 四个取值依次是： 水平偏移；垂直偏移；模糊值；阴影颜色； |  设置阴影及模糊效果  |

#### 5.3 背景属性

|       属性名        |                取值                 |          描述          |
| :-----------------: | :---------------------------------: | :--------------------: |
|  background-color   |   16进制;用于表示颜色的英语单词;    |       设置背景色       |
|  background-image   |           url('图片路径')           |      设置背景图片      |
|  background-repeat  | repeat-y;repeat-x;repeat;no-repeat; |  设置背景图的平铺方向  |
| background-position |   top;bottom;left;right ; center;   | 改变图像在背景中的位置 |

#### 5.4 列表属性

|       属性名        |      取值       |               描述               |
| :-----------------: | :-------------: | :------------------------------: |
|   list-style-type   |     disc等      |        改变列表的标识类型        |
|  list-style-image   | url("图片地址") |          用图像表示标识          |
| list-style-position | inside;outside  | 标识出现在列表项内容之外还是内部 |

#### 5.5 尺寸属性

> - width:设置元素的宽度
> - height:设置元素的高度

#### 5.6 显示属性

> 显示属性display ，以下是常用取值：
>
> - none:不显示
> - block:块级显示
> - inline:行级显示

#### 5.7 轮廓属性

> 绘制于元素周围的一条线，位于边框边缘的外围，可起到突出元素的作用。常用属性：

|    属性名     |                  取值                   |      描述      |
| :-----------: | :-------------------------------------: | :------------: |
| outline-style | solid(实线)/dotted(虚线)/dashed(虚线)等 | 设置轮廓的样式 |
| outline-color |        16进制;用于表示颜色的英文        | 设置轮廓的颜色 |
| outline-width |                  数值                   | 设置轮廓的宽度 |

#### 5.8 浮动属性float

> - 浮动的框可以向左或向右移动，直到它的外边缘碰到包含框或另一个浮动框的边框为止。由于浮动框不在文档的普通流中，所以文档的普通流中的块框表现得就像浮动框不存在一样。
>
> - 请看下图，当把框 1 向右浮动时，它脱离文档流并且向右移动，直到它的右边缘碰到包含框的右边缘：

|          浮动示例图           |
| :---------------------------: |
| ![](Pictures\positioning.gif) |



> - 再请看下图，当框 1 向左浮动时，它脱离文档流并且向左移动，直到它的左边缘碰到包含框的左边缘。因为它不再处于文档流中，所以它不占据空间，实际上覆盖住了框 2，使框 2 从视图中消失。
>
> - 如果把所有三个框都向左移动，那么框 1 向左浮动直到碰到包含框，另外两个框向左浮动直到碰到前一个浮动框。

|           浮动示例图           |
| :----------------------------: |
| ![](Pictures\positioning2.gif) |



> 如下图所示，如果包含框太窄，无法容纳水平排列的三个浮动元素，那么其它浮动块向下移动，直到有足够的空间。如果浮动元素的高度不同，那么当它们向下移动时可能被其它浮动元素“卡住”：
>

|           浮动示例图           |
| :----------------------------: |
| ![](Pictures\positioning3.gif) |



> clear 属性：规定元素的哪一侧不允许其他浮动元素。它的取值如下：
>

|  取值   |                 描述                  |
| :-----: | :-----------------------------------: |
|  left   |        在左侧不允许浮动元素。         |
|  right  |        在右侧不允许浮动元素。         |
|  both   |     在左右两侧均不允许浮动元素。      |
|  none   |   默认值。允许浮动元素出现在两侧。    |
| inherit | 规定应该从父元素继承 clear 属性的值。 |

#### 5.9 定位属性

>相对定位(relative)：元素框偏移某个距离，元素仍保持其未定位前的形状，它原本所占的空间仍保留。

```html
<head>
	<style type="text/css">
		h2.pos_left {
			position: relative;
			left: -20px
		}
        h2.pos_right {
			position: relative;
			left: 20px
		}
	</style>
</head>
<body>
    <h2>这是位于正常位置的标题</h2>
	<h2 class="pos_left">这个标题相对于其正常位置向左移动</h2>
	<h2 class="pos_right">这个标题相对于其正常位置向右移动</h2>
	<p>相对定位会按照元素的原始位置对该元素进行移动。</p>
	<p>样式 "left:-20px" 从元素的原始左侧位置减去 20 像素。</p>
	<p>样式 "left:20px" 向元素的原始左侧位置增加 20 像素。</p>
</body>
```

> 绝对定位(absolute)：元素框从文档流完全删除，并相对于其包含块进行定位。包含块可能是文档中的另一个元素或者是初始包含块。元素原先在正常文档流中所占的空间会关闭，就好像元素原来不存在一样。元素定位后生成一个块级框。

```html
<html>
    <head>
		<meta charset="utf-8" />
		<style type="text/css">
			h2.pos_abs {
				position: absolute;
				left: 100px;
				top: 150px
			}
		</style>
	</head>
	<body>
		<h2 class="pos_abs">这是带有绝对定位的标题</h2>
		<p>通过绝对定位，元素可以放置到页面上的任何位置。下面的标题距离页面左侧 100px，距离页面顶部 150px。</p>
	</body>
</html>
```

> 固定定位(fixed)：元素框的表现类似于将 position 设置为 absolute，不过其包含块是视窗本身。

```html
<!DOCTYPE html>
<html>
	<head>
		<meta charset="UTF-8">
		<title></title>
		<style>
			#left {
				width: 200px;
				height: 200px;
				background-color: red;
				position: fixed;
				left: 0px;
				bottom: 0px;
			}
			
			#right {
				width: 200px;
				height: 200px;
				background-color: green;
				position: fixed;
				right: 0px;
				bottom: 0px;
			}
			#middle{
				width: 200px;
				height: 200px;
				background-color: blue;
				position: fixed;
				left: 0px;
				bottom: 50%;
			}
			
		</style>
	</head>
	<body>
		<div id="left">左下</div>
		<div id="right">右下</div>
		<div id="middle">中间</div>
	</body>
</html>
```

### 六、 CSS盒子模型

------

|          盒子模型图           |
| :---------------------------: |
| ![盒子模型](Pictures/box.png) |

#### 6.1 边框相关属性  

|    属性名    |             取值             |      描述      |
| :----------: | :--------------------------: | :------------: |
| border-style | solid;double;dashed;dotted等 | 设置边框的样式 |
| border-color |  16进制;用于表示颜色的英文;  | 设置边框的颜色 |
| border-width |             数值             |  设置边框的粗  |

#### 6.2 外边距相关属性

> margin：外间距,边框和边框外层的元素的距离

|    属性名     |         取值          |      描述      |
| :-----------: | :-------------------: | :------------: |
|    margin     | top;right;bottom;left | 四个方向的距离 |
|  margin-top   |         数值          |     上间距     |
| margin-bottom |         数值          |     下间距     |
|  margin-left  |         数值          |     左间距     |
| margin-right  |         数值          |     右间距     |

#### 6.3 内边距相关属性

> padding：内间距,元素内容和边框之间的距离((top right bottom left)) 

|     属性值     | 描述 |      |
| :------------: | :--: | ---- |
|  padding-left  |      |      |
| padding-right  |      |      |
|  padding-top   |      |      |
| padding-bottom |      |      |

### 七、 CSS3扩展属性

---

#### 7.1 border-radius创建圆角

> 示例：border-radius: 25px;

|        圆角示例图        |
| :----------------------: |
| ![](Pictures\border.png) |

#### 7.2 box-shadow:用于向方框添加阴影  

> 示例：box-shadow: 10px 10px 5px #888888;

|        方框加阴影示例图         |
| :-----------------------------: |
| ![](Pictures\border_shadow.png) |

#### 7.3 background-size: 属性规定背景图片的尺寸

```html
<body style="text-align: center;
    background:url(img/1.png);
    background-size: 200px 300px;
    background-repeat: no-repeat;">
</body>
```

#### 7.4 background-image:为指定元素使用多个背景图像

|                 背景图片示例图                  |
| :---------------------------------------------: |
| <img src="Pictures\bk.png" style="zoom:60%;" /> |



#### 7.5 text-shadow: 可向文本应用阴影。

>  示例：text-shadow: 5px 5px 5px #ffff00;

|                 文本应用阴影示例图                  |
| :-------------------------------------------------: |
| <img src="Pictures\shadow.png" style="zoom:50%;" /> |



### 八、 HTML结合CSS完成淘宝分类页

---

#### 8.1 案例效果图

|         案例效果图         |
| :------------------------: |
| ![](Pictures\html+css.png) |


