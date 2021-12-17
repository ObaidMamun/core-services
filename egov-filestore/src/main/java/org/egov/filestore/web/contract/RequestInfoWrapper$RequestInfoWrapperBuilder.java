/*    */ package org.egov.filestore.web.contract;
/*    */ 
/*    */ import org.egov.common.contract.request.RequestInfo;
/*    */ 
/*    */ 
/*    */ 
/*    */ 
/*    */ public class RequestInfoWrapperBuilder
/*    */ {
/*    */   private RequestInfo requestInfo;
/*    */   
/*    */   public RequestInfoWrapperBuilder requestInfo(RequestInfo requestInfo) {
/* 13 */     this.requestInfo = requestInfo; return this; } public RequestInfoWrapper build() { return new RequestInfoWrapper(this.requestInfo); } public String toString() { return "RequestInfoWrapper.RequestInfoWrapperBuilder(requestInfo=" + this.requestInfo + ")"; }
/*    */ 
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\web\contract\RequestInfoWrapper$RequestInfoWrapperBuilder.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */