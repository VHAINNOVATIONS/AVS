package gov.va.med.lom.vistabroker.patient.dao;

import gov.va.med.lom.javaUtils.misc.StringUtils;
import gov.va.med.lom.vistabroker.dao.BaseDao;
import gov.va.med.lom.vistabroker.exception.VistaBrokerException;
import gov.va.med.lom.vistabroker.patient.data.VistaImageInfo;

import java.util.List;
import java.util.HashMap;

public class VistaImagingDao extends BaseDao {
  
  // CONSTRUCTORS
  public VistaImagingDao() {
    super();
  }
  
  public VistaImagingDao(BaseDao baseDao) {
    super(baseDao);
  }
  
  /*
    Steps for uploading an image to the VI server:

    1. use RPC call TIU CREATE RECORD and get back IEN of created note
    2. use RPC call MAG4 ADD IMAGE with patient information and image
       information.  Get back a file path and file name where
       image should be saved to.  Also get back IEN in IMAGE file
       of stored image metainfo.
    3. use RPC call MAG3 TIU IMAGE with IMAGE IEN, and note IEN
    4. save image to the file path and name given by the server.   
  */
  
  // RPC API
  
  public VistaImageInfo addVistaImage(String dfn, String duz, String fileExt) throws VistaBrokerException {
    
    setDefaultContext("MAG WINDOWS");
    setDefaultRpcName("MAG4 ADD IMAGE");
    
    HashMap<String, String> map = new HashMap<String, String>();
    map.put("\"OBJTYPE\"", "3^15");
    map.put("\"FileExt\"", "EXT^" + fileExt);
    map.put("\"DUZ\"", "8^" + duz);
    map.put("\"DATETIME\"", "7^NOW");
    map.put("\"magDFN\"", "5^" + dfn);
    Object[] params = {map};
    String x = sCall(params);
    
    VistaImageInfo vistaImageInfo = new VistaImageInfo();
    vistaImageInfo.setImageIen(StringUtils.piece(x, 1));
    vistaImageInfo.setLocation(StringUtils.piece(x, 2));
    vistaImageInfo.setFilename( StringUtils.piece(x, 3));

    return vistaImageInfo;
    
  }
  
  public String linkNoteToImage(String imageIen, String tiuNoteIen) throws VistaBrokerException {
  
    setDefaultContext("MAG WINDOWS");
    setDefaultRpcName("MAG3 TIU IMAGE");
    
    Object[] params = {imageIen, tiuNoteIen};
    String x = sCall(params);
    
    boolean success = StringUtils.strToBool(StringUtils.piece(x, 1), "1");
    String message = StringUtils.piece(x, 2);
    
    if (success) {
      return "1";
    } else {
      return message;
    }
    
  }
    
