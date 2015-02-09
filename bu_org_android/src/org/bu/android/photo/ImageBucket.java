package org.bu.android.photo;

import java.io.Serializable;
import java.util.LinkedHashMap;

/**
 * 一个目录的相册对象
 * 
 * @author Administrator
 * 
 */
public class ImageBucket implements Serializable {
	private static final long serialVersionUID = 4562101318279548165L;
	public int count = 0;
	public String bucketName = "";
	public LinkedHashMap<String, ImageItem> imageList = new LinkedHashMap<String, ImageItem>();

}
