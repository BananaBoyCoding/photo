/*******************************************************************************
 * Copyright 2011-2014 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.robin.lazy.cache.util.diskcache;

import android.content.Context;

import com.jiehun.component.utils.AbLazyLogger;
import com.robin.lazy.cache.disk.DiskCache;
import com.robin.lazy.cache.disk.impl.BaseDiskCache;
import com.robin.lazy.cache.disk.naming.FileNameGenerator;
import com.robin.lazy.cache.disk.naming.HashCodeFileNameGenerator;
import com.robin.lazy.util.StorageUtils;

import java.io.File;
import java.io.IOException;

/**
 * Utility for convenient work with disk cache.<br />
 * <b>NOTE:</b> This utility works with file system so avoid using it on
 * application main thread.
 *
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 * @since 1.8.0
 */
public abstract class IDiskCacheUtils {

	/**
	 * Creates {@linkplain HashCodeFileNameGenerator default implementation} of
	 * FileNameGenerator
	 */
	public FileNameGenerator createFileNameGenerator() {
		return new HashCodeFileNameGenerator();
	}

	/**
	 * Creates default implementation of {@link DiskCache} depends on incoming
	 * parameters
	 * @param diskCacheSize 可使用磁盘最大大小(单位byte,字节)
	 */
	public DiskCache createDiskCache(Context context,
											FileNameGenerator diskCacheFileNameGenerator, long diskCacheSize,
											int diskCacheFileCount) {
		File reserveCacheDir = createReserveDiskCacheDir(context);
		if (diskCacheSize > 0 || diskCacheFileCount > 0) {
			File individualCacheDir = getIndividualCacheDirectory(context);
			try {
				return getDiskCache(individualCacheDir, reserveCacheDir,
						diskCacheFileNameGenerator, diskCacheSize,
						diskCacheFileCount);
			} catch (IOException e) {
				AbLazyLogger.e(e, "获取LruDiskCache错误");
				// continue and create unlimited cache
			}
		}
		File cacheDir = StorageUtils.getCacheDirectory(context);
		return new BaseDiskCache(cacheDir, reserveCacheDir,
				diskCacheFileNameGenerator);
	}

	public abstract DiskCache getDiskCache(File individualCacheDir, File reserveCacheDir, FileNameGenerator diskCacheFileNameGenerator, long diskCacheSize,
										   int diskCacheFileCount) throws IOException;

	public abstract File getCacheDirectory(Context context);

	/**
	 * Creates reserve disk cache folder which will be used if primary disk
	 * cache folder becomes unavailable
	 */
	protected abstract File createReserveDiskCacheDir(Context context);


	protected abstract File getIndividualCacheDirectory(Context context);
	/**
	 * Returns {@link File} of cached key or <b>null</b> if key was not cached
	 * in disk cache
	 */
	public File findInCache(String key, DiskCache diskCache) {
		File file = diskCache.getFile(key);
		return file != null && file.exists() ? file : null;
	}

	/**
	 * Removed cached key file from disk cache (if key was cached in disk cache
	 * before)
	 *
	 * @return <b>true</b> - if cached key file existed and was deleted;
	 *         <b>false</b> - otherwise.
	 */
	public boolean removeFromCache(String key, DiskCache diskCache) {
		File file = diskCache.getFile(key);
		return file != null && file.exists() && file.delete();
	}
}
