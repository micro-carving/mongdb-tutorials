# MongoDB 概述

## MongoDB 是什么？

MongoDB 是一个文档数据库，旨在简化应用程序的开发和扩展。

## MongoDB 可以干什么？

- 对数据查询并存储
- 通过聚合函数转换数据
- 保障数据的访问安全
- 部署并扩展数据库

## MongoDB 数据类型 BSON

### BSON 是什么？

BSON 是一种类似 JSON 的一种二进制形式的存储格式，“BSON” 是 “二进制” 和 “JSON” 的合成词，可以将 BSON 看作 JSON (JavaScript Object Notation) 文档的二进制表示。它和 JSON 一样，支持内嵌的文档对象和数组对象，但是 BSON 有 JSON 没有的一些数据类型，如 `Date` 和 `BinData` 类型。

BSON 可以做为网络数据交换的一种存储形式，这个有点类似于 Google 的 Protocol Buffer，但是 BSON 是一种 schema-less 的存储形式，它的优点是灵活性高，但它的缺点是空间利用率不是很理想， BSON 具有三个特点：轻量性、可遍历性、高效性

`{"hello":"world"}` 这是一个 BSON 的例子，其中 "hello" 是 key name，它一般是 `cstring` 类型，字节表示是 `cstring::= (byte*) "/x00"` ,其中 `*` 表示零个或多个 `byte` 字节，`/x00` 表示结束符`;` 后面的 `"world"` 是 `value` 值，它的类型一般是 `string,double,array,binarydata` 等类型。

在 MongoDB 中用于存储文档和进行远程过程调用的序列化格式。BSON 规范位于 [bsonspec.org](http://bsonspec.org/)。
