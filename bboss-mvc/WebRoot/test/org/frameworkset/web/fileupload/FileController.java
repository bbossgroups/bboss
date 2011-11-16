package org.frameworkset.web.fileupload;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.frameworkset.util.annotations.HandlerMapping;
import org.frameworkset.util.annotations.RequestParam;
import org.frameworkset.util.annotations.ResponseBody;
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
	public @ResponseBody(charset="UTF-8") String deleteFileOpera(
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
	public String downloadFile(@RequestParam(name = "fileName")
	String fileName, HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		File file = new File(request.getRealPath("/")+"filesdown/"+fileName);
		
		sendFile(request, response, file);
		
		return null;
	}
	
	@HandlerMapping(value = "/file/downloadFile.htm")
	public @ResponseBody File downloadFile(@RequestParam(name = "fileName")
	String fileName, HttpServletRequest request)
			throws IOException {
		File file = new File(request.getRealPath("/")+"filesdown/"+fileName);
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
			model.addAttribute("files", files);

			return "files/downloadlist";
		}
	   
	   
	   
}
