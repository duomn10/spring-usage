package org.springusage.mybatis.generator.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

public class FileUtils {

	/**
	 * 获取子文件相对于父目录的相对路径
	 * @param baseDir	父目录
	 * @param subFile	子文件
	 * @param seperator  指定的目录分隔符
	 * @return 
	 */
	public static String getRelativePath(File baseDir, File subFile, String seperator) {
		String basePath = baseDir.getAbsolutePath();
		String filePath = subFile.getAbsolutePath();
		if (!filePath.startsWith(basePath)) {
			return null;
		}
		String relativePath = filePath.substring(basePath.length() + 1);
		return seperator == null ? relativePath : relativePath.replaceAll("\\\\", seperator).replaceAll("/", seperator);
	}
	
	/**
	 * 获取子文件相对于父目录的相对路径
	 * @param baseDir 父目录
	 * @param file 子文件
	 * @return
	 */
	public static String getRelativePath(File baseDir, File file) {
		return getRelativePath(baseDir, file, null);
	}
	
	/**
	 * 列出一个目录下的所有子文件，使用指定正则表达式过滤
	 * @param baseFile 目录
	 * @param regex 过滤的文件名正则表达式
	 * @return 文件列表，没有符合的则返回null
	 */
	public static List<File> listAllFiles(File baseFile, String regex) {
		List<File> result = new LinkedList<File>();
		if (baseFile.isDirectory()) {
			recursionDir(result, baseFile, regex);
		} else if (baseFile.isFile()) {
			result.add(baseFile);
		}
		return result.isEmpty() ? null : result;
	}
	
	/**
	 * 列出一个目录下的所有子文件
	 * @param baseFile 目录
	 * @return 文件列表，没有符合的返回null
	 */
	public static List<File> listAllFiles(File baseFile) {
		return listAllFiles(baseFile, null);
	}
	
	/** 递归一个目录 */
	private static void recursionDir(List<File> result, File dir, final String regex) {
		File[] fs = dir.listFiles(new FileFilter() {
			public boolean accept(File file) {
				if (regex == null || file.isDirectory()) return true;
//				System.out.println(regex + "|------|" + file.getName());
				int index = file.getName().indexOf(".");
				String fileName = (index == -1 ? file.getName() : file.getName().substring(0, file.getName().indexOf("."))); 
				return Pattern.matches(regex, fileName);
			}
		});
		if (fs == null) return;
		for (File f : fs) {
			if (f.isFile()) {
				result.add(f);
				continue;
			}
			recursionDir(result, f, regex);
		}
	}
	
	/**
	 * 读取文件，使用文件名作参数，默认的换行符为"\r\n"
	 * @param file
	 * @return
	 */
	public static String readFile(String file) {
		return readFile(file, null);
	}
	
	/**
	 * 读取文件，使用文件名作参数
	 * @param file
	 * @param lineBreak
	 * @return
	 */
	public static String readFile(String file, String lineBreak) {
		File f = new File(file);
		return readFile(f, lineBreak);
	}
	
	/**
	 * 读取文件，使用默认换行符"\r\n"
	 * @param file
	 * @return
	 */
	public static String readFile(File file) {
		return readFile(file, null);
	}

	/**
	 * 读取文件返回字符串
	 * @param file 文件
	 * @param lineBreak 换行符
	 * @return
	 */
	public static String readFile(File file, String lineBreak) {
		String lb = lineBreak == null ? "\r\n" : lineBreak;
		FileInputStream fis = null;
		InputStreamReader isr = null;
		BufferedReader br = null;
		StringBuilder buf = new StringBuilder();
		try {
			fis = new FileInputStream(file);
			isr = new InputStreamReader(fis);
			br = new BufferedReader(isr);
			String line = null;
			while ((line = br.readLine()) != null) {
				buf.append(line).append(lb);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (isr != null) {
				try {
					isr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
		return buf.toString();
	}
	
	/**
	 * 向文件中写入多个字符串，换行符用"\r\n";
	 * @param output
	 * @param content
	 * @return
	 */
	public static boolean writeToFile(File output, List<String> content){
		String[] input = content.toArray(new String[content.size()]);
		return writeToFile(output, input);
	}
	
	/**
	 * 向文件中写入多个字符串，换行符用"\r\n"
	 * @param output
	 * @param content
	 * @return
	 */
	public static boolean writeToFile(File output, String[] content) {
		String input = StringUtils.arrayJoin(content, "\r\n");
		return writeToFile(output, input, null);
	}
	
	public static boolean writeToFile(File output, String content) {
		return writeToFile(output, content, null);
	}
	
	/**
	 * 把字符串写入文件，并用"\r\n" 替换掉特殊的换行符
	 * @param output
	 * @param content 字符串
	 * @param linkBreak  字符串中指定的换行符
	 * @return
	 */
	public static boolean writeToFile(File output, String content, String linkBreak) {
		String input = linkBreak != null ? content.replaceAll(linkBreak, "\r\n") : content;
		
		if (!output.exists()) { // 如果文件不存在，检验并创建父级目录
			String dir = output.getParent();
			File dirFile = new File(dir);
			if (!dirFile.exists() || !dirFile.isDirectory()) {
				dirFile.mkdirs();
			}
		}
		
		FileOutputStream fos = null;
		OutputStreamWriter osw = null;
		BufferedWriter bw = null;
		
		try {
			fos = new FileOutputStream(output);
			osw = new OutputStreamWriter(fos);
			bw = new BufferedWriter(osw);
			bw.write(input);
			bw.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		} finally {
			if(bw != null) {
				try {
					bw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(osw != null) {
				try {
					osw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if(fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void deleteDirectory(String dir) {
		File dirFile = new File(dir);
		deleteDirectory(dirFile);
	}
	
	public static void deleteDirectory(File dir) {
		File[] files = dir.listFiles();
		if (files == null || files.length == 0) { // 空文件夹
			dir.delete();
			return;
		}
		for (File f : files) { // 非空先删除下面的文件，再删除外层文件夹
			if (f.isFile()) {
				f.delete();
			} else {
				deleteDirectory(f);
			}
		}
		dir.delete();
	}
	
	public static void mkdirs(File file) {
		if (!file.exists()) {
			file.mkdirs();
		}
	}
	
	public static void mkdirs(String path) {
		File dirs = new File(path);
		if (!dirs.exists()) {
			dirs.mkdirs();
		}
	}
}
