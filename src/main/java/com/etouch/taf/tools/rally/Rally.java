package com.etouch.taf.tools.rally;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;

import com.etouch.taf.core.exception.DefectException;
import com.etouch.taf.core.resources.DefectParameters;
import com.etouch.taf.tools.defect.DefectManager;
import com.etouch.taf.util.CommonUtil;
import com.etouch.taf.util.DateUtil;
import com.etouch.taf.util.LogUtil;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonArray;
import com.rallydev.rest.RallyRestApi;
import com.rallydev.rest.request.CreateRequest;
import com.rallydev.rest.request.DeleteRequest;
import com.rallydev.rest.request.QueryRequest;
import com.rallydev.rest.request.UpdateRequest;
import com.rallydev.rest.response.CreateResponse;
import com.rallydev.rest.response.DeleteResponse;
import com.rallydev.rest.response.QueryResponse;
import com.rallydev.rest.response.UpdateResponse;
import com.rallydev.rest.util.Fetch;
import com.rallydev.rest.util.QueryFilter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * RallyIntegration exposes API to integrate with Rally.
 * 
 * @author eTouch Systems Corporation
 * @version 1.0
 *
 */
public class Rally implements DefectManager{
	
	private static Log log = LogUtil.getLog(Rally.class);
	
	private RallyInfo rallyInfo;

	private RallyRestApi restApi;
	
	private String defectStatus;
	
	private String defectOID;
	
	public Rally(RallyInfo rallyInfo) throws DefectException{
		this.rallyInfo = rallyInfo;
	}

	public void setRallyInfo(RallyInfo rallyInfo) {
		this.rallyInfo = rallyInfo;
	}
	
	public String getDefectStatus() {
		return defectStatus;
	}

	public void setDefectStatus(String rallyStatus) {
		this.defectStatus = rallyStatus;
	}
	
	private String getDefectOID() {
		return defectOID;
	}

	private void setDefectOID(String defectOID) {
		this.defectOID = defectOID;
	}
	
   	/**
   	 * Build a RequestInfo and create a rally defect 
   	 * @param ri
   	 * @param fileName
   	 * @param testcaseId
   	 * @param projId
   	 * @param storyId
   	 */
	public void createDefectBuilder(String defName, String testcaseId, String projId, String DefSeverity,
									String DefOwner, String DefNotes, String storyId){
		try{
			Map<String, String> reqMap = new HashMap<String, String>();
			reqMap.put(RallyConstants.NAME, defName);
			reqMap.put(RallyConstants.CREATEDATE, DateUtil.getCurrentDateTime(RallyConstants.CREATION_DATE_FORMAT));
			reqMap.put(RallyConstants.TESTCASE, testcaseId);
			reqMap.put(RallyConstants.PROJECT, projId);
			reqMap.put(RallyConstants.SEVERITY, DefSeverity);
			reqMap.put(RallyConstants.OWNER, DefOwner);
			reqMap.put(RallyConstants.NOTES, DefNotes);
			//reqMap.put("Requirement", storyId);

			RequestInfo reqInfo= new RequestInfo(RallyConstants.DEFECT, RallyConstants.CREATE, reqMap);
			try {

				create(reqInfo);
			} catch (DefectException ex) {
				log.error(ex.getMessage());
			}
		}catch(NullPointerException e){
			log.error(e.getMessage());
		}
	}
	
