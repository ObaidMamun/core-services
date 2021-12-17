/*    */ package org.egov.filestore.web.contract;
/*    */ 
/*    */ import org.egov.common.contract.request.RequestInfo;
/*    */ 
/*    */ public class RequestInfoWrapper
/*    */ {
/*    */   private RequestInfo requestInfo;
/*    */   
/*    */   public void setRequestInfo(RequestInfo requestInfo) {
/* 10 */     this.requestInfo = requestInfo; } public boolean equals(Object o) { if (o == this) return true;  if (!(o instanceof RequestInfoWrapper)) return false;  RequestInfoWrapper other = (RequestInfoWrapper)o; if (!other.canEqual(this)) return false;  Object this$requestInfo = getRequestInfo(), other$requestInfo = other.getRequestInfo(); return !((this$requestInfo == null) ? (other$requestInfo != null) : !this$requestInfo.equals(other$requestInfo)); } protected boolean canEqual(Object other) { return other instanceof RequestInfoWrapper; } public int hashCode() { int PRIME = 59; result = 1; Object $requestInfo = getRequestInfo(); return result * 59 + (($requestInfo == null) ? 43 : $requestInfo.hashCode()); } public String toString() { return "RequestInfoWrapper(requestInfo=" + getRequestInfo() + ")"; }
/*    */    public RequestInfoWrapper() {}
/* 12 */   public RequestInfoWrapper(RequestInfo requestInfo) { this.requestInfo = requestInfo; }
/* 13 */   public static RequestInfoWrapperBuilder builder() { return new RequestInfoWrapperBuilder(); } public static class RequestInfoWrapperBuilder { public RequestInfoWrapperBuilder requestInfo(RequestInfo requestInfo) { this.requestInfo = requestInfo; return this; } private RequestInfo requestInfo; public RequestInfoWrapper build() { return new RequestInfoWrapper(this.requestInfo); } public String toString() { return "RequestInfoWrapper.RequestInfoWrapperBuilder(requestInfo=" + this.requestInfo + ")"; }
/*    */      }
/*    */   public RequestInfo getRequestInfo() {
/* 16 */     return this.requestInfo;
/*    */   }
/*    */ }


/* Location:              C:\Odisha\Docker-prod-filestore\egov-filestore\BOOT-INF\classes\!\org\egov\filestore\web\contract\RequestInfoWrapper.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */