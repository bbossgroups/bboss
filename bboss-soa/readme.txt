#######update function list since bbossgroups-3.4 begin###########
o 引进序列化机制，对于static，final，transient类型的属性不进行序列化，对于添加了@ExcludeField的注解不序列化
对象中序列化的字段不再需要get/set方法
#######update function list since bbossgroups-3.2 begin###########
o 实现对象与xml相互转化功能
o 性能优化策略
properties转换为ps
property转换为p
name转换为n
value转换为v
class转换为cs

list转换为l
array转换为a
map转换为m
set转换为s


soa:type_null_value转换为s:nvl
soa:type转换为s:t
componentType转换为cmt

o 修复set转换问题
o 修复枚举类型转换问题