   	/**
   	 * Verify if a defect already exists in Rally. If the defect exists, save the testcase formatted id, defect id and defect status
   	 * @param ri
   	 * @param fileName
   	 * @param testcaseId
   	 * @param projId
   	 * @param storyId
   	 * @param defName
   	 */	
	public boolean verifyDefectExists(String testcaseId, String projId, String storyId, String defName){
		String formattedID = null;
		String testCaseRegex = "^/testcase/";
		String formatRegex = "^FormattedID[\\s]*:[\\s]*";
		String stateRegex = "^.*State[\\s]*:[\\s]*";
		String objPreRegex = "^ObjectID[\\s]*:[\\s]*";
		String objPostRegex = "[\\s]*State[\\s]*:[\\s]*[\\w]*";
		RequestInfo rInfo = new RequestInfo();
		
		//Retrieve the TestCase FormattedID using the TestCase OID 
		String[] fetchParam = {RallyConstants.FORMATTED_ID};
		String[] filterArray = {RallyConstants.OBJECTID, RallyConstants.EQUALTO, testcaseId.replaceFirst(testCaseRegex, "")};
		buildQueryReqInfo(rInfo, RallyConstants.TESTCASE, fetchParam, projId, filterArray);
		try {
			List<String> list=getList(rInfo);
			if((list!=null)){
			formattedID = getList(rInfo).get(0);
			}
		} catch (DefectException e) {
			//log.error(e.getMessage());
			return false;
		}
		if(formattedID != null){
			formattedID = formattedID.replaceFirst(formatRegex, "");
		}else{
			log.error("Failure to verify if defect exists: failure to retrieve testcase details for OID - " + testcaseId);
			return false;
		}
		        
		//Retrieve all the defects related to the TestCase FormattedID 
		fetchParam = new String[]{RallyConstants.OBJECTID, RallyConstants.STATE};
		if(formattedID!=null){
		filterArray = new String[]{RallyConstants.TESTCASEFID, RallyConstants.EQUALTO, formattedID, RallyConstants.AND_OPERATOR, RallyConstants.NAME, RallyConstants.EQUALTO, defName};
		}
		else{
			
			filterArray = new String[]{ RallyConstants.NAME, RallyConstants.EQUALTO, defName};
		}
		
		buildQueryReqInfo(rInfo, RallyConstants.DEFECT, fetchParam, projId, filterArray);
		try {
			List<String> qlist = getList(rInfo);
			if((qlist != null) && qlist.size()>0){
				String objIDStr = qlist.get(0).replaceFirst(objPreRegex, "");
				setDefectOID(objIDStr.replaceFirst(objPostRegex, "").trim());
				setDefectStatus(qlist.get(0).replaceFirst(stateRegex, "").trim());
				return true;				
			}
		} catch (DefectException e) {
			log.error(e.getMessage());
			return false;
		}		
		return false;
	}
	
	public boolean verifyIfdefectClosed(){
		if(getDefectStatus() != null && (getDefectStatus().equals(RallyConstants.STATUS_FIXED) || getDefectStatus().equals(RallyConstants.STATUS_CLOSED))){
			return true;
		}
		return false;
	}
	
	public String queryTestCaseFormatID(String testcaseId, String projId, String storyId, String defName){
		String formattedID = "";
		String testCaseRegex = "^/testcase/";
		String formatRegex = "^FormattedID[\\s]*:[\\s]*";
		RequestInfo rInfo = new RequestInfo();
		
		//Retrieve the TestCase FormattedID using the TestCase OID 
		String[] fetchParam = {RallyConstants.FORMATTED_ID};
		String[] filterArray = {RallyConstants.OBJECTID, RallyConstants.EQUALTO, testcaseId.replaceFirst(testCaseRegex, "")};
		buildQueryReqInfo(rInfo, RallyConstants.TESTCASE, fetchParam, projId, filterArray);
		try {
			if((getList(rInfo)!=null)&&(getList(rInfo).size()>0)){
			formattedID = getList(rInfo).get(0);
			}
		} catch (DefectException e) {
			log.error(e.getMessage());
		}
		if(formattedID != null){
			formattedID = formattedID.replaceFirst(formatRegex, "");
		}
		return formattedID;
	}
	
