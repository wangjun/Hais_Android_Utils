package pw.hais.utils.http.imageloader;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.os.Environment;

import pw.hais.utils.L;
import pw.hais.utils.ScreenUtil;
import pw.hais.utils.UtilConfig;


/**
 * 本地图片管理工具类
 * 用于网络图片的本地存/取
 * @author Hello_海生
 * @date 2015年4月2日
 */
public class LocalImageManager {
	private static final String tag = "图片管理";
	private static final Config BITMAP_CONFIG = ImageLoaders.BITMAP_CONFIG;
	private static final int PNG_COMPRESS = UtilConfig.PNG_COMPRESS;// 存入SD卡时，PNG的压缩率
	private static final int JPG_COMPRESS = UtilConfig.JPG_COMPRESS;// 存入SD卡时，JPG的压缩率
	private static final String IMAGE_CACHE_DIR = ImageLoaders.IMAGE_CACHE_DIR;	//缓存目录

	/** 判断URL对应是否有本地图片 */
	public static boolean imagesIsCache(String url) {
		String filename = convertUrlToFileName(url);
		File file = new File(IMAGE_CACHE_DIR, filename);
		if (file.exists())return true;
		return false;
	}

	/** 根据URL 从SD卡读取 缓存 图片 */
	public static Bitmap getImageFromSDCache(String url) {
		return getImageFromSDCache(url, 0, 0);
	}

	/** 根据URL 从SD卡读取 指定  宽度的 缓存 图片 */
	public static Bitmap getImageFromSDCache(String url, int maxWidth, int maxHeight) {
		// SD卡是否有挂起，并具有读写权限
		if (!ImageLoaders.SD_IS_WIRTE) return null;
		String filename = convertUrlToFileName(url);	//根据url生成文件名
		// 获取应用图片缓存路径
		String path = IMAGE_CACHE_DIR + "/" + filename;
		return getImageFromSDCard(path, maxWidth, maxHeight);
	}

	/** 根据本地路径 从SD卡读取 图片 */
	public static Bitmap getImageFromSDCard(String path, int maxWidth, int maxHeight) {
		if (!ImageLoaders.SD_IS_WIRTE) return null;
		try {
			if (new File(path).exists()) {
				return decodeBitmapFile(path, maxWidth, maxHeight);
			}
		} catch (Exception e) {
			L.i(tag, "读取" + path + "图片出错！");
		}
		return null;
	}

	/** 通过绝对路径path,获取内存/SD卡中的图片 */
	public static Bitmap getLocalImageBitmap(String path, int maxWidth, int maxHeight) {
		Bitmap bitmap = ImageLoaders.getBitmapCache(path);	//根据URL 获取缓存
		if (bitmap == null) {
			bitmap = getImageFromSDCard(path, maxWidth, maxHeight);	//从内存卡获取
			if (bitmap != null) {
				ImageLoaders.putBitmapCache(path, bitmap);	//存入缓存
			}
		}
		return bitmap;
	}

	/**读取文件 解码 成 Bitmap*/
	public static Bitmap decodeBitmapFile(String path, int maxWidth, int maxHeight) {
		BitmapFactory.Options opt = new BitmapFactory.Options();

		opt.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, opt);
		if (maxWidth > 0 && maxWidth > 0) {
			opt.inSampleSize = computeSampleSize(opt, -1, maxWidth * maxHeight);
		} else {
			opt.inSampleSize = computeSampleSize(opt, -1, (ScreenUtil.getScreenWidth() * 2 / 3) * (ScreenUtil.getScreenHeight() * 2 / 3));
		}

