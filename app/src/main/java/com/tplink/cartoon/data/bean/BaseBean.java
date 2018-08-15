/*
 * Description
 *
 * Author xufeng
 *
 * Ver 1.0, 18-7-27, xufeng, Create file
 */

package com.tplink.cartoon.data.bean;

import java.io.Serializable;

public class BaseBean implements Serializable {
    /**
     * @Entity：告诉GreenDao该对象为实体，只有被@Entity注释的Bean类才能被dao类操作
     *   @Id：对象的Id，使用Long类型作为EntityId，否则会报错。(autoincrement = true)表示主键会自增，如果false就会使用旧值
     *  @Property：可以自定义字段名，注意外键不能使用该属性
     * @NotNull：属性不能为空
     * @Transient：使用该注释的属性不会被存入数据库的字段中
     *@Unique：该属性值必须在数据库中是唯一值
     * @Generated：编译后自动生成的构造函数、方法等的注释，提示构造函数、方法等不能被修改
     */

}