   	/**
   	 * Retrieve the defect object ID 
   	 * @param ri
   	 * @param fileName
   	 * @param testcaseId
   	 * @param projId
   	 * @param storyId
   	 * @param defName
   	 */	
	public String queryDefectID(String testcaseId, String storyId, String projId, String defName){
		String objPreRegex = "^ObjectID[\\s]*:[\\s]*";
		RequestInfo rInfo = new RequestInfo();
		String[] fetchParam = new String[]{RallyConstants.OBJECTID};
		
		//Retrieve the testcase format id
		String tcFormattedID = queryTestCaseFormatID(testcaseId, projId, storyId, defName);
		//Retrieve all the defects related to the TestCase FormattedID 
		if(tcFormattedID!= null && !tcFormattedID.isEmpty()){
			String[] filterArray = new String[]{RallyConstants.TESTCASEFID, RallyConstants.EQUALTO, tcFormattedID, RallyConstants.AND_OPERATOR, RallyConstants.NAME, RallyConstants.EQUALTO, defName};
			buildQueryReqInfo(rInfo, RallyConstants.DEFECT, fetchParam, projId, filterArray);
			try {
				List<String> qlist = getList(rInfo);
				if((qlist != null) && !qlist.isEmpty()){
					setDefectOID(qlist.get(0).replaceFirst(objPreRegex, "").trim());
					//setDefectOID(objIDStr.replaceFirst(objPostRegex, "").trim());				
				}
			} catch (DefectException e) {
				log.error(e.getMessage());
			}
		}
		return getDefectOID();
	}
	
   	/**
   	 * Retrieve the defect object ID 
   	 * @param ri
   	 * @param fileName
   	 * @param testcaseId
   	 * @param projId
   	 * @param storyId
   	 * @param defName
   	 */	
	public String queryDefectStatus(String testcaseId, String storyId, String projId, String defName){
		String stateRegex = "^.*State[\\s]*:[\\s]*";
		RequestInfo rInfo = new RequestInfo();
		
		//Retrieve the testcase format id
		String tcFormattedID = queryTestCaseFormatID(testcaseId, projId, storyId, defName);
		//Retrieve all the defects related to the TestCase FormattedID 
		String[] fetchParam = new String[]{RallyConstants.STATE};
		String[] filterArray = new String[]{RallyConstants.TESTCASEFID, RallyConstants.EQUALTO, tcFormattedID, RallyConstants.AND_OPERATOR, RallyConstants.NAME, RallyConstants.EQUALTO, defName};
		buildQueryReqInfo(rInfo, RallyConstants.DEFECT, fetchParam, projId, filterArray);
		try {
			List<String> qlist = getList(rInfo);
			if((qlist != null) && !qlist.isEmpty()){
				setDefectStatus(qlist.get(0).replaceFirst(stateRegex, "").trim());
			}
		} catch (DefectException e) {
			log.error(e.getMessage());
		}
		
		return getDefectStatus();
	}

   	/**
   	 * Retrieve the defect object ID 
   	 * Note: example of queryByParam: String[] queryByParam = new String[]{IRallyParam.OBJECTID};
   	 * example of fetchQueryRegex: String fetchQueryRegex = "^ObjectID[\\s]*:[\\s]*";
   	 * @param ri
   	 * @param fileName
   	 * @param testcaseId
   	 * @param projId
   	 * @param storyId
   	 * @param defName
   	 */	
	public String queryDefect(String testcaseId, String storyId, String projId, final String defName, DefectParameters.IDefect queryByParam){
		RequestInfo rInfo = new RequestInfo();
		String fetchQueryVal = null;
		String fetchQueryRegex = "^.*" + queryByParam.getParamType() + "[\\s]*:[\\s]*";
		
		//Retrieve the testcase format id
		String tcFormattedID = queryTestCaseFormatID(testcaseId, projId, storyId, defName);
		//Retrieve all the defects related to the TestCase FormattedID 
		if(tcFormattedID!= null && !tcFormattedID.isEmpty()){
			String[] fetchParamArray = new String[]{queryByParam.getParamType()};
			String[] filterArray = new String[]{RallyConstants.TESTCASEFID, RallyConstants.EQUALTO, tcFormattedID, RallyConstants.AND_OPERATOR, RallyConstants.NAME, RallyConstants.EQUALTO, defName};
			buildQueryReqInfo(rInfo, RallyConstants.DEFECT, fetchParamArray, projId, filterArray);
			try {
				List<String> qlist = getList(rInfo);
				if((qlist != null) && !qlist.isEmpty()){
					fetchQueryVal = qlist.get(0).replaceFirst(fetchQueryRegex, "").trim();				
				}
			} catch (DefectException e) {
				log.error(e.getMessage());
			}
		}
		return fetchQueryVal;
	}	
	
