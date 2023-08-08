# ConcurrentHashMap



# put 方法

```java
final V putVal(K key, V value, boolean onlyIfAbsent) {
    if (key == null || value == null) throw new NullPointerException();
    int hash = spread(key.hashCode());
    int binCount = 0;
    for (Node<K,V>[] tab = table;;) {
        Node<K,V> f; int n, i, fh;
        if (tab == null || (n = tab.length) == 0)
            tab = initTable(); //如果是第一次,则先创建哈希表
        else if ((f = tabAt(tab, i = (n - 1) & hash)) == null) {
          //如果当前数据要放的位置上面没有数据,则通过 cas 比较交换,将原始的 null 替换为最新的值
            if (casTabAt(tab, i, null,
                         new Node<K,V>(hash, key, value, null)))
                break;                   // no lock when adding to empty bin  无锁竞争
        }
        else if ((fh = f.hash) == MOVED)
            tab = helpTransfer(tab, f);
        else {
         
            V oldVal = null;
            synchronized (f) { //如果当前要插入的数组位置有数据的,则锁定当前表头,注意没有锁定当前要插入的对应的key 的具体数据,因为到现在位置还不知道这个位置后有没有我们要放的数据,有可能有,有可能没有,如果锁的是存在的那个节点,得先进去遍历,但是有可能没有,那么可能到最后都没有加锁,就是去了安全性的意义,所以此处锁定的是当前要插入的数据所在的位置的表头对象
                if (tabAt(tab, i) == f) {
                    if (fh >= 0) {
                        binCount = 1;
                        for (Node<K,V> e = f;; ++binCount) {
                            K ek;
                            if (e.hash == hash &&
                                ((ek = e.key) == key ||
                                 (ek != null && key.equals(ek)))) {
                                oldVal = e.val;
                                if (!onlyIfAbsent)
                                    e.val = value;
                                break;
                            }
                            Node<K,V> pred = e;
                            if ((e = e.next) == null) {
                                pred.next = new Node<K,V>(hash, key,
                                                          value, null);
                                break;
                            }
                        }
                    }
                    else if (f instanceof TreeBin) {
                        Node<K,V> p;
                        binCount = 2;
                        if ((p = ((TreeBin<K,V>)f).putTreeVal(hash, key,
                                                       value)) != null) {
                            oldVal = p.val;
                            if (!onlyIfAbsent)
                                p.val = value;
                        }
                    }
                }
            }
            if (binCount != 0) {
                if (binCount >= TREEIFY_THRESHOLD)
                    treeifyBin(tab, i);
                if (oldVal != null)
                    return oldVal;
                break;
            }
        }
    }
    addCount(1L, binCount);
    return null;
}
```



### 1.7 和 1.8 的区别

```
在 JDK1.7的时候ConcurrentHashMap的安全是通过分段锁实现的,一个长度为 16 的分段锁,所谓的分段锁就是根据哈希表的数组长度除以 16 得到一个值,这个值就是一个分段锁能锁定的哈希表数组的长度,比如在初始情况下,哈希表的长度为 16 ,所以一个分段锁刚好锁一个数组位置,也就是锁一个表头,当扩容到 32 后,一个分段锁能锁定两个数组位置, 当向 hashmap 中存放数据的时候,根据当前数据应该存放的数组的位置来找到对应的锁对象来进行抢锁,抢到后就可以执行操作
在 JDK1.8 的时候,当向 hashmap 中放数据的时候,根据 hash 方法找到当前数据应该存放的数组位置,当这个位置上面没有数据的时候通过 cas无锁的方式来将数据放进去,来减少锁竞争,当这个数组位置上面有数据的时候,就拿到这个表头(数组上面放的是链表或者树的第一个数据),然后以这个表头对象作为锁,抢到后进行下一步操作,抢不到等待,这样的情况下,哈希表的数组有多长就会有多少个锁对象,这样比JDK1.7 的时候可以减少锁竞争,提高效率, 因为当我们向不同的数组位置上放数据的时候一定不会出现安全问题,所以没必要把他们都锁起来,而 1.7 的时候你向不同位置放数据也有可能会被一个分段锁锁定
JDK 通过cas(当存档的数组位置没有数据)+synchronized(当这个位置有数据)这两种方式来代替之前分段锁目的就是为了提升效率,降低抢同一把锁的可能性,减少锁竞争带来的性能问题
```

