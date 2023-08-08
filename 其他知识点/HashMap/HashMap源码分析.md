# HashMap

### 1. 属性介绍

```java
	//哈希表数组的默认长度 16
	static final int DEFAULT_INITIAL_CAPACITY = 1 << 4; // aka 16

    //哈希表数组的最大长度,2的30次方的原因是,int最大值 2的31次方减 1,所以只能是 30 次方
    static final int MAXIMUM_CAPACITY = 1 << 30;

    //默认的加载因子,加载因子指的是 hashmap 中数据个数超过数组长度*当前加载因子的时候会触发扩容
    static final float DEFAULT_LOAD_FACTOR = 0.75f;

   	//当单个位置的数据链表长度超过 8 的时候会将该链表转换为红黑树
    static final int TREEIFY_THRESHOLD = 8;

    //当当前位置的红黑树内容长度小于 6 的时候会重新变回链表
    static final int UNTREEIFY_THRESHOLD = 6;

    //当某个位置的链表在转换为红黑树的时候,如果此时数组长度小于 64 会先进行扩容
    static final int MIN_TREEIFY_CAPACITY = 64;

```



### 2 Hash 方法介绍



```java
static final int hash(Object key) {
        int h;
  //计算 key 的 hashcode 后再次和 hashcode 右移 16 位进行异或运算
  //目的是让 hash 的高位参与低位的运算,从而在不同的 key 但是低位相同的情况下改变低位的值
  //原因是 hashmap 初始长度 16,计算位置是用长度-1&hashcode,所以数据放的位置完全取决于 key 的 hashcode 的最后四位,如果最后 4 位一样,即便不是一样的 key,也会放在一起,产生碰撞高位运算有几率将不同的 key 的相同低位改为不同低位
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
```



### 3 put 方法介绍

```java
  final V putVal(int hash, K key, V value, boolean onlyIfAbsent,
                   boolean evict) {
        //声明了一个局部变量 tab,局部变量 Node 类型的数据 p,int 类型 n,i
        Node<K,V>[] tab; Node<K,V> p; int n, i;
        //首先将当前 hashmap 中的 table(哈希表)赋值给当前的局部变量 tab,然后判断tab 是不是空或者长度是不是 0,实际上就是判断当前 hashmap 中的哈希表是不是空或者长度等于 0
        if ((tab = table) == null || (n = tab.length) == 0)
        //如果是空的或者长度等于0,代表现在还没哈希表,所以需要创建新的哈希表,默认就是创建了一个长度为 16 的哈希表
            n = (tab = resize()).length;
        //将当前哈希表中与要插入的数据位置对应的数据取出来,(n - 1) & hash])就是找当前要插入的数据应该在哈希表中的位置,如果没找到,代表哈希表中当前的位置是空的,否则就代表找到数据了, 并赋值给变量 p
        if ((p = tab[i = (n - 1) & hash]) == null)
            tab[i] = newNode(hash, key, value, null);//创建一个新的数据,这个数据没有下一条,并将数据放到当前这个位置
        else {//代表要插入的数据所在的位置是有内容的
        //声明了一个节点 e, 一个 key k
            Node<K,V> e; K k;
            if (p.hash == hash && //如果当前位置上的那个数据的 hash 和我们要插入的 hash 是一样,代表没有放错位置
            //如果当前这个数据的 key 和我们要放的 key 是一样的,实际操作应该是就替换值
                ((k = p.key) == key || (key != null && key.equals(k))))
                //将当前的节点赋值给局部变量 e
                e = p;
            else if (p instanceof TreeNode)//如果当前节点的 key 和要插入的 key 不一样,然后要判断当前节点是不是一个红黑色类型的节点
                e = ((TreeNode<K,V>)p).putTreeVal(this, tab, hash, key, value);//如果是就创建一个新的树节点,并把数据放进去
            else {
                //如果不是树节点,代表当前是一个链表,那么就遍历链表
                for (int binCount = 0; ; ++binCount) {
                    if ((e = p.next) == null) {//如果当前节点的下一个是空的,就代表没有后面的数据了
                        p.next = newNode(hash, key, value, null);//创建一个新的节点数据并放到当前遍历的节点的后面
                        if (binCount >= TREEIFY_THRESHOLD - 1) // 重新计算当前链表的长度是不是超出了限制
                            treeifyBin(tab, hash);//超出了之后就将当前链表转换为树,注意转换树的时候,如果当前数组的长度小于MIN_TREEIFY_CAPACITY(默认 64),会触发扩容,我个人感觉可能是因为觉得一个节点下面的数据都超过8 了,说明 hash寻址重复的厉害(比如数组长度为 16 ,hash 值刚好是 0或者 16 的倍数,导致都去同一个位置),需要重新扩容重新 hash
                        break;
                    }
                    //如果当前遍历到的数据和要插入的数据的 key 是一样,和上面之前的一样,赋值给变量 e,下面替换内容
                    if (e.hash == hash &&
                        ((k = e.key) == key || (key != null && key.equals(k))))
                        break;
                    p = e;
                }
            }
            if (e != null) { //如果当前的节点不等于空,
                V oldValue = e.value;//将当前节点的值赋值给 oldvalue
                if (!onlyIfAbsent || oldValue == null)
                    e.value = value; //将当前要插入的 value 替换当前的节点里面值
                afterNodeAccess(e);
                return oldValue;
            }
        }
        ++modCount;//增加长度
        if (++size > threshold)
            resize();//如果当前的 hash表的长度已经超过了当前 hash 需要扩容的长度, 重新扩容,条件是 haspmap 中存放的数据超过了临界值(经过测试),而不是数组中被使用的下标,因为 java 官方建议我们使用 hashmap 的时候尽量让 key 的 hashcode 算法好一点,来尽量减少 hash 碰撞,在没有碰撞的情况下,基本上可以保证一个位置就一个数据
        afterNodeInsertion(evict);
        return null;
    }
```