   	/**
   	 * Build a RequestInfo and update a rally defect 
   	 * @param ri
   	 * @throws DefectException
   	 */	
	public void reopenDefect(){
		try{
			String ref = "/" + RallyConstants.DEFECT + "/" + getDefectOID();
			RequestInfo reqInfo= new RequestInfo();
			reqInfo.setRefField(ref);
			reqInfo.addEntry(RallyConstants.UPDATE, RallyConstants.STATE, RallyConstants.STATUS_OPEN);
			try {
				update(reqInfo);
			} catch (DefectException e) {
				// TODO Auto-generated catch block
				log.error(e.getMessage());
			}
		} finally{
			setDefectOID(null);
			setDefectStatus(null);
		}
	}	
	
   	/**
   	 * Build a RequestInfo and update a rally defect field/attribute
   	 * @param ri
   	 * @throws DefectException
   	 */	
	public boolean updateDefect(String testcaseId, String projId, String storyId, String defName, DefectParameters.IDefect updateKey, String updateValue){
		try{
			if(verifyDefectExists(testcaseId, projId, storyId, defName)){
//				String tcFormattedID = queryTestCaseFormatID(testcaseId, projId, storyId, defName);
//				setDefectOID(queryDefectID(projId, tcFormattedID, defName));
				
				String ref = "/" + RallyConstants.DEFECT + "/" + getDefectOID();
				RequestInfo reqInfo= new RequestInfo();
				reqInfo.setRefField(ref);
				reqInfo.addEntry(RallyConstants.UPDATE, updateKey.getParamType(), updateValue);
				update(reqInfo);	
				log.info("Successfully updated:" + updateKey.getParamType());
				return true;
			}
		}catch (DefectException e) {
				log.error("Error when updating defect: " + e.getMessage());				
		}finally{
			setDefectOID(null);
		}
		return false;
	}
	
	/**
	 * Close rally connection.
	 * @throws DefectException
	 */
	public void closeDefect() throws DefectException{
		if(!CommonUtil.isNull(restApi)){
	        //Release resources
	        try {
				restApi.close();
			} catch (IOException e) {
	        	log.error("failed to close rally connection, message : " + e.toString());
	        	throw new DefectException("failed to close rally connection, message : " + e.toString());        	
			}finally{
				restApi=null;
			}
		}	 
	}	
	
   	/**
   	 * Build the RequestInfo with arguments provided
   	 * @param rInfo
   	 * @param requestType
   	 * @param fetchParam
   	 * @param projId
   	 * @param filterArray
   	 */		
	private void buildQueryReqInfo(RequestInfo rInfo, String requestType, String[] fetchParam, String projId, String[] filterArray){
		rInfo.setRequestType(requestType);
		rInfo.setProjectOID(projId);
		rInfo.setScopeDown(true);
		rInfo.setFetch(fetchParam);
		ArrayList<String> filterList = new ArrayList<String>(Arrays.asList(filterArray));
		rInfo.setQueryFilter(filterList);	
		System.out.println(requestType + " -- " + projId + " -- " + fetchParam + " -- " +filterList);
	}
	


