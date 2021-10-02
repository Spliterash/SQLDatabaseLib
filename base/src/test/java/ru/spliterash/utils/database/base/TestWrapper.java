package ru.spliterash.utils.database.base;

import lombok.Getter;
import lombok.Setter;
import ru.spliterash.utils.database.base.utils.SQLProperty;

@Getter
@Setter
public class TestWrapper {
    private int number;
    private String str;
    private double flt;
    private boolean bln;

    @Setter(onMethod = @__(@SQLProperty("custom_name")))
    private String randomProperty;
}
