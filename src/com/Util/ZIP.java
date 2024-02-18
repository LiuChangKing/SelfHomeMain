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
	            
	            // �ݹ���������ļ��в���ӵ�ѹ����
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
	     * ���ļ��м����е��ļ��ݹ���ӵ�ѹ������
	     *
	     * @param parentPath ����·��
	     * @param folder �ļ���
	     * @param zos Zip�����
	     * @throws FileNotFoundException �ļ�δ�ҵ��쳣
	     * @throws IOException IO�쳣
	     */
	    private static void addFolderToZip(String parentPath, File folder, ZipOutputStream zos) throws FileNotFoundException, IOException {
	        for (File file : folder.listFiles()) {
	            if (file.isDirectory()) {
	                // �ݹ�������ļ����е��ļ�
	                addFolderToZip(parentPath + folder.getName() + "/", file, zos);
	            } else {
	                FileInputStream fis = null;
	                try {
	                    fis = new FileInputStream(file);
	                    
	                    // �½�Zip��Ŀ�������������뵽Zip����
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


