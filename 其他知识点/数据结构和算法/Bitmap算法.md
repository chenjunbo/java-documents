# Bitmap算法



![img](mdpic/eee966d238a04a8aba2643e46ebf6080.jpeg)

![img](mdpic/3c0dd46e6039438a93dcb7bf91a22e7c.jpeg)

![img](mdpic/cb7795eb6f494740aa9f63b3fb262e87.jpeg)

![img](mdpic/023a2970c66a4a2bb7944e7392e2cea5.jpeg)

两个月之前——

![img](mdpic/fc1d3196a7f9457498adc56037a45056.jpeg)

![img](mdpic/b7b2ade354d74fe7b7bcdc6f58b7a5bd.jpeg)

![img](mdpic/1be5deca95b04242b6c52b64ffbd788a.jpeg)

![img](mdpic/c3a824db924b42f2943c4c3fb75fe6c9.jpeg)

![img](mdpic/c26a5bae69a04d34bb1483defdc03952.jpeg)

为满足用户标签的统计需求，小灰利用 Mysql 设计了如下的表结构，每一个维度的标签都对应着 Mysql 表的一列：

![img](mdpic/b3d86db8e49f4524ace4a3f20420b604.jpeg)

要想统计所有90后的程序员该怎么做呢？

用一条求交集的SQL语句即可：

> Select count（distinct Name） as 用户数 from table whare age = '90后' and Occupation = '程序员' ;

Select count（distinct Name） as 用户数 from table whare age = '90后' and Occupation = '程序员' ;

要想统计所有使用苹果手机或者00后的用户总合该怎么做？

用一条求并集的SQL语句即可：

> Select count（distinct Name） as 用户数 from table whare Phone = '苹果' or age = '00后' ;

Select count（distinct Name） as 用户数 from table whare Phone = '苹果' or age = '00后' ;

![img](mdpic/eded3128a8ca4b38830e41939cebd83b.jpeg)

两个月之后——

![img](mdpic/d05f80d92dd0409ea70c2dc63aef149f.jpeg)

![img](mdpic/96bd86b4948d4531ac68a5eb2ecdfe16.jpeg)

![img](mdpic/5755e8971bd04a06ba6f3bf93b114995.jpeg)

![img](mdpic/c076efc650cd4612843b96fdc7b631d6.jpeg)

![img](mdpic/5483641e35354e668d013769b47a678f.jpeg)

![img](mdpic/c7e93cfc4ab74e8fb7290fbe974015df.jpeg)

![img](mdpic/fd7c7627f6c24b17914067eeb9c484bc.jpeg)

![img](mdpic/dc1fc70bd6554efbbccc0f7b603b5e2c.jpeg)

![img](mdpic/9771da19d3e640a1bd6c4aa178277d7d.jpeg)

\1. 给定长度是 10 的 bitmap，每一个 bit 位分别对应着从 0 到 9 的 10 个整型数。此时 bitmap 的所有位都是 0。

![img](mdpic/f6925832b586498b93e65156c7519a10.png)

\2. 把整型数 4 存入 bitmap，对应存储的位置就是下标为4的位置，将此 bit 置为 1。

![img](mdpic/0bbe72b8f0e4438da4430ccabdf55bb0.png)

\3. 把整型数2存入bitmap，对应存储的位置就是下标为2的位置，将此bit置为1。

![img](mdpic/aeb318208d1d4e03aec28acba6fa5654.png)

\4. 把整型数1存入bitmap，对应存储的位置就是下标为1的位置，将此bit置为1。

![img](mdpic/83f540ec896a4e74887b73fa28cf7b85.png)

\5. 把整型数3存入bitmap，对应存储的位置就是下标为3的位置，将此bit置为1。

![img](mdpic/9ab6e1bf7f494a9bbcda74445d7df540.png)

要问此时 bitmap 里存储了哪些元素？显然是 4,3,2,1，一目了然。

Bitmap 不仅方便查询，还可以去除掉重复的整型数。

