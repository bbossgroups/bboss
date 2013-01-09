//
// JODConverter - Java OpenDocument Converter
// Copyright 2004-2011 Mirko Nasato and contributors
//
// JODConverter is free software: you can redistribute it and/or
// modify it under the terms of the GNU Lesser General Public License
// as published by the Free Software Foundation, either version 3 of
// the License, or (at your option) any later version.
//
// JODConverter is distributed in the hope that it will be useful,
// but WITHOUT ANY WARRANTY; without even the implied warranty of
// MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
// Lesser General Public License for more details.
//
// You should have received a copy of the GNU Lesser General
// Public License along with JODConverter.  If not, see
// <http://www.gnu.org/licenses/>.
//
package bboss.org.artofsolving.jodconverter;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import bboss.org.artofsolving.jodconverter.document.DefaultDocumentFormatRegistry;
import bboss.org.artofsolving.jodconverter.document.DocumentFormat;
import bboss.org.artofsolving.jodconverter.document.DocumentFormatRegistry;
import bboss.org.artofsolving.jodconverter.office.OfficeException;
import bboss.org.artofsolving.jodconverter.office.OfficeManager;
import org.frameworkset.jodconvertor.WorkBookmarkConvertorTask;

import com.sun.star.beans.PropertyValue;
import com.sun.star.document.UpdateDocMode;

public class OfficeDocumentConverter {

    private final OfficeManager officeManager;
    private final DocumentFormatRegistry formatRegistry;

    private Map<String,?> defaultLoadProperties = createDefaultLoadProperties();

    public OfficeDocumentConverter(OfficeManager officeManager) {
        this(officeManager, new DefaultDocumentFormatRegistry());
    }

    public OfficeDocumentConverter(OfficeManager officeManager, DocumentFormatRegistry formatRegistry) {
        this.officeManager = officeManager;
        this.formatRegistry = formatRegistry;
    }

    private Map<String,Object> createDefaultLoadProperties() {
        Map<String,Object> loadProperties = new HashMap<String,Object>();
        loadProperties.put("Hidden", true);
        loadProperties.put("ReadOnly", true);
        loadProperties.put("UpdateDocMode", UpdateDocMode.QUIET_UPDATE);
        return loadProperties;
    }

    public void setDefaultLoadProperties(Map<String, ?> defaultLoadProperties) {
        this.defaultLoadProperties = defaultLoadProperties;
    }

    public DocumentFormatRegistry getFormatRegistry() {
        return formatRegistry;
    }

    public void convert(File inputFile, File outputFile) throws OfficeException {
        String outputExtension = FilenameUtils.getExtension(outputFile.getName());
        DocumentFormat outputFormat = formatRegistry.getFormatByExtension(outputExtension);
        convert(inputFile, outputFile, outputFormat);
    }

    public void convert(File inputFile, File outputFile, DocumentFormat outputFormat) throws OfficeException {
        String inputExtension = FilenameUtils.getExtension(inputFile.getName());
        DocumentFormat inputFormat = formatRegistry.getFormatByExtension(inputExtension);
        StandardConversionTask conversionTask = new StandardConversionTask(inputFile, outputFile, outputFormat);
        conversionTask.setDefaultLoadProperties(defaultLoadProperties);
        conversionTask.setInputFormat(inputFormat);
        officeManager.execute(conversionTask);
    }
    
    public File getRealWordFromWordTemplateWithMapdatas(String wordtemplate, String wordfile,
			Map<String, Object> bookdatas) throws Exception
	{
    	 String outputExtension = FilenameUtils.getExtension(wordfile);
         DocumentFormat outputFormat = formatRegistry.getFormatByExtension(outputExtension);
         String inputExtension = FilenameUtils.getExtension(outputExtension);
         DocumentFormat inputFormat = formatRegistry.getFormatByExtension(inputExtension);
         WorkBookmarkConvertorTask conversionTask = new WorkBookmarkConvertorTask(wordtemplate, wordfile, bookdatas,outputFormat);
         Map<String,Object> defaultLoadProperties = new HashMap<String,Object>();
         defaultLoadProperties.put("AsTemplate",new Boolean (true));
    	 defaultLoadProperties.put("Hidden",new Boolean (true));
         conversionTask.setDefaultLoadProperties(defaultLoadProperties);
//         conversionTask.setDefaultStroreProperties(defaultLoadProperties);
         conversionTask.setInputFormat(inputFormat);
         officeManager.execute(conversionTask);
         return conversionTask.getOutputFile();
	}
    
    public File getRealWordFromWordTemplateWithArraydatas(String wordtemplate, String wordfile,
			String[] bookmarks,Object[] bookdatas) throws Exception
	{
    	 Map<String,Object> defaultLoadProperties = new HashMap<String,Object>();
    	 defaultLoadProperties.put("AsTemplate",new Boolean (true));
    	 defaultLoadProperties.put("Hidden",new Boolean (true));
    	 String outputExtension = FilenameUtils.getExtension(wordfile);
         DocumentFormat outputFormat = formatRegistry.getFormatByExtension(outputExtension);
         String inputExtension = FilenameUtils.getExtension(outputExtension);
         DocumentFormat inputFormat = formatRegistry.getFormatByExtension(inputExtension);
         WorkBookmarkConvertorTask conversionTask = new WorkBookmarkConvertorTask(wordtemplate, wordfile, bookmarks,bookdatas,outputFormat);
         conversionTask.setDefaultLoadProperties(defaultLoadProperties);
//    	 Map<String,Object> defaultStoreProperties = null;
//         conversionTask.setDefaultStroreProperties(defaultStoreProperties);
         conversionTask.setInputFormat(inputFormat);
         officeManager.execute(conversionTask);
         return conversionTask.getOutputFile();
	}

}
