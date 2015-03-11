package org.frameworkset.web.fileupload;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.util.annotations.HandlerMapping;
import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.util.annotations.ResponseBody;
import org.frameworkset.web.multipart.IgnoreFieldNameMultipartFile;
import org.frameworkset.web.multipart.MultipartFile;
import org.frameworkset.web.multipart.MultipartHttpServletRequest;
import org.frameworkset.web.servlet.ModelMap;
import org.frameworkset.web.servlet.handler.annotations.Controller;


@Controller
public class FileController {

	@HandlerMapping("/swfupload/upload.htm")
	public void uploadFile(MultipartHttpServletRequest request,
			ModelMap model, @RequestParam(name = "upload_")
			String upload_) {
		Iterator<String> fileNames = request.getFileNames();
//		System.out.println(upload_);
		List<UpFile> files = new ArrayList<UpFile>();
		File dir = new File("d:/mutifiles/");
		if(!dir.exists())
			dir.mkdirs();
		while (fileNames.hasNext()) {
			String name = fileNames.next();
			MultipartFile file = request.getFile(name);
			String temp = file.getOriginalFilename();

			try {
				file.transferTo(new File("d:/mutifiles/" + temp));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			UpFile uf = new UpFile();
			uf.setFileName(temp);
			uf.setFileType(file.getContentType());

			System.out.println(temp);
			files.add(uf);
		}
		model.addAttribute("files", files);
//		return "";
	}
	@HandlerMapping("/swfupload/svnfileupload.htm")
	public @ResponseBody(datatype="json") UpFile  svnfileupload(IgnoreFieldNameMultipartFile file,HttpServletRequest request) {
		
			
			
			String temp = file.getOriginalFilename();

			try {
				file.transferTo(new File(request.getRealPath("/")+"filesdown/"+temp));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			UpFile uf = new UpFile();
			uf.setFileName(temp);
			uf.setFileType(file.getContentType());

			return uf;
		
		
//		return "";
	}
	/**
	 * http://localhost:8080/bboss-mvc/swfupload/mutifileselects.htm
	 * 
	 * @return
	 */
	@HandlerMapping("/swfupload/mutifileselects.htm")
	public String mutifileselects(ModelMap model) {
		File file = new File("d:/mutifiles/");

		List<UpFile> files = new ArrayList<UpFile>();
		if (file.exists()) {
			File[] fl = file.listFiles();
			for (int i = 0; fl != null && i < fl.length; i++) {
				UpFile uf = new UpFile();
				uf.setFileName(fl[i].getName());
				uf.setFileSize(fl[i].length());
				String type = uf.getFileName().substring(
						uf.getFileName().lastIndexOf("."),
						uf.getFileName().length());

				uf.setFileType(type);
				files.add(uf);
			}
		}
		model.addAttribute("files", files);

		return "files/upload";
	}

	@HandlerMapping("/swfupload/deletefiles.htm")
	public @ResponseBody String deleteFileOpera(
			@RequestParam(name = "fileNames", decodeCharset = "UTF-8")
			String fileNames) throws UnsupportedEncodingException {
		
		// String fileNames = request.getParameter("fileNames");
		// fileNames = URLDecoder.decode(fileNames, "utf-8");

		
		String[] names = fileNames.split(",");

		String backStr = "[";

		for (int i = 0; i < names.length; i++) {
			File file = new File("D:/mutifiles/" + names[i]);
			if (file.exists()) {
				file.delete();
				if (i != names.length - 1)
					backStr = backStr + "{" + "name:'" + names[i] + "'},";
				else
					backStr = backStr + "{" + "name:'" + names[i] + "'}]";
			} else {
				System.out.println(file + "  is not exit");
				
				return null;
			}
		}
		
		
		return backStr;
	}
	
	@HandlerMapping("/swfupload/deleteAllfiles.htm")
	public  String deleteAllFiles() {
		File file = new File("D:/mutifiles/");

		List<UpFile> files = new ArrayList<UpFile>();
		if (file.exists()) {
			File[] fl = file.listFiles();
			for (int i = 0; fl != null && i < fl.length; i++) {
				UpFile uf = new UpFile();
				uf.setFileName(fl[i].getName());
				uf.setFileSize(fl[i].length());
				String type = uf.getFileName().substring(
						uf.getFileName().lastIndexOf("."),
						uf.getFileName().length());

				uf.setFileType(type);
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				uf.setLastModified(sdf.format(new Date(fl[i].lastModified())));
				files.add(uf);
				fl[i].delete();
			}
		}
		//model.addAttribute("files", files);
		return "redirect:mutifileselects.htm";
	}

	@HandlerMapping(value = "/file/download.htm")
	public @ResponseBody File downloadFile(@RequestParam(decodeCharset="UTF-8")
	String fileName, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		if(fileName != null && fileName.indexOf("..") > 0)
			throw new IOException(fileName +" is illeage:.. is not allowed.");
		File file = new File(request.getRealPath("/")+"filesdown/"+fileName);
		
		
		
		return file;
	}
	
	@HandlerMapping(value = "/file/downloadFile.htm")
	public @ResponseBody File downloadFileaaa(@RequestParam(decodeCharset="UTF-8")
	String fileName, HttpServletRequest request)
			throws IOException {
		if(fileName != null && fileName.indexOf("..") > 0)
			throw new IOException(fileName +" is illeage:.. is not allowed.");
		File file = new File(request.getRealPath("/")+"filesdown/"+fileName);
		return file;
	}
	
	@HandlerMapping(value = "/vidio/download.htm")
	public @ResponseBody File downloadVidioFile(@RequestParam(decodeCharset="UTF-8")
	String fileName, HttpServletRequest request)
			throws IOException {
		if(fileName != null && fileName.indexOf("..") > 0)
			throw new IOException(fileName +" is illeage:.. is not allowed.");
		File file = new File(request.getRealPath("/")+"vidiosdown/"+fileName);
		return file;
	}
	
	
	@HandlerMapping(value = "/tool/download.htm")
	public @ResponseBody File downloadToolFile(@RequestParam(decodeCharset="UTF-8")
	String fileName, HttpServletRequest request)
			throws IOException {
		if(fileName != null && fileName.indexOf("..") > 0)
			throw new IOException(fileName +" is illeage:.. is not allowed.");
		File file = new File(request.getRealPath("/")+"toolsdown/"+fileName);
		return file;
	}
	   public static void sendFile(HttpServletRequest request, HttpServletResponse response, File file) throws IOException {
	        OutputStream out = response.getOutputStream();
	        RandomAccessFile raf = new RandomAccessFile(file, "r");
	        try {
	            long fileSize = raf.length();
	            long rangeStart = 0;
	            long rangeFinish = fileSize - 1;

	            // accept attempts to resume download (if any)
	            String range = request.getHeader("Range");
	            if (range != null && range.startsWith("bytes=")) {
	                String pureRange = range.replaceAll("bytes=", "");
	                int rangeSep = pureRange.indexOf("-");

	                try {
	                    rangeStart = Long.parseLong(pureRange.substring(0, rangeSep));
	                    if (rangeStart > fileSize || rangeStart < 0) rangeStart = 0;
	                } catch (NumberFormatException e) {
	                    // ignore the exception, keep rangeStart unchanged
	                }

	                if (rangeSep < pureRange.length() - 1) {
	                    try {
	                        rangeFinish = Long.parseLong(pureRange.substring(rangeSep + 1));
	                        if (rangeFinish < 0 || rangeFinish >= fileSize) rangeFinish = fileSize - 1;
	                    } catch (NumberFormatException e) {
	                        // ignore the exception
	                    }
	                }
	            }

	            // set some headers


	            response.setHeader("Content-Disposition", "attachment; filename=" + new String(file.getName().getBytes(),"ISO-8859-1").replaceAll(" ", "-"));
	            response.setHeader("Accept-Ranges", "bytes");
	            response.setHeader("Content-Length", Long.toString(rangeFinish - rangeStart + 1));
	            response.setHeader("Content-Range", "bytes " + rangeStart + "-" + rangeFinish + "/" + fileSize);

	            // seek to the requested offset
	            raf.seek(rangeStart);

	            // send the file
	            byte buffer[] = new byte[1024];

	            long len;
	            int totalRead = 0;
	            boolean nomore = false;
	            while (true) {
	                len = raf.read(buffer);
	                if (len > 0 && totalRead + len > rangeFinish - rangeStart + 1) {
	                    // read more then required?
	                    // adjust the length
	                    len = rangeFinish - rangeStart + 1 - totalRead;
	                    nomore = true;
	                }

	                if (len > 0) {
	                    out.write(buffer, 0, (int) len);
	                    totalRead += len;
	                    if (nomore) break;
	                } else {
	                    break;
	                }
	            }
	            out.flush();
	        } finally {
	            raf.close();
	            out.close();
	        }
	    }
	   @HandlerMapping(value="/files/downloadList.htm")
	   public String filesDownList(ModelMap model,HttpServletRequest request) {
			File file = new File(request.getRealPath("/")+"filesdown");

			List<UpFile> files = new ArrayList<UpFile>();
			if (file.exists()) {
				File[] fl = file.listFiles();
				for (int i = 0; fl != null && i < fl.length; i++) {
					if(fl[i].isDirectory())
						continue;
					UpFile uf = new UpFile();
					uf.setFileName(fl[i].getName());
					uf.setFileSize(fl[i].length());
					String type = uf.getFileName().substring(
							uf.getFileName().lastIndexOf("."),
							uf.getFileName().length());

					uf.setFileType(type);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					uf.setLastModified(sdf.format(new Date(fl[i].lastModified())));
					files.add(uf);
				}
			}
			if(files != null && files.size() > 0)
				sortfile(files);
			model.addAttribute("files", files);
			model.addAttribute("filetype", "file");
			return "files/downloadlist";
		}
	   
	   
	   @HandlerMapping(value="/vidios/downloadList.htm")
	   public String vidiosDownList(ModelMap model,HttpServletRequest request) {
			File file = new File(request.getRealPath("/")+"vidiosdown");

			List<UpFile> files = new ArrayList<UpFile>();
			if (file.exists()) {
				File[] fl = file.listFiles(new FileFilter(){

					@Override
					public boolean accept(File pathname) {
						if(pathname.isDirectory() || pathname.getName().equals("Thumbs.db"))
							return false;
						else
							return true;
					}});
				for (int i = 0; fl != null && i < fl.length; i++) {
					if(fl[i].isDirectory())
						continue;
					UpFile uf = new UpFile();
					uf.setFileName(fl[i].getName());
					uf.setFileSize(fl[i].length());
					String type = uf.getFileName().substring(
							uf.getFileName().lastIndexOf("."),
							uf.getFileName().length());

					uf.setFileType(type);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					uf.setLastModified(sdf.format(new Date(fl[i].lastModified())));
					files.add(uf);
				}
			}
			if(files != null && files.size() > 0)
				sortfile(files);
			model.addAttribute("files", files);
			model.addAttribute("filetype", "vidio");

			return "files/downloadlist";
		}
	   @HandlerMapping(value="/tools/downloadList.htm")
	   public String toolsDownList(ModelMap model,HttpServletRequest request) {
			File file = new File(request.getRealPath("/")+"toolsdown");

			List<UpFile> files = new ArrayList<UpFile>();
			if (file.exists()) {
				File[] fl = file.listFiles(new FileFilter(){

					@Override
					public boolean accept(File pathname) {
						if(pathname.isDirectory() || pathname.getName().equals("Thumbs.db"))
							return false;
						else
							return true;
					}});
				for (int i = 0; fl != null && i < fl.length; i++) {
					if(fl[i].isDirectory())
						continue;
					UpFile uf = new UpFile();
					uf.setFileName(fl[i].getName());
					uf.setFileSize(fl[i].length());
					String type = uf.getFileName().substring(
							uf.getFileName().lastIndexOf("."),
							uf.getFileName().length());

					uf.setFileType(type);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					uf.setLastModified(sdf.format(new Date(fl[i].lastModified())));
					files.add(uf);
				}
			}
			if(files != null && files.size() > 0)
				sortfile(files);
			model.addAttribute("files", files);
			model.addAttribute("filetype", "tool");

			return "files/downloadlist";
		}
	   private void sortfile(List<UpFile> files)
	   {
		   Collections.sort(files, new Comparator<UpFile>() {

			@Override
			public int compare(UpFile o1, UpFile o2) {
				// TODO Auto-generated method stub
				return o1.getFileName().toLowerCase().compareTo(o2.getFileName().toLowerCase());
			}

			
			   
		   });
	   }
	   
	   
	   
}
