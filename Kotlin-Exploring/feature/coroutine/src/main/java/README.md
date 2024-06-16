# Kotlin 协程学习

Kotlin 协程以包括以下模块

- common：common coroutines across all backends
- core：kotlin 协程核心基础模块
- reactive：为各种响应式库(RxJava等)提供构建器和迭代器
- ui 模块
- integration 模块

具体参考 [kotlinx.coroutines](https://github.com/Kotlin/kotlinx.coroutines)。

---

## 1 示例代码分包说明

官方模块：

- core：kotlin 协程官方文档 core 模块示例代码
- reactive：kotlin 协程官方文档 reactive 模块示例代码
- rxjava2：kotlin 协程官方文档 rxjava 模块示例代码

扩展学习：

- concept：帮助理解 kotlin 协程
- retrofit：kotlin 协程与 retrofit 配合示例代码
- analyse： [破解 kotlin 协程系列 ](https://juejin.im/post/5ceb423451882533441ece67) 学习代码
- book.dukc：《深入理解 Kotlin 协程》学习代码

---

## 2 协程 Core 模块

### 协程基础（core.base）

- 连接阻塞和非阻塞世界
- 等待一个Job
- Coroutines是轻量级的
- Coroutines类似守护线程
    
### 撤销和超时（core.cancel_timeouts）

- 取消协程执行
- 创建可以取消的计算任务
- 在finally中释放资源
- 设置协程超时
    
### 组合暂停函数（core.functions）

- 顺序执行的协程
- 使用async并发执行——带返回值的协程Deferred
- 懒执行
- 编写异步风格的协程函数
    
### 调度器（core.context_dispatchers）

- 调度器于线程
- 无限制的dispatcher vs confined(限制的) dispatcher
- 如何调试协程
- 在不同的线程中跳转
- Job in the context(Job是context的属性)
- 协程与子协程
- 组合多个协程contexts
- 给协程命名
- 明确的取消Job

### 异步流（core.flow）

- 选择表达式(还不是很理解)

### 通道（core.channels）

- 通道
- 生产者消费者
- 管道
- 通道是公平的

### 并发（core.concurrency）

- 写成也是线程不安全的
- 线程控制——细粒度
- 线程控制——粗粒度
- 互斥
- Actor

### 选择表达式（core.select）

- 选择表达式(还不是很理解)

---

## 3 协程 Reactive 模块

- [ ] todo
