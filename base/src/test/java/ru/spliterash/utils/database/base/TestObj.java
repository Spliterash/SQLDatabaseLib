package ru.spliterash.utils.database.base;

import lombok.Getter;
import lombok.Setter;
import ru.spliterash.utils.database.base.utils.SQLProperty;

import java.util.List;

@Getter
@Setter
public class TestObj {
    private String name;
    private String a;
    private boolean b;
    private int c;
    private double d;
    private List<String> f;
    @Getter(onMethod_ = @SQLProperty("g"))
    private String randomValue;
}