### 4 扩容方法



```java
//haspmap 触发扩容的条件有两个,一个是当存放的数据超过临界值的时候会触发扩容(包括首次创建),另外一个是当需要转成红黑树的时候,如果当前数组的长度小于 64,会触发扩容
final Node<K,V>[] resize() {
    //声明了一个 oldtab ,并且把当前(扩容前) hashmap里面的哈希表赋值过来,如果是第一次放数据,此时这两个其实都是空
        Node<K,V>[] oldTab = table;
        //获取当前(扩容前)哈希表的长度,如果是第一次的话,就是 0,否则就是扩容之前的哈希表的长度
        int oldCap = (oldTab == null) ? 0 : oldTab.length;
        //当前(扩容前)哈希表需要扩容时候的长度(临界值),其实这值就是哈希表的长度*加载因子的长度,如果是第一次放数据,就是 0
        int oldThr = threshold;
        //新的长度和新的扩容长度(临界值)
        int newCap, newThr = 0;
        if (oldCap > 0) { //如果是第一次的时候,这个长度是 0,所以不符合当前判断,如果大于 0 代表是原先的老哈希表长度已经超出限制了
            if (oldCap >= MAXIMUM_CAPACITY) { //看看最新的长度是不是大于等于hashmap 对数组长度的最大限制
                threshold = Integer.MAX_VALUE;//设置为int默认的最大长度
                return oldTab;//不在库容直接返回老的数组,因为已经到达最大长度,无法扩容
            }
            else if ((newCap = oldCap << 1) < MAXIMUM_CAPACITY && 
                     oldCap >= DEFAULT_INITIAL_CAPACITY)
                newThr = oldThr << 1; //如果没有超出长度限制,新的数组长度等于老的数组长度*2(向左移动 1 位),并将新的临界值也*2
        }
        else if (oldThr > 0) //如果当前的扩容长度大于 0,代表已经有哈希表
            newCap = oldThr;
        else { //代表还没有哈希表,实际上就是第一次向 map 中放数据
            newCap = DEFAULT_INITIAL_CAPACITY;//新的哈希表长度为当前map 的默认值
            newThr = (int)(DEFAULT_LOAD_FACTOR * DEFAULT_INITIAL_CAPACITY); //新的扩容长度为默认长度*默认的加载因子,这里算它的原因是为了不在后面放数据的时候每次都重新计算,因为每次都要算是不是应该扩容,如果不找变量接收,每次都要做数学运算
        }
        if (newThr == 0) {//如果新的长度还是 0,则继续计算
            float ft = (float)newCap * loadFactor;
            newThr = (newCap < MAXIMUM_CAPACITY && ft < (float)MAXIMUM_CAPACITY ?
                      (int)ft : Integer.MAX_VALUE);
        }
        threshold = newThr;//当前 hashma的扩容长度等于最新计算出来的扩容长度
        @SuppressWarnings({"rawtypes","unchecked"})
        Node<K,V>[] newTab = (Node<K,V>[])new Node[newCap];//根据最新的长度创建对应长度的哈希表,如果是首次创建,默认就是 16
        table = newTab;//将当前 hashmap 中的哈希表赋值为最新刚刚创建的哈希表
        if (oldTab != null) {//如果原老的哈希表有数据,需要将老的数据放到新的哈希表,如果是首次创建就不执行
            for (int j = 0; j < oldCap; ++j) { //遍历老的数组
                Node<K,V> e;
                if ((e = oldTab[j]) != null) { //取出当前遍历的位置上的第一个节点
                    oldTab[j] = null;
                    if (e.next == null)//如果当前节点没有后面的数据
                        newTab[e.hash & (newCap - 1)] = e; //新的数组的最新的节点上的数据直接就是这个数据
                    else if (e instanceof TreeNode) //判断是不是树节点,如果是 就重新对树进行分割,然后放到新的位置
                        ((TreeNode<K,V>)e).split(this, newTab, j, oldCap);
                    else { // 创建两个链表,主要是因为基本上扩容的时候,部分数据会在原始位置,另外一部分数据会去向后便宜老数组的长度,比如原先是数组长度是 16,原先在 1 位置上面的数据,扩容到 32 后要么就还在 1,要么就应该去17,也就是向后移动原始长度(或者是扩容增加的长度)
                        Node<K,V> loHead = null, loTail = null;
                        Node<K,V> hiHead = null, hiTail = null;
                        Node<K,V> next;
                        do {
                            next = e.next; //首先将当前的下一个数据赋值给 e
                            if ((e.hash & oldCap) == 0) {//根据原先的寻址算法得出,hash和长度的与的值是 0 的是原始位置,符合应该在原始位置条件的创建一条链表,因为==0意味着hash的对应这个位置的数据是0,所以扩容后和新的长度与还是取决于原先的长度,比如key的hash的低5位是00111,当从16扩容到32的时候,因为32减去1是11111,但是因为hash的第五位是0,所以之后的实际的位置还是取决于之前的四位,和扩容之前是一样的结果也就是还是0-15,如果hash的第五位是1,那么结果就比之前的四位加上第五位的值,也就是16,也就是扩容的多少加多少
                                if (loTail == null)//如果没有数据
                                    loHead = e;//当前节点就是一个头
                                else
                                    loTail.next = e;//否则当前的尾节点下一条数据就是 e
                                loTail = e;//e 就成为了尾结点
                            }
                            else {//代表不符合原始位置的条件,就创建另外一个链表,来存放另外一部分数据
                                if (hiTail == null)//如果没有数据
                                    hiHead = e;//当前节点就是一个头
                                else
                                    hiTail.next = e;//否则当前的尾节点下一条数据就是 e
                                hiTail = e;//e 就成为了尾结点
                            }
                        } while ((e = next) != null);//如果当前位置下一个数据不等于空,继续向下找
                        if (loTail != null) { 
                            loTail.next = null;
                            newTab[j] = loHead;//遍历完成后,当前位置的数据为上面构建的应该在当前原始位置的链表数据
                        }
                        if (hiTail != null) {
                            hiTail.next = null;
                            newTab[j + oldCap] = hiHead;//将另外一部分数据直接放到后面的位置,位置为原始位置加上偏移量(因为扩容就是翻倍长度,所以偏移量就是原始的长度或者说是扩容增加的长度)
                        }
                    }
                }
            }
        }
        return newTab; //返回最新创建的那个哈希表
    }
```



