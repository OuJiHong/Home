package com.cq.home.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 *提供文件压缩功能 
 * @author Administrator
 * 2018年4月20日 下午11:39:33
 *
 */
public class ZipUtils {
	
	private static Logger logger  = LoggerFactory.getLogger(ZipUtils.class);
	
	
	/**
	 * 压缩
	 * @param fileOrDir
	 * @param optionDestFolder 可选的目标文件夹
	 * @return
	 */
	public static File compress(File fileOrDir, File optionDestFolder) throws IOException {
		if(optionDestFolder == null) {
			optionDestFolder = fileOrDir.getAbsoluteFile().getParentFile();
		}
		
		File outFile = new File(optionDestFolder, fileOrDir.getName() + ".zip");
		if(!outFile.getParentFile().exists()) {
			outFile.getParentFile().mkdirs();
		}
		ZipOutputStream zipOutputStream = new ZipOutputStream(new FileOutputStream(outFile));
		try {
			compressToEntry(zipOutputStream, fileOrDir, null);
		}finally {
			try {
				zipOutputStream.close();
			}catch(Exception e) {
				logger.error("compress close error", e);
			}
			
		}
		
		return outFile;
		
	}
	
	
	/**
	 * 重载的压缩方法
	 * @param fileOrDir
	 * @return
	 * @throws IOException
	 */
	public static File compress(File fileOrDir) throws IOException {
		return compress(fileOrDir, null);
	}
	
	/**
	 * 压缩条目
	 * @param zipOutputStream
	 * @param fileOrDir
	 * @param beforeEntry
	 * @throws IOException
	 */
	private static void compressToEntry(ZipOutputStream zipOutputStream, File fileOrDir, ZipEntry beforeEntry) throws IOException{
		ZipEntry nextEntry  = null;
		if(beforeEntry != null) {
			nextEntry = new ZipEntry(beforeEntry.getName() + "/" + fileOrDir.getName());
		}else {
			nextEntry = new ZipEntry(fileOrDir.getName());
		}
		
		
		if(fileOrDir.isDirectory()) {
			File[] files = fileOrDir.listFiles();
			for(File file : files) {
				compressToEntry(zipOutputStream, file, nextEntry);
			}
		}else {
			//定位流的输出位置
			zipOutputStream.putNextEntry(nextEntry);
			FileInputStream inputStream = new FileInputStream(fileOrDir);
			byte[] buffer = new byte[4096];
			int len = -1;
			try {
				while((len = inputStream.read(buffer)) != -1) {
					zipOutputStream.write(buffer, 0, len);
				}
			}finally {
				try {
					inputStream.close();
				}catch(Exception e) {
					logger.error("compress close error", e );
				}
			}
			
		}
		
		
	}
	
	/**
	 * 解压
	 * @param zipFile
	 * @param optionDestFolder 目标文件夹,可选
	 * @return
	 */
	public static File uncompress(File zipFile,  File optionDestFolder) throws IOException {
		
		if(optionDestFolder == null) {
			optionDestFolder = zipFile.getAbsoluteFile().getParentFile();
		}
		
		String folderName = zipFile.getName().replaceAll("\\.zip$", "");
		File outDir = new File(optionDestFolder, folderName);
		if(outDir.exists() && outDir.isFile()) {
			outDir = new File(optionDestFolder, folderName + "_" +  System.currentTimeMillis());//解决文件名冲突
		}
		
		if(!outDir.exists()) {
			outDir.mkdirs();
		}
		ZipInputStream zipInputStream = new ZipInputStream(new FileInputStream(zipFile));
		ZipEntry zipEntry = null;
		byte[] buffer = new byte[512];
		int len = -1;
		while((zipEntry = zipInputStream.getNextEntry()) != null) {
			File entryOut = new File(outDir, zipEntry.getName());
			if(!entryOut.getParentFile().exists()) {
				entryOut.getParentFile().mkdirs();
			}
			
			FileOutputStream out = new FileOutputStream(entryOut);
			try {
				while((len = zipInputStream.read(buffer)) != -1) {
					out.write(buffer, 0, len);
				}
			}finally {
				try {
					out.close();
				}catch(Exception e) {
					logger.error("uncompress close error", e );
				}
			}
		}
		//关闭
		zipInputStream.close();
		
		return outDir;
	}
	
	/**
	 * 重载的解压缩方法
	 * @param zipFile
	 * @return
	 * @throws IOException
	 */
	public static File uncompress(File zipFile) throws IOException {
		return uncompress(zipFile);
	}
	
	/**
	 * 复制数据到目标
	 * @param source
	 * @param target
	 */
	public static void transferData(File source, OutputStream out) throws IOException {
		FileInputStream input = new FileInputStream(source);
		try {
			byte[] buff = new byte[4096];
			int count;
			while ((count = input.read(buff)) != -1) {
				if (count > 0) {
					out.write(buff, 0, count);
				}
			}
		}finally {
			try {
				input.close();
			}catch(Exception e) {
				logger.error("transferData close error", e);
			}
		}
	}
	
	/**
	 * 复制文件或目录
	 * @param source  文件或目录
	 * @param destFile 文件或目录,如果是目录，则复制到目录之中
	 * @return 返回复制结果
	 */
	public static File copy(File source, File destFile) throws IOException {
		
		if(!source.exists()) {
			throw new IllegalArgumentException("source 文件或目录不存在");
		}
		
		if(destFile.exists() && destFile.isDirectory()) {
			destFile = new File(destFile, source.getName());//产生同名文件或目录
		}
		
		//来源是否是目录
		if(source.isDirectory()) {
			destFile.mkdir();//生成当前目录，不创建父级目录
			//复制子文件
			File[] files = source.listFiles();
			for(File file : files) {
				copy(file, destFile);
			}
			
			return destFile;
		}
		
		//--------------复制文件------------
		FileInputStream input = new FileInputStream(source);
		FileOutputStream out = new FileOutputStream(destFile);
		
		try {
			byte[] buff = new byte[4096];
			int count;
			while ((count = input.read(buff)) != -1) {
				if (count > 0) {
					out.write(buff, 0, count);
				}
			}
		}finally {
			try {
				input.close();
				out.close();
			}catch(Exception e) {
				logger.error("copy close error", e);
			}
		}
		
		return destFile;
	}
	
	/**
	 * 删除文件
	 * @param fileOrDir
	 */
	public static void deleteQuietly(File fileOrDir) {
		if(fileOrDir == null || !fileOrDir.exists()) {
			return;
		}
		
		if(fileOrDir.isDirectory()) {
			File[] files = fileOrDir.listFiles();
			//clean child
			for(File file : files) {
				deleteQuietly(file);
			}
		}
		
		boolean flag = fileOrDir.delete();
		if(!flag) {
			logger.debug("file delete fail - " + fileOrDir.getPath());
		}
		
	}

	
}
