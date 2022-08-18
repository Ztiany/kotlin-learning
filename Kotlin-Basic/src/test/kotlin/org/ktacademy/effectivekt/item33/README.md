# Effective Kotlin Item 33: Consider factory functions instead of constructors

## Reference

- [Consider factory functions instead of constructors【Old Version】](https://blog.kotlin-academy.com/effective-java-in-kotlin-item-1-consider-static-factory-methods-instead-of-constructors-8d0d7b5814b2)
- [Consider factory functions instead of constructors【Old Version】【Translation】](https://juejin.cn/post/6844903662922235918)
- [Consider factory functions instead of constructors【Latest】](https://kt.academy/article/ek-factory-functions)

## Content

- CompanionFactory
- TopFunctionFactory
- FakeConstructor

## Summary

虽然 Kotlin 在对象创建方面做了很多改变，但是 Effective Java 中有关静态工厂方法的争论仍然是最火的。改变的是 Kotlin 排除了静态成员方法，而是我们可以使用如下具有SFM优势的替代方法:

-  伴生对象工厂方法
- 顶层函数
- 伪构造器
- 扩展工厂方法

它们中的每一个都是在不同的需求场景下使用，并且每个都具有与 Java SFM 不同的优点。一般规则是，在大多数情况下，我们创建对象所需的全部是主构造器，默认情况下连接到类结构和创建。当我们需要其他构建方法时，我们应该最有可能使用一些SFM替代方案。
