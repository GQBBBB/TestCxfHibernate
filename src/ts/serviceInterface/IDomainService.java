package ts.serviceInterface;

import java.util.HashSet;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import ts.model.ExpressSheet;
import ts.model.PackageRoute;
import ts.model.TransPackage;
import ts.model.UserInfo;

@Path("/Domain") // 业务操作
public interface IDomainService {
    // 快件操作访问接口=======================================================================
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getExpressList/{Property}/{Restrictions}/{Value}")
    public List<ExpressSheet> getExpressList(@PathParam("Property") String property,
            @PathParam("Restrictions") String restrictions, @PathParam("Value") String value);

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getExpressListInPackage/PackageId/{PackageId}")
    public List<ExpressSheet> getExpressListInPackage(@PathParam("PackageId") String packageId);

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getExpressSheet/{id}")
    public Response getExpressSheet(@PathParam("id") String id);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/newExpressSheet/id/{id}/uid/{uid}")
    public Response newExpressSheet(@PathParam("id") String id, @PathParam("uid") int uid);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/saveExpressSheet")
    public Response saveExpressSheet(ExpressSheet obj);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/receiveExpressSheet/{uid}")
    public Response receiveExpressSheet(ExpressSheet es, @PathParam("uid") int uid);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/dispatchExpressSheetId/id/{id}/uid/{uid}")
    public Response DispatchExpressSheet(@PathParam("id") String id, @PathParam("uid") int uid);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/deliveryExpressSheetId/id/{id}/uid/{uid}")
    public Response DeliveryExpressSheetId(@PathParam("id") String id, @PathParam("uid") int uid);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getExpressSheetByTelCode/{telCode}")
    public HashSet<ExpressSheet> getExpressSheetByTelCode(@PathParam("telCode") String telCode);

    // 包裹操作访问接口=======================================================================
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getTransPackageList/{Property}/{Restrictions}/{Value}")
    public List<TransPackage> getTransPackageList(@PathParam("Property") String property,
            @PathParam("Restrictions") String restrictions, @PathParam("Value") String value);

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getTransPackage/{id}")
    public Response getTransPackage(@PathParam("id") String id);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/newTransPackage")
    public Response newTransPackage(String id, int uid);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Path("/saveTransPackage")
    public Response saveTransPackage(TransPackage obj);

    // 包裹坐标信息操作访问接口=======================================================================
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getPackageRouteList/{PackageID}")
    public List<PackageRoute> getPackageRouteList(@PathParam("PackageID") String packageID);

    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/setPackageRoute/{PackageID}/{x}/{y}")
    public Response setPackageRoute(@PathParam("PackageID") String packageID, @PathParam("x") float x,
            @PathParam("y") float y);

    // 快递员揽收包裹访问接口=======================================================================
    @GET
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getReceivePackageID/{UID}/{URull}")
    public Response getReceivePackageID(@PathParam("UID") String UID, @PathParam("URull") int URull);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/cleanPackageID/{UID}/{flag}")
    public Response cleanPackageID(@PathParam("UID") String UID, @PathParam("flag") String flag);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getTaskList/{UID}")
    public List<ExpressSheet> getTaskList(@PathParam("UID") String UID);

    // 快递员拆包接口=======================================================================
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/unpacking/{UID}/{PackageID}/{x}/{y}")
    public Response unpacking(@PathParam("UID") String UID, @PathParam("PackageID") String PackageID,
            @PathParam("x") float x, @PathParam("y") float y);

    // 快件从包裹中移除
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/MoveExpressFromPackage/{expressSheetID}/{sourcePkgId}")
    public Response MoveExpressFromPackage(@PathParam("expressSheetID") String expressSheetID,
            @PathParam("sourcePkgId") String sourcePkgId);
    
    // 快件移入包裹
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/MoveExpressIntoPackage/{expressSheetID}/{targetPkgId}")
    public Response MoveExpressIntoPackage(@PathParam("expressSheetID") String expressSheetID,
            @PathParam("targetPkgId") String targetPkgId);

    // 快递员信息接口=======================================================================
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/changeUserInfo")
    public Response changeUserInfo(UserInfo userInfo);

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getUserInfoList")
    public List<UserInfo> getUserInfoList();

    // 运输员获得根据ID获得转运包裹对象
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces({ MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON })
    @Path("/getTransportPackage/{UID}/{PackageId}")
    public Response getTransportPackage(@PathParam("UID") int UID, @PathParam("PackageId") String PackageId);

}
