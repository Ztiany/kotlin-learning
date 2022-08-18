package me.ztiany.kotlin.tool.noarg;

import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public interface Storage {

    @Nullable
    <T> T getEntity(String key, Type type);

}