# java.nio的缓冲区
## 缓冲区基础
### 家族
> 除了boolean类型，java基本类型都有对应的缓冲区类

- Buffer
    - CharBuffer
    - ShortBuffer
    - IntBuffer
    - LongBuffer
    - FloatBuffer
    - DoubleBuffer
    - ByteBuffer
        - MappedByteBuffer  //ByteBuffer内存映射文件的特例
### 属性
> 0 <= mark <= position <= limit <= capacity
1. 容量 capacity 缓冲区能容下元素的最大数量，**永远不变**
2. 上界 limit 缓冲区的第一个不能被读或写的元素。或者说，缓冲区中现存元素的计数
3. 当前位置 position 下一个要被读或写的元素的索引。位置会自动由相应的 get( )和 put( )方法更新
4. 标记 mark 一个备忘位置。调用 mark( )来设定 mark = postion。调用 reset( )设定 position = mark。
标记在设定前是未定义的(undefined)

### 缓冲区API
> 以上8个缓冲区类都继承自抽象类Buffer，拥有一下共有api

1. 属性相关
    - capacity():int
    - position():int
    - position(int):Buffer //设置position位置
    - limit():int
    - limit(int):Buffer
    - mark():Buffer // mark=position
    - reset():Buffer // position设置为mark的位置
2. 存取相关
    - clear():Buffer //postion=0 limit=capacity mark=-1
    - flip():Buffer //准备开始读 limit=position position=0 mark=-1
    - rewind():Buffer //不改变limit位置 position=0 mark=-1
    - remaining():int //还可以读取的元素个数 limit-position
    - hasRemaining():boolean //position<limit
    - abstract isReadOnly():boolean
3. get/put 具体放数据的方式只存在于子类，Buffer父类连存取的抽象类也不提供
get/put分为绝对存取和相对存取，绝对存取需要提供位置参数get(int)/put(int)
相对存取就是存/取position处

在放数据的时候，相对put超过limit 抛出BufferOverFlowException(如果limit没被手动设置过，
此时limit=capacity);

在取数据的时候，相对get超过limit 抛出BufferUnderFlowException(如果limit没被手动设置过，
此时limit=最后一个元素后一个位置，其大小就是元素个数);

绝对存取<0或>limit则抛出IndexOutOfBoundsException

### 缓冲区压缩
> 有时需要将部分已读的元素从缓冲区释放，这需要将未读的第一个元素的位置移动到缓冲区索引0

buffer.compact() 可以做到，未读元素移动到从缓冲区索引0开始 position设为被复制的数据元素的数目，比如原有6个元素`abcdef`
需要移除头2个已读元素，把未读的4个元素位置从2-5**复制**到0-3 `cdefed` postion设为4(现在4，5位置上的元素是“死的” 和现在2，3
位置上元素相同) 有点像FIFO

### 标记

缓冲区的标记在 mark( )方法被调用之前是未定义的，调
用时标记被设为当前位置的值。reset()方法将位置设为当前的标记值。如果标记值未定义，调
用 reset()将导致 InvalidMarkException 异常。一些缓冲区方法会抛弃已经设定的标记
（rewind()，clear()，以及 flip()总是抛弃标记）。如果新设定的值比当前的标记小，调用
limit(int)或 position(int)会抛弃标记

### 比较
> 缓冲区implements Comparable

其equals充分必要条件是：
- 两个对象类型相同。包含不同数据类型的 buffer 永远不会相等，而且 buffer
绝不会等于非 buffer 对象。
- 两个对象都剩余同样数量的元素。Buffer 的容量不需要相同，而且缓冲区中剩
余数据的索引也不必相同。但每个缓冲区中剩余元素的数目（从position到limit）必须相
同。
- 在每个缓冲区中应被 Get()方法返回的剩余数据元素序列必须一致
```java
//参数小于，等于，或者大于引用 compareTo( )的对象实例时，分别返回一个负整数，0 和正整数
public class BufferCompare {
    public static void main(String[] args) {
        CharBuffer buffer1 = CharBuffer.allocate(100);
        CharBuffer buffer2 = CharBuffer.allocate(20);
        String s1 = "hello";
        String s2 = "mellow";
        fillBuffer(buffer1, s1);
        fillBuffer(buffer2, s2);
        // 怪怪怪！和习惯不一样
        //参数buffer2小于，等于，或者大于引用 compareTo( )的对象实例buffer1时，
        //分别返回一个负整数，0 和正整数
        if (buffer1.compareTo(buffer2) > 0) {
            System.out.println("buffer1 < buffer2");
        }
    }

    private static void fillBuffer(CharBuffer buffer, String s) {
        assert buffer != null && s != null;
        for (int i = 0;i < s.length();i++) {
            buffer.put(s.charAt(i));
        }
    }
}
```

### 批量存取