	/**
	 * Returns list of query result based on query parameters supplied using request info.
	 * @param requestInfo Information related to request.
	 * @return list of query result if successful.
	 * @throws DefectException
	 */
	private List<String> getList(RequestInfo requestInfo) throws DefectException {
		log.info("Start - getList");
		createInstance();
		List<String> lstResult = new ArrayList<String>();
		if(CommonUtil.isNull(requestInfo)){
			log.error("Request Info is null - "+ requestInfo);			
			throw new DefectException("failed to get list, request information is missing");
		}
		
        try {

            log.info("Querying for top highest priority unfixed defects...");
            System.out.println("Query Request Info " + requestInfo.getQueryFilter());
            QueryResponse qryResponse = getQueryResponse(requestInfo);
            
            
            if (qryResponse.wasSuccessful()) {
            	log.info(String.format("\nTotal results: %d", qryResponse.getTotalResultCount()));
                for (JsonElement result : qryResponse.getResults()) {
                    JsonObject qryObject = result.getAsJsonObject();
                    String strFetch[] = requestInfo.getFetch();
                    String strPrintInfo = "";
                    if(strFetch != null && strFetch.length != 0){
                    	for(int i=0; i < strFetch.length; i++){
                    		strPrintInfo = strPrintInfo + strFetch[i] + " : " + qryObject.get(strFetch[i]).getAsString() + " ";
                    	}
                    }
                    log.info(strPrintInfo);
                    lstResult.add(strPrintInfo);
                }
            } else {
                for (String err : qryResponse.getErrors()) {
                    System.err.println("\t" + err);
                    lstResult.add(err);
                }
                log.error("Error in getting list : " + lstResult);
                throw new DefectException("Error in getting list" + lstResult);
            }
        } catch (Exception e){
            log.error("Error in getting list, message : " + e.toString());
            throw new DefectException("Error in getting list, message : " + e.toString());
        } finally {
        	closeDefect();
    		log.info("End - getList");
        }
		return lstResult;
    }
	