### 5 链表转红黑树



```java
  /**
     * Replaces all linked nodes in bin at index for given hash unless
     * table is too small, in which case resizes instead.
     */
    final void treeifyBin(Node<K,V>[] tab, int hash) {
        int n, index; Node<K,V> e;
      //如果当前哈希表是空的或者是哈希表的数组长度小于 64,则触发扩容,这也是 hashmap 扩容的第二个条件和方式,这里的目的是先通过扩容来重新分配数据,让数据均匀一些,因为 java 官方建议我们使用 hashmap 的时候尽量让 key 的 hashcode 算法好一点,来尽量减少 hash 碰撞,在没有碰撞的情况下,如果出现了链表,基本上可能性就是数组长度比较短,所以先扩容平均下,不行再转
        if (tab == null || (n = tab.length) < MIN_TREEIFY_CAPACITY)
            resize();
        else if ((e = tab[index = (n - 1) & hash]) != null) {//不能扩容的情况下才会进行转树操作
            TreeNode<K,V> hd = null, tl = null; //转成树节点
            do {
                TreeNode<K,V> p = replacementTreeNode(e, null);
                if (tl == null)
                    hd = p;
                else {
                    p.prev = tl;
                    tl.next = p;
                }
                tl = p;
            } while ((e = e.next) != null);
            if ((tab[index] = hd) != null)
                hd.treeify(tab);
        }
    }
```