![img](mdpic/52cddd89eb7145cb97a7fcaa8e7b6a86.jpeg)

![img](mdpic/706d60531b104be897a01a3e3daf1d5e.jpeg)

![img](mdpic/65b4ab0f3d95439fa942724c776540f0.jpeg)

![img](mdpic/678cf198e6594e67a93fa9171bb778cd.jpeg)

![img](mdpic/359a9afd09b148ea81d9ff2d36dd11f1.jpeg)

![img](mdpic/3dc9c21427864765b8c68a7af0a6d9b6.jpeg)

![img](mdpic/cdf5a10a2e834d72a6c1b79950f3353c.jpeg)

\3. 这样，实现用户的去重和查询统计，就变得一目了然：

![img](mdpic/9a6a26ef76634928861f05401228ce69.jpeg)

![img](mdpic/b0a5d5d60f5c409783aec2438f559649.jpeg)

![img](mdpic/651039558c3043a19ee03f1c0e1c6eb8.jpeg)

![img](mdpic/7804b9ed6b26449c9a9bf8577a174179.jpeg)

![img](mdpic/087a7bbef0ca413a8a7bb527615fc53b.jpeg)

![img](mdpic/22f1fedfad2c4026867c32cb737d3cf2.jpeg)

一周之后......

![img](mdpic/c469b9f5420b42cfa52e75517c830fde.jpeg)

![img](mdpic/a048478cc6a24bcfa8448c41647441f3.jpeg)

![img](mdpic/eb2fa891d7a54c4a8b385ca7f1ffa88a.jpeg)

![img](mdpic/fbdc2aa64811400eb6af6f2ad56c6005.jpeg)

我们以上一期的用户数据为例，用户基本信息如下。按照年龄标签，可以划分成 90 后、00 后两个 Bitmap：

![img](mdpic/b14d45483bee4cda8ddb9839d86f6d5f.jpeg)

用更加形象的表示，90 后用户的 Bitmap 如下：

![img](mdpic/61f0d20f868a49ddb9567f5b6ac4343c.jpeg)

这时候可以直接求得非90后的用户吗？直接进行非运算？

![img](mdpic/7f0fb097f19849229ed7a86bbe1d468b.jpeg)

显然，非 90 后用户实际上只有 1 个，而不是图中得到的 8 个结果，所以不能直接进行非运算。

![img](mdpic/24e6c35b70b54e25b1e1693488427046.jpeg)

![img](mdpic/0a50452a25c648af8f1f45cb69da3151.jpeg)

同样是刚才的例子，我们给定 90 后用户的 Bitmap，再给定一个全量用户的 Bitmap。最终要求出的是存在于全量用户，但又不存在于 90 后用户的部分。

![img](mdpic/92dbd025757c4904badcfe52edc09ff7.jpeg)

如何求出呢？我们可以使用异或操作，即相同位为 0，不同位为 1。

![img](mdpic/f8fe2766b9474f4999e61bd3a7ee114b.jpeg)

![img](mdpic/d4e208220f9f4cb8a0adf88abf0d4430.jpeg)

![img](mdpic/724a343541774f278e91f965c2d0d90c.jpeg)

【 图片来源：null 所有者：null 】

![img](mdpic/9c09a17ad28f4812baf4463d1ad56069.jpeg)

![img](mdpic/89dc711abc6e48b48e0b877749c58ebc.jpeg)

![img](mdpic/4a2687b71aab4f8d939fd0f5a8d09b4f.jpeg)

![img](mdpic/acd45d28d3b0478d93e3da2add3ef747.jpeg)

![img](mdpic/148cc36240194fbc80dda8af7cde8e20.jpeg)

![img](mdpic/0e0c2a8e0f9b4a689dcdbbe7d0ca3c46.jpeg)

![img](mdpic/b645d465674b4357be38e3b5c2f9902c.jpeg)

![img](mdpic/597f94422d4a4c6d8fb415eed81b97ac.jpeg)

![img](mdpic/a75a93bf576e42f7b937603a5507914c.jpeg)