		opt.inPreferredConfig = BITMAP_CONFIG;
		opt.inPurgeable = true;
		opt.inInputShareable = true;
		opt.inTempStorage = new byte[8 * 1024];
		opt.inJustDecodeBounds = false;
		L.i(tag, "读取图片：" + path);
		return BitmapFactory.decodeFile(path, opt);
	}



	/**
	 * 保存图片到 SD 卡
	 * @param bitmap
	 * @param url
	 * @param folderPath 文件存放目录
	 * @param context
	 */
	public static void saveBmpToSd(Bitmap bitmap, String url, String folderPath,Context context) {
		if (bitmap == null)return;
		if (!ImageLoaders.SD_IS_WIRTE)return; 		// SD卡是否有挂起，并具有读写权限

		String filename = convertUrlToFileName(url);// 获取文件名
		String dir = folderPath;					// 获取存放目录
		File dirFile = new File(dir);
		File file = new File(dir, filename);
		try {
			if (!dirFile.exists())dirFile.mkdirs();	//目录不存在，则创建
			if (file.exists())return;	//文件存在则结束

			file.createNewFile();	//创建文件
			//保存 图片 到 文件
			BufferedOutputStream outStream = new BufferedOutputStream(new FileOutputStream(file));
			if (url.indexOf(".png") >= 0) {
				/* 720P以下手机内存小，固压缩比例增大防止OOM */
				if (ScreenUtil.getScreenWidth() < 720) {
					bitmap.compress(Bitmap.CompressFormat.PNG, PNG_COMPRESS - 10, outStream);
				} else {
					bitmap.compress(Bitmap.CompressFormat.PNG, 100, outStream);
				}
			} else {
				if (ScreenUtil.getScreenWidth() < 720) {
					bitmap.compress(Bitmap.CompressFormat.JPEG, JPG_COMPRESS - 10, outStream);
				} else {
					bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outStream);
				}
			}
			outStream.flush();
			outStream.close();
			L.i(tag, "已存SD:"+filename);
		} catch (FileNotFoundException e) {
			L.i(tag, "图片文件写入SD出错"+filename);
		} catch (IOException e) {
			L.i(tag, "图片写入处理除错"+filename);
			e.printStackTrace();
		}
	}
	
	/** 将bitmap存储到SD卡 */
	public static void saveBmpToSd(Bitmap bm, String url, Context context) {
		saveBmpToSd(bm, url, IMAGE_CACHE_DIR, context);
	}

	/** 根据url生成文件名	 */
	public static String convertUrlToFileName(String url) {
		int index = url.lastIndexOf("/");
		if (index != -1) {
			String name = url.substring(index);
			if (name.lastIndexOf(".jpg") == -1 && name.lastIndexOf(".png") == -1 && name.lastIndexOf(".jpeg") == -1) {
				name = name.replace("\\", "").replace("/", "").replace(":", "").replace("*", "").replace("?", "").replace("\"", "").replace("<", "")
						.replace(">", "").replace("|", "")
						+ ".jpg";
			}
			return name;
		} else return url;
	}


	/** 获取文件夹大小 */
	public static float getFolderSize(File file) {
		float size = 0;
		File[] fileList = file.listFiles();
        if(fileList==null){
            return 0.00f;
        }
		for (int i = 0; i < fileList.length; i++) {
			if (fileList[i].isDirectory()) {
				size = size + getFolderSize(fileList[i]);
			} else {
				size = size + fileList[i].length();
			}
		}
		return size / 1048576;
	}

	/**
	 * 计算压缩率
	 * 
	 * @param options
	 * @param minSideLength
	 * @param maxNumOfPixels
	 * @return
	 */
	public static int computeSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		int initialSize = computeInitialSampleSize(options, minSideLength, maxNumOfPixels);

		int roundedSize;
		if (initialSize <= 8) {
			roundedSize = 1;
			while (roundedSize < initialSize) {
				roundedSize <<= 1;
			}
		} else {
			roundedSize = (initialSize + 7) / 8 * 8;
		}
		return roundedSize;
	}

	public static int computeInitialSampleSize(BitmapFactory.Options options, int minSideLength, int maxNumOfPixels) {
		double w = options.outWidth;
		double h = options.outHeight;
		int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math.sqrt(w * h / maxNumOfPixels));
		int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math.floor(w / minSideLength), Math.floor(h / minSideLength));

		if (upperBound < lowerBound) {
			// return the larger one when there is no overlapping zone.
			return lowerBound;
		}

		if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
			return 1;
		} else if (minSideLength == -1) {
			return lowerBound;
		} else {
			return upperBound;
		}
	}

	/** 清空本地缓存 */
	public static boolean cleanLocalCache() {
		File dir = new File(IMAGE_CACHE_DIR);
		boolean b=deleteDir(dir);
		File file = new File(IMAGE_CACHE_DIR);
		if (!file.exists()) {
			file.mkdirs();
		}
		return b;
	}

	/** 清空制定目录下的缓存 */
	public static boolean cleanLocalCache(String dirs) {
		File dir = new File(dirs);
		boolean b=deleteDir(dir);
		
		if (!dir.exists()) {
			dir.mkdirs();
		}
		return b;
	}

	/** 递归删除文件 */
	private static boolean deleteDir(File dir) {
		if (dir.isDirectory()) {
			String[] children = dir.list();
			// 递归删除目录中的子目录下
			for (int i = 0; i < children.length; i++) {
				boolean success = deleteDir(new File(dir, children[i]));
				if (!success) {
					return false;
				}
			}
		}
		// 目录此时为空，可以删除
		return dir.delete();
	}
}
