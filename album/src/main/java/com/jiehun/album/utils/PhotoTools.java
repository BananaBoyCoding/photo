/*
 * Copyright (C) 2014 pengjianbo(pengjianbosoft@gmail.com), Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package com.jiehun.album.utils;

import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore;
import android.text.TextUtils;


import com.jiehun.album.vo.AlbumInfo;
import com.jiehun.album.vo.PhotoInfo;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @version 1.0
 * @author Michael
 * @describe:获取相册中的图片
 */
public class PhotoTools {

	//大图遍历字段
	private static final String[] STORE_IMAGES          = {
			MediaStore.Images.Media._ID,
			MediaStore.Images.Media.DATA,
	};
	//小图遍历字段
	private static final String[] THUMBNAIL_STORE_IMAGE = {
			MediaStore.Images.Thumbnails._ID,
			MediaStore.Images.Thumbnails.DATA
	};

	public static List<AlbumInfo> loadAlbums(Context context) {
		final Map<String, AlbumInfo> albums = new HashMap<>();
		//获取大图的游标
		Cursor cursor = context.getContentResolver().query(
				MediaStore.Images.Media.EXTERNAL_CONTENT_URI,  // 大图URI
				STORE_IMAGES,   // 字段
				null,         // No where clause
				null,         // No where clause
				MediaStore.Images.Media.DATE_TAKEN + " DESC"); //根据时间升序
		if (cursor == null)
			return null;
		while (cursor.moveToNext()) {
			int id = cursor.getInt(0);//大图ID
			String path = cursor.getString(1);//大图路径
			File file = new File(path);
			//判断大图是否存在
			if (file.exists()) {
				//小图URI
//				String thumbUri = getThumbnail(context, id);
				//获取大图URI
//				String originalUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build().toString();
				String originalUri = "file://" + path;
				String thumbUri = originalUri;
				if (TextUtils.isEmpty(originalUri))
					continue;

				if (TextUtils.isEmpty(thumbUri))
					thumbUri = originalUri;

				// 获取目录名
				String albumName = file.getParentFile().getName();
				// 图片
				PhotoInfo photo = new PhotoInfo();
				photo.id = id;
				photo.originalUri = originalUri;
				photo.thumbnailUri = thumbUri;
				//判断文件夹是否已经存在
				if (albums.containsKey(albumName)) {
					albums.get(albumName).photos.add(photo);
				} else {
					AlbumInfo albumInfo = new AlbumInfo();
					List<PhotoInfo> photos = new ArrayList<>();
					photos.add(photo);
					albumInfo.albumName = albumName;
					albumInfo.coverImageUri = photo.originalUri;
					albumInfo.photos = photos;
					albums.put(albumName, albumInfo);
				}
			}
		}
		cursor.close();
		return new ArrayList<>(albums.values());
	}

	private static String getThumbnail(Context context, int id) {
		String uri = "";
		Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnail(context.getContentResolver(), id, MediaStore.Images.Thumbnails.MICRO_KIND, null);
		if (cursor != null && cursor.getCount() > 0) {
			cursor.moveToFirst();//**EDIT**
			uri = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Thumbnails.DATA));
		}
		cursor.close();
		return uri;
	}
}
