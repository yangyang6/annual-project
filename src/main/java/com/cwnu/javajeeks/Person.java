package com.cwnu.javajeeks;
import lombok.*;
/**
 * Created by rey on 2018/1/20.
 * 如果一个构造函数或工厂模式拥有太多的可选参数，那么Builder模式是一个很好的选择。
 * 是哦，这是一种好的思想额~  创建对象的内容一目了然
 *
 * 这种其实是在需要使用不同的构造方法的时候最有用，一般我们是多个同样的构造方法，所以如果参数一多，builder起来也比较麻烦
 */

@Builder
@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Person {

    @NonNull
    private final String lastName;

    @NonNull
    private final String firstName;

    @NonNull
    private final Integer age;

    private static Person JOHN = Person.builder().lastName("yang").firstName("li").age(25).build();
    public static void main(String[] args) {
        System.out.println(JOHN);
    }
}