  /*
   This RPC accepts an array as the only input parameter. The input array used in the RPC call
   is composed of two pieces of data – a code identifying the kind of data to follow and the 
   data itself. The codes used to identify the type of data correspond to the entries in the 
   table in the Input Array section.
   
   A node in the input array would look like: MAGDATA(n)=CODE^DATA
   
   MAGDATA(1)="IMAGE^\\ImageServer\ImageShare \MAX1.JPG^Image description"
   MAGDATA(2)="IMAGE^\\ImageServer\ImageShare \MAX2.JPG"
   MAGDATA(3)="IMAGE^\\ImageServer\ImageShare \MAX3.JPG^Image description"
   MAGDATA(4)="ACQD^COMPUTER CALLING RPC"
   MAGDATA(5)="ACQL^99"
   MAGDATA(6)="ACQS^688"
   MAGDATA(7)="DOCCTG^19"
   MAGDATA(8)="DOCDT^05/05/1999"
   MAGDATA(9)="IDFN^1033"
   MAGDATA(10)="STSCB^TESTCB^MAGGSIUI"
   MAGDATA(11)="TRKID^GK;101"
   
   Each 'IMAGE' entry is the full path of the Image using UNC notation, and optionally a 
   short description of the Image as the third ‘^’ delimited piece.
   
   Input Array Descriptions:
   
   ACQD - Acquisition Device [required]
   Acquisition Device name must be unique for each instrument at a site. Used by the Imaging 
   Package for statistical, tracking, and debugging purposes. For Windows systems, ACQD should 
   be the "computer name" of the capturing device. For non-Windows systems, "ACQD" should be 
   the domain name of the system.
   
   DFLG - Delete Flag
   This parameter must be sent if the calling application wants to delete the original image 
   file(s) after being processed and copied to the Imaging Network. Possible values: "1" means 
   delete the original image(s) after processing.

   DOCCTG - Document Category (Type)
   This parameter is an IEN in the MAG DESCRIPTIVE CATEGORIES file (#2005.81), and is used to 
   identify the type of images being imported.  When DOCCTG is used, DOCDT must be used as well. 
   If DOCCTG is not used, values must be defined for PXDT, PXIEN, and PXPKG.
   Example:
   30 CLIN  GEN
   12 CLIN  OTHER-MEDICAL
   
   IXTYPE - Image or Document TYPE
   This is a pointer to IMAGE INDEX FOR TYPES file (#2005.83) or the full name of the Index Type.
   
   IXSPEC - Image or Document SPECIALTY/SUBSPECIALTY
   Pointer to IMAGE INDEX FOR SPECIALTY/SUPSPECIALTY file (#2005.84) or the full name of the Index Specialty.
   
   IXPROC - Image or Document PROCEDURE/EVENT
   Pointer to IMAGE INDEX FOR PROCEDURE/EVENT file (#2005.85) or the full name of the Index Proc/Event.
   
   IXORIGIN - Image or Document ORIGIN
   Set of Codes. Possible values are: VA, NON-VA, DOD, FEE. If a value for this is not sent, it will default to VA.
   
   GDESC - Group Description
   A Short Description (60 character maximum) for the group of images. Imaging generates a default description 
   if this parameter is null. The default description consists of the Procedure Name and Procedure Date.
   
   ITYPE - Image Type
   Use ITYPE to specify an Image Type other than the default type assumed by VistA Imaging.
   Examples:
   100  DICOM IMAGE DCM
   103  TEXT  ASC
   104  ADOBE PDF
   Typically, VistA Imaging will use the file extension of an image to determine the Image Type.
   
   PXIEN - Procedure IEN
   The IEN of the procedure in the VistA Package File.
   
   PXPKG - Procedure PKG [required]
   The File number of the VistA Package to associated with the Image. Possible values of PXPKG are "8925" or "null".
   For the current version of the Import API, images can be associated only with TIU package or as an Image Category.
   
   PXNEW - Procedure New
   Procedure New is a flag that determines if a new Procedure Report will be created, or if an existing one will be used.
   
   PXTIUTTL - TIU Title IEN or Text
   External or internal value of TIU Title in TIU DOCUMENT DEFINITION file (#8925.1), the TIU Title of a new TIU note.
   
   PXSGNTYP - Signature Type
   Signature Type of a new TIU Note – unsigned or electronically filed.
   
   PXTIUTCNT - TIU Text Lines Counter
   Text Lines counter of a new TIU Note
   
   PXTIUTXTnnnnn – TIU Note Text
   Text Line in newly created TIU Note
   
   STSCB - Status Handler [required]
   The Status Handler must be "Tag^Routine" of an M routine that exists in VistA. This is always called to inform 
   the calling package of the Status of the Import process. A Status^Message, TrackingID, and Queue Number 
   are returned and possibly a list of warnings.
   
   TRKID - Tracking ID [required]
   This is a unique identifier passed by the calling package. It will be saved with the IMAGE file (#2005) entry. 
   If an error occurs during processing, causing the import to fail, this number is returned along with a status
   and message to the Status Handler Routine.
   
   Username and Password
   Separate parameters are passed for username and encrypted password, each consisting of a string value.
   
   Return Parameter is an Array
   Example  : Successful Queue
    MAGRY(0)="111^Data has been Queued."
          Queue Number ^ message is returned in the (0) node.  
    No other nodes are defined.
    Example: unsuccessful Queue
    MAGRY (0)="0^Required parameter is null"
    MAGRY (1)="Tracking ID is Required. !"
    MAGRY (2)="Status Handler is Required. !"
    MAGRY (3)="Acquisition Site is Required. !"
           node (0) = 0 '^' Error message
           node(1..n)= all error messages incurred during validation
   
  */
  public List<String> importVistaImage(List<String> nodeList) throws VistaBrokerException {
    
    setDefaultContext("MAG WINDOWS");
    setDefaultRpcName("MAG4 REMOTE IMPORT");
    
    HashMap<String, String> map = new HashMap<String, String>();
    int i = 0;
    for (String node : nodeList) {
      i++;
      map.put(String.valueOf(i), node);
    }
    Object[] params = {map};
    return lCall(params);
    
  }  
  
}