	/**
	 * Creates TestCase,Defects with or with out attachment etc.
	 * @param requestInfo
	 * @throws DefectException
	 */
	private void create(RequestInfo requestInfo) throws DefectException {
		log.info("Start - create");
		createInstance();
		if(CommonUtil.isNull(requestInfo)){
			log.error("Request Info is null - "+ requestInfo);			
			throw new DefectException("failed to create, request information is missing");
		}
        try {
        	JsonObject newEntry = convertMapToJson(requestInfo.getEntry().get(RallyConstants.CREATE));
	        CreateRequest createRequest = new CreateRequest(requestInfo.getRequestType(), newEntry);
	        CreateResponse createResponse = null;
			try {
				createResponse = restApi.create(createRequest);
			} catch (IOException e) {
				log.error("failed to create new entry, message : " + e.toString() + "RallyInfo -" + rallyInfo);
	        	throw new DefectException("failed to create new entry, message : " + e.toString());
			}
			
            if (createResponse.wasSuccessful()){
            	log.info(String.format("Created %s", createResponse.getObject().get(RallyConstants._REF).getAsString()));
            	String ref = createResponse.getObject().get(RallyConstants._REF).getAsString();

            	if(requestInfo.isAttachmentPresent()){
            		String fullImageFile = requestInfo.getImgFilePath() + requestInfo.getImgFileName();
		            	
            		if(CommonUtil.isNull(fullImageFile)){
            			log.error("failed to create an attachment, image file information is missing");
            			throw new DefectException("failed to create an attachment, image file information is missing");
            		}
		            		
            		String imageBase64String;
            		long attachmentSize;
		
            		// Open file
            		RandomAccessFile myImageFileHandle = null;
            		try {
            			myImageFileHandle = new RandomAccessFile(fullImageFile, requestInfo.getAccessMode());
            		} catch (FileNotFoundException e) {
            			log.error("failed to create an attachment, image file not found");
            			throw new DefectException("failed to create an attachment, image file not found");
            		}
								
            		long length = 0;
            		try {
            			length = myImageFileHandle.length();
		
            			// Max upload size for Rally attachments is 5MB
            			long maxAttachmentLength = 5120000;
            			if (length > maxAttachmentLength) 
            				throw new DefectException("File size too big for Rally attachment, > 5 MB");
		
            			// Read file and return data
            			byte[] fileBytes = new byte[(int) length];
            			myImageFileHandle.readFully(fileBytes);
                		imageBase64String = Base64.encodeBase64String(fileBytes);
		            	attachmentSize = length;

            		} catch (IOException e) {
            			log.error("failed to create an attachment, image file not readable message : "+e.toString());
            			throw new DefectException("failed to create an attachment, image file not readable");
            		}finally{
		            	try {
		            		myImageFileHandle.close();
		            	} catch (IOException e) {
	            			log.error("failed to create an attachment, error closing image file handle message: "+e.toString());
	            			throw new DefectException("failed to create an attachment, error closing image file handle");
		            	}
            		}
		
            		// First create AttachmentContent from image string
							    
            		JsonObject myAttachmentContent = new JsonObject();
            		myAttachmentContent.addProperty(RallyConstants.CONTENT, imageBase64String);
		            	
            		CreateRequest attachmentContentCreateRequest = new CreateRequest(RallyConstants.ATTACHMENTCONTENT, myAttachmentContent);
            		CreateResponse attachmentContentResponse = null;
            		
            		try {
            			attachmentContentResponse = restApi.create(attachmentContentCreateRequest);
					} catch (IOException e1) {
            			log.error("failed to create an attachment content request, message: "+e1.toString());
            			throw new DefectException("failed to create an attachment content request");
					}
							    
            		String myAttachmentContentRef = attachmentContentResponse.getObject().get(RallyConstants._REF).getAsString();
            		log.info("Attachment Content created: " + myAttachmentContentRef); 
		            	
            		// Now create the Attachment itself
            		JsonObject myAttachment = convertMapToJson(requestInfo.getEntry().get(RallyConstants.ATTACHMENT));
            		myAttachment.addProperty(RallyConstants.ARTIFACT, ref);
            		myAttachment.addProperty(RallyConstants.CONTENT, myAttachmentContentRef);
            		myAttachment.addProperty(RallyConstants.SIZE, attachmentSize);
		            	
            		CreateRequest attachmentCreateRequest = new CreateRequest(RallyConstants.ATTACHMENT, myAttachment);
            		CreateResponse attachmentResponse = null;

            		try {
            			attachmentResponse = restApi.create(attachmentCreateRequest);
            		} catch (IOException e) {
            			log.error("failed to create attachment, message : " + e.toString() + "RequestInfo -" + requestInfo);
            			throw new DefectException("failed to create attachment, message : " + e.toString());
            		}

            		String myAttachmentRef = attachmentResponse.getObject().get(RallyConstants._REF).getAsString();
            		log.info("Attachment  created: " + myAttachmentRef);  
		            	
            		if (attachmentResponse.wasSuccessful()) {
            			log.info("Successfully created Attachment");
            		} else {
            			List<String> lstErrors = new ArrayList<String>();
                        for (String err : attachmentResponse.getErrors()) {
                            System.err.println("\t" + err);
                            lstErrors.add(err);
                        }
                        log.error("Error in creating attachment : " + lstErrors);
                        throw new DefectException("Error in creating attachment :" + lstErrors);
            		}
            	}
            } else {
                List<String> lstResult = new ArrayList<String>();
                for (String err : createResponse.getErrors()) {
                    System.err.println("\t" + err);
                    lstResult.add(err);
                }
                log.error("Error in creating new entry in rally : " + lstResult);                
                throw new DefectException("Error in creating new entry in rally" + lstResult);
            }
        } finally {
        	closeDefect();
			log.info("End - create");
        }
	} 
	
