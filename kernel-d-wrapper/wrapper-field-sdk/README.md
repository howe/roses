# 字段转化模块

通过json的序列化定制，将ID或者枚举值相关的字段，转化成可以前段直接读懂的名称

## 示例1

http响应时，转化userId为用户的名字：

Long userId; -> "张三"

## 示例2

http响应时，转化status为状态的名字：

Integer status; -> "启用"

## 示例3

http响应时，转化枚举为对应的名称：

StatusEnum status; -> "禁用"

## 示例4

http响应时，转化一组数组：

List<Long> userIdList; -> ["张三","李四","王五"]

## 其他拓展

只要按接口规范，可以拓展任何相关值的转化