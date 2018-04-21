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
	 * @return
	 */
	public static File compress(File fileOrDir, String basePath) throws IOException {
		if(basePath == null) {
			logger.debug("basePath不能为空，basePath=" + basePath);
		}
		
		File outFile = new File(basePath, fileOrDir.getName() + ".zip");
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
				//ignore
			}
			
		}
		
		return outFile;
		
	}
	
	/**
	 * 压缩条目
	 * @param zipOutputStream
	 * @param fileOrDir
	 * @param beforeEntry
	 * @throws IOException
	 */
	private static void compressToEntry(ZipOutputStream zipOutputStream, File fileOrDir, ZipEntry beforeEntry) throws IOException{
		if(fileOrDir.isDirectory()) {
			File[] files = fileOrDir.listFiles();
			for(File file : files) {
				ZipEntry nextEntry  = null;
				if(beforeEntry != null) {
					nextEntry = new ZipEntry(beforeEntry.getName() + File.separator + file.getName());
				}else {
					nextEntry = new ZipEntry(file.getName());
				}
				compressToEntry(zipOutputStream, file, nextEntry);
			}
		}else {
			if(beforeEntry == null) {
				beforeEntry = new ZipEntry(fileOrDir.getName());
			}
			
			zipOutputStream.putNextEntry(beforeEntry);
			FileInputStream inputStream = new FileInputStream(fileOrDir);
			byte[] buffer = new byte[512];
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
	 * @return
	 */
	public static File uncompress(File zipFile,  String basePath) throws IOException {
		if(basePath == null) {
			logger.debug("basePath不能为空，basePath=" + basePath);
		}
		
		String folderName = zipFile.getName().replaceAll("\\.zip$", "");
		File outDir = new File(basePath, folderName);
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
	
}