	/**
	 * Updates test case or defect.
	 * @param requestInfo
	 * @throws DefectException
	 */
	private void update(RequestInfo requestInfo) throws DefectException {
		log.info("Start - update");
		createInstance();
		if(CommonUtil.isNull(requestInfo)){
			log.error("Request Info is null - "+ requestInfo);			
			throw new DefectException("failed to update, request information is missing");
		}
        try{

        	String ref = requestInfo.getRefField();

        	//Update
        	JsonObject updateEntry = convertMapToJson(requestInfo.getEntry().get(RallyConstants.UPDATE));	        
        	UpdateRequest updateRequest = new UpdateRequest(ref, updateEntry);
        	UpdateResponse updateResponse = null;

        	try {
   	   			updateResponse = restApi.update(updateRequest);
			} catch (IOException e) {
				log.error("failed to update, message : " + e.toString() + "RequestInfo -" + requestInfo);
	        	throw new DefectException("failed to update, message : " + e.toString());
			}

            if (updateResponse.wasSuccessful()) {
            	log.info(String.format("Updated %s", updateResponse.getObject().get(RallyConstants._REF).getAsString()));          
            } else {
                List<String> lstResult = new ArrayList<String>();
                for (String err : updateResponse.getErrors()) {
                    System.err.println("\t" + err);
                    lstResult.add(err);
                }
                log.error("Error in updating : " + lstResult);
                throw new DefectException("Error in updating : " + lstResult);
            }
        } finally {
        	closeDefect();
        	log.info("End - update");
        }
   	}

   	/**
   	 * Delete Test case or Defect.
   	 * TODO: create a wrapper method 'deleteDefectBuilder' for this method
   	 * @param requestInfo
   	 * @throws DefectException
   	 */
   	private void delete(RequestInfo requestInfo) throws DefectException {
		log.info("Start - delete");
		createInstance();
		if(CommonUtil.isNull(requestInfo)){
			log.error("Request Info is null - "+ requestInfo);			
			throw new DefectException("failed to create, request information is missing");
		}
        try{

        	
        	String ref = getReference(requestInfo);

        	//Delete
            DeleteRequest deleteRequest = new DeleteRequest(ref);
            DeleteResponse deleteResponse = null;

        	try {
        		deleteResponse = restApi.delete(deleteRequest);
			} catch (IOException e) {
				log.error("failed to delete, message : " + e.toString() + "RequestInfo -" + requestInfo);
	        	throw new DefectException("failed to delete, message : " + e.toString());
			}

            if (deleteResponse.wasSuccessful()) {
            	log.info("Deleted");          
            } else {
            	
                log.error("Error in update : ");
                List<String> lstResult = new ArrayList<String>();
                for (String err : deleteResponse.getErrors()) {
                    System.err.println("\t" + err);
                    lstResult.add(err);
                }
                log.error("Error in deleting : " + lstResult);
                throw new DefectException("Error in deleting" + lstResult);
            }
        } finally {
        	closeDefect();
        	log.info("End - delete");
        }
   	}

   	/**
   	 * Returns reference of user, workspace , project, tags
   	 * @param rallyInfo Rally integration related information is stored.
   	 * @return reference of user, workspace , project, tags 
   	 */
   	private String getReference(RequestInfo requestInfo) throws DefectException { 
		log.info("Start - getReference");
		if(CommonUtil.isNull(requestInfo)){
			log.error("Request Info is null - "+ requestInfo);			
			throw new DefectException("failed to read, request information is missing");
		}
		if(CommonUtil.isNull(restApi))
			createInstance();
		String ref = null;
        try {

            log.info("Getting Query Response");
            
            QueryResponse qryResponse = getQueryResponse(requestInfo);
            
            log.info("Got Query Response : " + qryResponse);
            
            JsonArray qryResults = qryResponse.getResults();
            JsonElement qryElement = qryResults.get(0);
            JsonObject qryObject = qryElement.getAsJsonObject();
            ref = qryObject.get((requestInfo.getRefField() != null)?requestInfo.getRefField():RallyConstants._REF).toString();

        } finally {
        	closeDefect();
    		log.info("End - getReference");
        }
		return ref;
   	}
   	