### 6 JDK1.7 多线程扩容环形链表

> 在 JDK1.7 以及之前的版本,HashMap 在扩容后再查询数据的时候有极低概率会发生 CPU 占用百分百的问题,原因是产生了环形链表,导致无限查询,最终将 CPU 资源耗尽

*`这是 1.7 的resize 扩容方法`*

```java
void resize(int newCapacity)
{
    Entry[] oldTable = table;
    int oldCapacity = oldTable.length;
    ......
    //创建一个新的Hash Table
    Entry[] newTable = new Entry[newCapacity];
    //将Old Hash Table上的数据迁移到New Hash Table上
    transfer(newTable);
    table = newTable;
    threshold = (int)(newCapacity * loadFactor);
}
```

*`好，重点在这里面的transfer()!`*

```java
void transfer(Entry[] newTable)
{
    Entry[] src = table;
    int newCapacity = newTable.length;
    //下面这段代码的意思是：
    //  从OldTable里摘一个元素出来，然后放到NewTable中
    for (int j = 0; j < src.length; j++) {
        Entry<K,V> e = src[j];
        if (e != null) {
            src[j] = null;
            do {
                Entry<K,V> next = e.next; //如果线程1在这里卡住
                int i = indexFor(e.hash, newCapacity);
                e.next = newTable[i];
                newTable[i] = e;
                e = next;
            } while (e != null);
        }
    }
}
```



假设我们称当前要 重新放的数据为 a, 当前a 的下一个为 b, 目标位置上面的数据为 c(c 可能会null,但是无所谓)



`线程 1`

> Entry<K,V> next = e.next; //如果线程1在这里卡住
>
>这个时候线程 1 中的 next 还没值, 局部变量e就是 a 对象,一定要记住 e 此时是局部变量,所以它的指向不会随着其他线程的修改而变化



`线程 2`



> Entry<K,V> next = e.next;  //这里的 next 也是 b, e 就是 a 对象
>
>  int i = indexFor(e.hash, newCapacity);//计算a 应该出现的位置
>
> e.next = newTable[i];//将a 对象的 next 属性指定为它应该存放的新位置的表头对象 c,注意newTable指向的是一个成员变量
>
> newTable[i] = e; //将当前表头对象设置为 a
>
>当上述代码执行完成后现在数组的最新位置的数据为 a--->c 这样的顺序
>
>



`线程 1`

>此时切换回线程 1
>
>Entry<K,V> next = e.next; //因为此时线程 1 中的这个局部变量 e 仍旧指向的是对象 a, 所以此时的 next 就是 c(但是无所谓)
>
>int i = indexFor(e.hash, newCapacity);//计算a 应该出现的位置,和线程 2 计算的位置是一样的
>
>e.next = newTable[i];//将a 对象的 next 属性指定为它应该存放的新位置的表头对象 ,但是因为在线程2 中表头对象已经被替换为a 了,所以此时实际上 a.next 对象仍旧是a 自己,因为实际上指向的是同一个对象,所以线程 2 中给 a 设置的 next 为 c 就会被覆盖为 a
>
>newTable[i] = e; //将当前表头对象设置为 a,这个 a 实际上和之前线程 2 的 a 是同一个对象



**`当上述代码执行完成后,我们发现当前这个位置的表头数据是 a ,然后 a.next 还是自己 a,然后接着它的 next 还是自己 a,最终陷入无限循环的链表中`**