![img](mdpic/ff99a951db0847b099d9a98b59bf96f6.jpeg)

![img](mdpic/cceb3d89b23b419c89c9babf39e69988.jpeg)

![img](mdpic/cc747da05969408fb2b0f15d7a7d5641.jpeg)

![img](mdpic/2d394b833cf84e31b701ab93f7ab07b5.jpeg)

![img](mdpic/7ee2443934fb4f039f4b9522a0239d65.jpeg)

![img](mdpic/dac3f5223c0649dfb853a0b62c3512da.jpeg)

![img](mdpic/68b3583df20b44798728d0ce42bd157b.jpeg)

![img](mdpic/f055e86d12154231b1899997b581b6f2.jpeg)

![img](mdpic/5d3956be4e7447cfb376a7094bb30078.jpeg)

![img](mdpic/88032d1904084b1cbffe6e797cf11a66.jpeg)

![img](mdpic/100b5ee393164eae9f9593fef92ed82e.jpeg)

![img](mdpic/751ee8fdcabb498b91bfe19a66686a86.jpeg)

![img](mdpic/3bffe1519c0a4505a271b5b754ac46b5.jpeg)

![img](mdpic/1d7980015aec46a1a92bf2f5cb3fb9cb.jpeg)

![img](mdpic/57f4ff6a64664adfa579902721f051a3.jpeg)

![img](mdpic/a7bab3a55d8840f2ac74165c0f5a85bc.jpeg)

![img](mdpic/120d7926302e427a84ff09fd7ebaeddf.jpeg)

![img](mdpic/5faca52d8a384d689980e33b11dc0ca8.jpeg)

![img](mdpic/4cc8588d8a1049c08fa155a61f82a716.jpeg)

![img](mdpic/f7a5a7c5c1604d328a65ba470ff8c982.jpeg)

![img](mdpic/03e6579f83ec40a8bcf72bcbf2c8907d.jpeg)

![img](mdpic/3d85c3d1d83a45909dad708337459034.jpeg)

![img](mdpic/d99f3741960e4f3f82d95b6dd0bab39e.jpeg)

25769803776 L = 11000000000000000000000000000000000 B

8589947086 L = 1000000000000000000011000011001110 B

![img](mdpic/e459d44868e74482bd7be491ced4cd07.jpeg)

![img](mdpic/300f722fdcea4dbba339d32c5e8396e9.jpeg)

![img](mdpic/ff203c50e53a4e5c9b1c2216b7836058.jpeg)

![img](mdpic/34ba3708ecdc4e33bdd04f9abea40b66.jpeg)

![img](mdpic/a27d22a1c66144fd989abf3b0ceb3285.jpeg)

![img](mdpic/af434cc45cdb49e98072a8014aad24da.jpeg)

1.解析 Word 0，得知当前 RLW 横跨的空 Word 数量为 0，后面有连续 3 个普通 Word。

2.计算出当前 RLW 后方连续普通 Word 的最大 ID 是 64 X (0 + 3) -1 = 191。

\3. 由于 191 < 400003，所以新 ID 必然在下一个 RLW（Word4）之后。

4.解析 Word 4，得知当前 RLW 横跨的空 Word 数量为 6247，后面有连续 1 个普通 Word。

5.计算出当前 RLW（Word4）后方连续普通 Word 的最大 ID 是 191 + （6247 + 1）X64 = 400063。

6.由于 400003 < 400063，因此新 ID 400003 的正确位置就在当前 RLW（Word4）的后方普通 Word，也就是 Word 5 当中。

最终插入结果如下：

![img](mdpic/e57becc6a04540cea62559978d69d1d7.jpeg)

![img](mdpic/2da2cef98d284c21af92340089ed199d.jpeg)

![img](mdpic/76355e3b943d42b18ca2595af56dd022.jpeg)

![img](mdpic/06dbf4ab033f475386c905054b3df787.jpeg)

![img](mdpic/09dedfbfb34b4574b4384bfc52e8855c.jpeg)

