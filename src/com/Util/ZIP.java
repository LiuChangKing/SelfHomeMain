package com.Util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZIP
{
	 public static void zipFolder(String folderPath, String zipFilePath) throws IOException {
	        FileOutputStream fos = null;
	        ZipOutputStream zos = null;
	        try {
	            fos = new FileOutputStream(zipFilePath);
	            zos = new ZipOutputStream(fos);
	            
	            // 递归遍历整个文件夹并添加到压缩包
	            addFolderToZip("", new File(folderPath), zos);
	        } finally {
	            if (zos != null) {
	                zos.close();
	            }
	            if (fos != null) {
	                fos.close();
	            }
	        }
	    }
	 
	    /**
	     * 将文件夹及其中的文件递归添加到压缩流中
	     *
	     * @param parentPath 父级路径
	     * @param folder 文件夹
	     * @param zos Zip输出流
	     * @throws FileNotFoundException 文件未找到异常
	     * @throws IOException IO异常
	     */
	    private static void addFolderToZip(String parentPath, File folder, ZipOutputStream zos) throws FileNotFoundException, IOException {
	        for (File file : folder.listFiles()) {
	            if (file.isDirectory()) {
	                // 递归添加子文件夹中的文件
	                addFolderToZip(parentPath + folder.getName() + "/", file, zos);
	            } else {
	                FileInputStream fis = null;
	                try {
	                    fis = new FileInputStream(file);
	                    
	                    // 新建Zip条目并将输入流加入到Zip包中
	                    ZipEntry zipEntry = new ZipEntry(parentPath + folder.getName() + "/" + file.getName());
	                    zos.putNextEntry(zipEntry);
	                    
	                    byte[] bytes = new byte[1024];
	                    int length;
	                    while ((length = fis.read(bytes)) >= 0) {
	                        zos.write(bytes, 0, length);
	                    }
	                } finally {
	                    if (fis != null) {
	                        fis.close();
	                    }
	                }
	            }
	        }
	    }


	}


