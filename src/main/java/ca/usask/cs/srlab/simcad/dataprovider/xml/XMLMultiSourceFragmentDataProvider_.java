package ca.usask.cs.srlab.simcad.dataprovider.xml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import ca.usask.cs.srlab.simcad.SimcadException;
import ca.usask.cs.srlab.simcad.dataprovider.AbstractFragmentDataProvider;
import ca.usask.cs.srlab.simcad.model.CloneFragment;
import ca.usask.cs.srlab.simcad.util.PropsUtil;

public class XMLMultiSourceFragmentDataProvider_ extends AbstractFragmentDataProvider{
	
	protected class NodeListIterator{
		private int currentIndex;
		private int size;
		private NodeList nodeList;
		
		public NodeListIterator(NodeList nodeList) {
			super();
			this.nodeList = nodeList;
			currentIndex = 0;
			size = nodeList.getLength();
		}

		public boolean hasNext(){
			return currentIndex < size;
		}
		
		public Node next() {
//			while (hasNext()) {
//				Node node = nodeList.item(currentIndex++);
//				if (node.getNodeType() != Node.ELEMENT_NODE)
//					continue;
//				return node;
//			}
			if(hasNext())
				return nodeList.item(currentIndex++);
			return null;
		}
	}  
	
	protected NodeListIterator orirginalCloneFragmentIterator;
	protected NodeListIterator transformedCloneFragmentIterator;
	
	
	@SuppressWarnings("unused")
	private XMLMultiSourceFragmentDataProvider_(){
	}
	
	public XMLMultiSourceFragmentDataProvider_(XMLMultiSourceFragmentDataProviderConfiguration dataProviderConfig){
		super(dataProviderConfig);
	}
	
//	public XMLMultiSourceCloneFragmentDataProvider(XMLMultiSourceFragmentDataProviderConfiguration dataProviderConfig, DetectionSettings ds){
//		super(dataProviderConfig, ds);
//	}
	
	protected void initDataSource(){
		String dataSource[] = applyDataTransformation();
		orirginalCloneFragmentIterator = new NodeListIterator(getSourceNodeList(dataSource[0]));
		transformedCloneFragmentIterator = new NodeListIterator(getSourceNodeList(dataSource[1]));
	}