   	/**
   	 * Returns query response based on query request.
   	 * @param qryRequestInfo Query Request information is stored.
   	 * @return query response 
   	 */
	private QueryResponse getQueryResponse(RequestInfo qryRequestInfo) throws DefectException{
		
		if(CommonUtil.isNull(qryRequestInfo)){
			log.error("failed to make query request, Query Request Info-" + qryRequestInfo);
        	throw new DefectException("failed to make query request, required query request info is missing");
		}
		
		// Request type can be User,defect etc.
		QueryRequest qryRequest = new QueryRequest(qryRequestInfo.getRequestType());
		
		log.info("Request Type : " + qryRequestInfo.getRequestType());
		
		qryRequest.setFetch(new Fetch(qryRequestInfo.getFetch()));
		try{
			if(qryRequestInfo.getQueryFilter() != null){
				QueryFilter qfilter = null;
				ArrayList<String> filterList = qryRequestInfo.getQueryFilter();
				if(!filterList.isEmpty() && filterList.size() >= 3){
					if((filterList.get(2)==null)||(filterList.get(2).length() ==0)){
						filterList.set(2,"null");
					}
					qfilter = new QueryFilter(filterList.get(0), filterList.get(1), filterList.get(2));
					for(int i=3;i+3<filterList.size();i=i+4){
						if(filterList.get(i) == "AND"){
							qfilter = QueryFilter.and(qfilter, new QueryFilter(filterList.get(i + 1), filterList.get(i + 2), filterList.get(i + 3)));
						}else if(filterList.get(i) == "OR"){
							qfilter = QueryFilter.or(qfilter, new QueryFilter(filterList.get(i + 1), filterList.get(i + 2), filterList.get(i + 3)));
						}else{
							throw new DefectException("Invalid query filter string.");
						}
					}
				}
				qryRequest.setQueryFilter(qfilter);
			}
		}catch(IndexOutOfBoundsException e){
			log.error(e.getMessage());
		}catch(NullPointerException e){
			log.error(e.getMessage());
		}
		if(qryRequestInfo.getQueryOrder() != null){
			qryRequest.setOrder(convertMapToString(qryRequestInfo.getQueryOrder()));
		}
	
		if(qryRequestInfo.getPageSize() != -1)
			qryRequest.setPageSize(qryRequestInfo.getPageSize());
			
		if(qryRequestInfo.getLimit() != -1)			
			qryRequest.setPageSize(qryRequestInfo.getLimit());
		
		QueryResponse qryResponse = null;
		try {
			qryResponse = restApi.query(qryRequest);
		} catch (IOException e) {
			log.error("failed to query , message : " + e.toString() + " Query Response -" + qryResponse);
        	throw new DefectException("failed to query , message : " + e.toString());
		}
		
		return qryResponse;
	}	      	

   	
	/**
	 * Connect to rally instance.
	 * @throws DefectException
	 */
	private void createInstance() throws DefectException{
		try {
			restApi = new RallyRestApi(new URI(rallyInfo.getUrl()), rallyInfo.getUserName(), rallyInfo.getPassword());
		} catch (URISyntaxException e) {
			log.error("failed to connect to rally instance, message : " + e.toString() + "RallyInfo -" + rallyInfo);
			throw new DefectException("failed to connect to rally instance, message : " + e.toString());        	
		}
		if(rallyInfo.getWsapiVersion() != null)
			restApi.setWsapiVersion(rallyInfo.getWsapiVersion());
		restApi.setApplicationName(rallyInfo.getAppName());
        
	}
	
	private JsonObject convertMapToJson(Map<String,String> entry){
		JsonObject jsonObj = new JsonObject();
		Set<String> keySet = entry.keySet();
		
		for(String key : keySet){
			jsonObj.addProperty(key, entry.get(key));
		}
		return jsonObj;
	}
	
	private String convertMapToString(Map<String,String> map){
		String str = "";
		Set<String> keySet = map.keySet();
		int count = 1;
		for(String key : keySet){
			str = str + key + " " + map.get(key);
			if(keySet.size() != count)
				str = str + ",";
			count++;
		}
		return str;
	}

	
}