- get(char[] dst):CharBuffer    // 如果dst.length > buffer.remaining() BufferUnderFlowException
- get(char[] dst, int offset, int length):CharBuffer
- put(char[] src):CharBuffer    // 如果src.length > buffer.remaining() BufferOverFlowException
- put(char[] src, int offset, int length):CharBuffer
- put(CharBuffer):CharBuffer   // buffer必须有足够的空间 position~limit之间的空间 remaining()
- put(String):CharBuffer       
- put(String src, int start, int end) //将src.subString(start, end)数据放入buffer

## 缓冲区创建
- static allocate(int capacity):CharBuffer
- static wrap(char[] array):CharBuffer  //使用array作为缓冲区的存储数组
- static wrap(char[] array, int offset, int length):CharBuffer //用array作为缓冲区的存储数组 制定position=offset limit = length
不是字面上理解说将array的一部分作为缓冲区的存储区
- static hasArray():boolean //返回false说明缓冲区只读 无法调用以下方法 UnsupportedOperationExcetion
- static array():char[]    
- static arrayOffset():int //作为缓冲区的数组 开始用于缓冲区存储的开始位置 wrap(char[], int, int)创建的也是返回0，除非对数组做了切分

**CharBuffer特有**
- static wrap(CharSequence):CharBuffer
- static wrap(CharSequence csq, int start, int end):CharBuffer //CharSequence代表可读字符流， 
标准实现是String StringBuffer CharBuffer 是含有内容的

## 复制缓冲区
> 管理其他缓冲区数据的缓冲区被称为视图缓冲器

- abstract duplicate():CharBuffer   //创建与原始buffer同样的缓冲区，但底层数据共享，有不同的position mark limit
- abstract asReadOnlyBuffer():CharBuffer
- abstract slice():CharBuffer   //创建原缓冲区position~limit部分的缓冲区 newCapacity=newLimit=limit-position
保存的元素下标是position ~ limit - 1
```
//要创建一个映射到数组位置 12-20（9 个元素）的 buffer 对象，应使用下面的代码实现
char [] myBuffer = new char [100];
CharBuffer cb = CharBuffer.wrap (myBuffer);
cb.position(12).limit(21);
CharBuffer sliced = cb.slice( );
```

## 字节缓冲区
### 字节顺序
> order():`ByteOrder` 返回`ByteOrder`两个实例 大端BIG_ENDIAN(内存低地址存多字节数值高位字节，数值高位先存储)
/小端顺序LITTLE_ENDIAN

`ByteBuffer`可以通过order(ByteOrder)改变多字节数值存储顺序 但其他buffer不行

如果一个缓冲区被创建为一个 ByteBuffer 对象的视图，那么order()返回的数值就是视图被创建时其创建源头的ByteBuffer的字节顺
序设定。视图的字节顺序设定在创建后不能被改变，而且如果原始的字节缓冲区的字节顺序在之后被改变，它也不会受到影响

### 直接缓冲区
> 操作系统在内存中进行I/O操作，对操作系统而言，这些内存区域是相连的字节序列，但在JVM中字节数组不会在内存中连续存储，或者
它会被GC移动位置，因此操作系统要在JVM进程的内存中进行I/O操作的目标绝对不会是字节数组，因此就有了直接字节缓冲区的概念，也是I/O
的最佳选择

如果您向一个通道中传递一个非直接`ByteBuffer`，对象用于写入，通道可能会在每次调用中隐含地进行下面的操作：
1. 创建一个临时的直接`ByteBuffer`对象。
2. 将非直接缓冲区的内容复制到临时缓冲中。
3. 使用临时缓冲区执行低层次 I/O 操作。
4. 临时缓冲区对象离开作用域，并最终成为被回收的无用数据。

但创建直接缓冲区会绕开JVM内存堆栈，在JVM之外通过操作系统开辟一段内存空间，因此其开辟的耗费会高于非直接缓冲区
具体的性能权衡取决于JVM 操作系统，因此“不要过早优化，先使其工作，再考虑优化”
- static allocateDirect():ByteBuffer
任何`wrap()`方法都是创建非直接缓冲区，ByteBuffer 是唯一可以被直接分配的类型

### 视图缓冲区
ByteBuffer 根据当前position和limit创建视图，新的缓冲区的容量是字节缓冲区中存在的元素数量除以视图类型中
组成一个数据类型的字节数

一旦得到了视图缓冲区，可以用duplicate()，slice()和asReadOnlyBuffer()方法创建进一步的子视图

### 数据元素试图
ByteBuffer提供get/put基础数据类的方法，从position出发到limit位置的符合该类型的字节数，取出或放入包装
或解包装的元素，位置不足则抛出异常，当然包装和解包装还与字节顺序有关，BIG_ENDIAN LITTLE_ENDIAN