	private NodeList getSourceNodeList(String dataSource) {
		try{
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
	        InputStream inputStream= new FileInputStream(dataSource);
	        Reader reader = new InputStreamReader(inputStream,"UTF-8");
	        InputSource is = new InputSource(reader);
	        is.setEncoding("UTF-8");
	        
	        Document doc = db.parse(is);
	        doc.getDocumentElement().normalize();
			
			Element root = doc.getDocumentElement();
			NodeList nl = root.getElementsByTagName("name");
			
//		  if(nl.getLength()>0){
//			  //projectName = nl.item(0).getFirstChild().getNodeValue();
//		  }else{
//			  error = true;
//		  }
//		  
//		  nl = root.getElementsByTagName("description");
//		  if(nl.getLength()>0){
//			  //projectDesc = nl.item(0).getChildNodes().item(0).getNodeValue();
//		  }else{
//			  error = true;
//		  }
		  
			nl = root.getElementsByTagName("source");
		  
			if(nl.getLength()>0){
				return nl;//.item(0).getChildNodes();
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			throw new SimcadException("Unable to parse code fragment from file", e);
		}
		return null;
	}
	
	/*
	public List<CloneFragment> extractFragments(){
		
		initDataSource();
		
		List<CloneFragment> cloneFragmentList = new LinkedList<CloneFragment>();
		Integer minSizeOfGranularity = PropsUtil.getMinSizeOfGranularity();
		boolean fileUrlRelative = PropsUtil.getIsFragmentFileRelativeURL();
		
		Integer items = 0;
		
		for(; orirginalCloneFragmentIterator.hasNext();){
			Node originalSource = orirginalCloneFragmentIterator.next();
			Node transformedSource = transformedCloneFragmentIterator.next();

			if (originalSource.getNodeType() != Node.ELEMENT_NODE)
				continue;
			
			String originalContent = originalSource.getFirstChild().getTextContent().trim();
			if(CloneFragment.computeActualLineOfCode(originalContent) < minSizeOfGranularity) continue;

			String file = originalSource.getAttributes().getNamedItem("file").getFirstChild().getNodeValue();
			String startline = originalSource.getAttributes().getNamedItem("startline").getFirstChild().getNodeValue();
			String endline = originalSource.getAttributes().getNamedItem("endline").getFirstChild().getNodeValue();
			
			String fileT = transformedSource.getAttributes().getNamedItem("file").getFirstChild().getNodeValue();
			String startlineT = transformedSource.getAttributes().getNamedItem("startline").getFirstChild().getNodeValue();
			String endlineT = transformedSource.getAttributes().getNamedItem("endline").getFirstChild().getNodeValue();
			
			//System.out.println(file +" ("+startline+","+endline+")");
			//System.out.println(fileT +" ("+startlineT+","+endlineT+")");
			
			if(!file.equals(fileT)){
				throw new SimcadException("Original ("+file+" ( "+startline+"/"+endline+")) and transformed ("+fileT+" ("+startlineT+"/"+endlineT+")) source (at file) are not synced to each other");
			}else if(!startline.equals(startlineT)){
				throw new SimcadException("Original ("+file+" ( "+startline+"/"+endline+")) and transformed ("+fileT+" ("+startlineT+"/"+endlineT+")) source (at startline) are not synced to each other");
			}if(!endline.equals(endlineT)){
				throw new SimcadException("Original ("+file+" ( "+startline+"/"+endline+")) and transformed ("+fileT+" ("+startlineT+"/"+endlineT+")) source (at endline) are not synced to each other");
			}
			
			if(dataProviderConfig.getSourceDataRootUrl() != null && fileUrlRelative){
				//file = file.replace(dataProviderConfig.getSourceDaraRootUrl(), "");
				file = file.substring(dataProviderConfig.getSourceDataRootUrl().length());
			}
//			System.out.println(file);
			
			String transformedContent = transformedSource.getFirstChild().getTextContent().trim();
			CloneFragment cloneFragment = createNewCloneFragment(file, startline, endline, originalContent, transformedContent, items++);
			cloneFragmentList.add(cloneFragment);
		}
		
		return cloneFragmentList;
	}
*/
	
public List<CloneFragment> extractFragments(){
        
        initDataSource();
        
        List<CloneFragment> cloneFragmentList = new LinkedList<CloneFragment>();
        Integer minSizeOfGranularity = PropsUtil.getMinSizeOfGranularity();
        boolean fileUrlRelative = PropsUtil.getIsFragmentFileRelativeURL();
        
        Integer items = 0;
        boolean lastMatchingFailed = false;
        Node originalSource, transformedSource = null;
        
        for(; orirginalCloneFragmentIterator.hasNext();){
            
            originalSource = orirginalCloneFragmentIterator.next();
            if (originalSource.getNodeType() != Node.ELEMENT_NODE)
                continue;
            
            if(!lastMatchingFailed){
                transformedSource = transformedCloneFragmentIterator.next();
            }
        
            String originalContent = originalSource.getFirstChild().getTextContent().trim();
            if(CloneFragment.computeActualLineOfCode(originalContent) < minSizeOfGranularity) continue;

            String file = originalSource.getAttributes().getNamedItem("file").getFirstChild().getNodeValue();
            String startline = originalSource.getAttributes().getNamedItem("startline").getFirstChild().getNodeValue();
            String endline = originalSource.getAttributes().getNamedItem("endline").getFirstChild().getNodeValue();
            
            String fileT = transformedSource.getAttributes().getNamedItem("file").getFirstChild().getNodeValue();
            String startlineT = transformedSource.getAttributes().getNamedItem("startline").getFirstChild().getNodeValue();
            String endlineT = transformedSource.getAttributes().getNamedItem("endline").getFirstChild().getNodeValue();
            
            //System.out.println(file +" ("+startline+","+endline+")");
            //System.out.println(fileT +" ("+startlineT+","+endlineT+")");
            
            //TODO:Assumption: elements in source and transformed file assumed to be in same order
            
            if(!file.equals(fileT)){
                lastMatchingFailed = true; continue;
                //throw new SimcadException("Original ("+file+" ( "+startline+"/"+endline+")) and transformed ("+fileT+" ("+startlineT+"/"+endlineT+")) source (at file) are not synced to each other");
            }else if(!startline.equals(startlineT)){
                lastMatchingFailed = true; continue;
                //throw new SimcadException("Original ("+file+" ( "+startline+"/"+endline+")) and transformed ("+fileT+" ("+startlineT+"/"+endlineT+")) source (at startline) are not synced to each other");
            }if(!endline.equals(endlineT)){
                lastMatchingFailed = true; continue;
                //throw new SimcadException("Original ("+file+" ( "+startline+"/"+endline+")) and transformed ("+fileT+" ("+startlineT+"/"+endlineT+")) source (at endline) are not synced to each other");
            }
            
            if(fileUrlRelative){
                //file = file.replace(dataProviderConfig.getSourceDaraRootUrl(), "");
                file = file.substring(dataProviderConfig.getSourceDataRootUrl().length());
            }
//            System.out.println(file);
            
            String transformedContent = transformedSource.getFirstChild().getTextContent().trim();
            CloneFragment cloneFragment = createNewCloneFragment(file, startline, endline, originalContent, transformedContent, items++);
            cloneFragmentList.add(cloneFragment);
            lastMatchingFailed = false;
        }
        
        return cloneFragmentList;
    }
	
	
	//@Override
	protected String[] applyDataTransformation() {
		List<IXMLFragmentDataProviderTransformer> dataTransformerList = ((XMLMultiSourceFragmentDataProviderConfiguration)dataProviderConfig).getDataTransformer();
		String dataSource1 = ((XMLMultiSourceFragmentDataProviderConfiguration)dataProviderConfig).getOriginalSourceXmlFileName();
		String dataSource2 = ((XMLMultiSourceFragmentDataProviderConfiguration)dataProviderConfig).getTransformedSourceXmlFileName();
		
		for (IXMLFragmentDataProviderTransformer xmlFragmentDataProviderTransformer : dataTransformerList) {
			dataSource1 = xmlFragmentDataProviderTransformer.transform(dataSource1);
			dataSource2 = xmlFragmentDataProviderTransformer.transform(dataSource2);
		}
		return new String[] {dataSource1, dataSource2};
	}
	
}
