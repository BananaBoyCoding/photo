package com.jiehun.component.storage;

import com.robin.lazy.cache.disk.naming.FileNameGenerator;

/**
 * Created by zhangjianlin on 16-9-26.
 * 保存的文件
 */
public class StorageFileNameGenerator implements FileNameGenerator {

    @Override
    public String generate(String imageUri) {
        return imageUri;
    }

}
