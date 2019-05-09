package ts.serviceImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javafx.scene.control.Tab;
import ts.daoImpl.CustomerInfoDao;
import ts.daoImpl.ExpressSheetDao;
import ts.daoImpl.PackageRouteDao;
import ts.daoImpl.TransHistoryDao;
import ts.daoImpl.TransPackageContentDao;
import ts.daoImpl.TransPackageDao;
import ts.daoImpl.UserInfoDao;
import ts.daoImpl.UsersPackageDao;
import ts.model.CustomerInfo;
import ts.model.ExpressSheet;
import ts.model.PackageRoute;
import ts.model.TransHistory;
import ts.model.TransPackage;
import ts.model.TransPackageContent;
import ts.model.UserInfo;
import ts.model.UsersPackage;
import ts.serviceInterface.IDomainService;

public class DomainService implements IDomainService {

    private ExpressSheetDao expressSheetDao;
    private TransPackageDao transPackageDao;
    private TransHistoryDao transHistoryDao;
    private TransPackageContentDao transPackageContentDao;
    private PackageRouteDao packageRouteDao;
    private UserInfoDao userInfoDao;
    private UsersPackageDao usersPackageDao;

    public UsersPackageDao getUsersPackageDao() {
        return usersPackageDao;
    }

    public void setUsersPackageDao(UsersPackageDao dao) {
        this.usersPackageDao = dao;
    }

    public PackageRouteDao getPackageRouteDao() {
        return packageRouteDao;
    }

    public void setPackageRouteDao(PackageRouteDao dao) {
        this.packageRouteDao = dao;
    }

    public ExpressSheetDao getExpressSheetDao() {
        return expressSheetDao;
    }

    public void setExpressSheetDao(ExpressSheetDao dao) {
        this.expressSheetDao = dao;
    }

    public TransPackageDao getTransPackageDao() {
        return transPackageDao;
    }

    public void setTransPackageDao(TransPackageDao dao) {
        this.transPackageDao = dao;
    }

    public TransHistoryDao getTransHistoryDao() {
        return transHistoryDao;
    }

    public void setTransHistoryDao(TransHistoryDao dao) {
        this.transHistoryDao = dao;
    }

    public TransPackageContentDao getTransPackageContentDao() {
        return transPackageContentDao;
    }

    public void setTransPackageContentDao(TransPackageContentDao dao) {
        this.transPackageContentDao = dao;
    }

    public UserInfoDao getUserInfoDao() {
        return userInfoDao;
    }

    public void setUserInfoDao(UserInfoDao dao) {
        this.userInfoDao = dao;
    }

    public Date getCurrentDate() {
        // ����һ�����������ʱ��,��Ȼ,SQLʱ���JAVAʱ���ʽ��һ��
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date tm = new Date();
        try {
            tm = sdf.parse(sdf.format(new Date()));
        } catch (ParseException e1) {
            e1.printStackTrace();
        }
        return tm;
    }

    // ��ÿ���б�
    @Override
    public List<ExpressSheet> getExpressList(String property, String restrictions, String value) {
        List<ExpressSheet> list = new ArrayList<ExpressSheet>();
        switch (restrictions.toLowerCase()) {
        case "eq":
            list = expressSheetDao.findBy(property, value, "ID", true);
            break;
        case "like":
            list = expressSheetDao.findLike(property, value + "%", "ID", true);
            break;
        }
        return list;
    }
//	@Override
//	public List<ExpressSheet> getExpressList(String property,
//			String restrictions, String value) {
//		Criterion cr1;
//		Criterion cr2 = Restrictions.eq("Status", 0);
//
//		List<ExpressSheet> list = new ArrayList<ExpressSheet>();
//		switch(restrictions.toLowerCase()){
//		case "eq":
//			cr1 = Restrictions.eq(property, value);
//			break;
//		case "like":
//			cr1 = Restrictions.like(property, value);
//			break;
//		default:
//			cr1 = Restrictions.like(property, value);
//			break;
//		}
//		list = expressSheetDao.findBy("ID", true,cr1,cr2);		
//		return list;
//	}

    // ��ð����е����п�ݵ�
    @Override
    public List<ExpressSheet> getExpressListInPackage(String packageId) {
        List<ExpressSheet> list = new ArrayList<ExpressSheet>();
        list = expressSheetDao.getListInPackage(packageId);
        return list;
    }

    // ��ÿ�ݵ�
    @Override
    public Response getExpressSheet(String id) {
        ExpressSheet es = expressSheetDao.get(id);
        return Response.ok(es).header("EntityClass", "ExpressSheet").build();
    }

    // �½���ݵ�
    @Override
    public Response newExpressSheet(String id, int uid) {
        ExpressSheet es = null;
        try {
            es = expressSheetDao.get(id);
        } catch (Exception e1) {
        }

        if (es != null) {
//			if(es.getStatus() != 0)
//				return Response.ok(es).header("EntityClass", "L_ExpressSheet").build(); //�Ѿ�����,�Ҳ��ܸ���
//			else
//            return Response.ok("����˵���Ϣ�Ѿ�����!\n�޷�����!").header("EntityClass", "E_ExpressSheet").build(); // �Ѿ�����
        }
        try {
            String pkgId = userInfoDao.get(uid).getReceivePackageID();
            ExpressSheet nes = new ExpressSheet();
            nes.setID(id);
            nes.setType(0);
            nes.setAccepter(String.valueOf(uid));
            nes.setAccepteTime(getCurrentDate());
            nes.setStatus(ExpressSheet.STATUS.STATUS_CREATED);
//			TransPackageContent pkg_add = new TransPackageContent();
//			pkg_add.setPkg(transPackageDao.get(pkgId));
//			pkg_add.setExpress(nes);
//			nes.getTransPackageContent().add(pkg_add);
            expressSheetDao.save(nes);
            // �ŵ��ռ�������
            MoveExpressIntoPackage(nes.getID(), pkgId);
            return Response.ok(nes).header("EntityClass", "ExpressSheet").build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // �����ݵ�
    @Override
    public Response saveExpressSheet(ExpressSheet obj) {
        try {
            String id = obj.getID();
            if (id == null || id.length() == 0) {
                // ���� ExpressSheet ��ID
                while (true) {
                    id = String.valueOf(System.currentTimeMillis());
                    if (expressSheetDao.get(id) == null) {
                        obj.setID(id);
                        break;
                    }
                }
            }

            if (obj.getStatus() != ExpressSheet.STATUS.STATUS_CREATED) {
                return Response.ok("����˵��Ѹ���!�޷��������!").header("EntityClass", "E_ExpressSheet").build();
            }
            expressSheetDao.save(obj);
            return Response.ok(obj).header("EntityClass", "R_ExpressSheet").build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // ���տ��
    @Override
    public Response receiveExpressSheet(ExpressSheet es, int uid) {
        // System.out.println("sss" + es + "dddd" + uid);
        try {
            if (es.getStatus() != ExpressSheet.STATUS.STATUS_CREATED) {
                return Response.ok("����˵�״̬����!�޷��ռ�!").header("EntityClass", "ER_ExpressSheet").build();
            }
            es.setAccepter(String.valueOf(uid));
            es.setAccepteTime(getCurrentDate());
            es.setStatus(ExpressSheet.STATUS.STATUS_COLLECT);// System.out.println(es);
            expressSheetDao.update(es);

            TransPackageContent transPackageContent = new TransPackageContent();
            transPackageContent.setExpress(es);
            transPackageContent.setPkg(transPackageDao.get(userInfoDao.get(uid).getReceivePackageID()));
            transPackageContent.setStatus(TransPackage.STATUS.STATUS_COLLECT);
            transPackageContentDao.save(transPackageContent);

            return Response.ok(es).header("EntityClass", "R_ExpressSheet").build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // �ַ����
    @Override
    public Response DispatchExpressSheet(String id, int uid) {
        // TODO Auto-generated method stub
        return null;
    }

    // ����������
    public boolean MoveExpressIntoPackage(String id, String targetPkgId) {
        TransPackage targetPkg = transPackageDao.get(targetPkgId);
        if ((targetPkg.getStatus() > 0) && (targetPkg.getStatus() < 5)) { // ������״̬��㶨��,�򿪵İ������߻������ܲ���==================================================================
            return false;
        }

        TransPackageContent pkg_add = new TransPackageContent();
        pkg_add.setPkg(targetPkg);
        pkg_add.setExpress(expressSheetDao.get(id));
        pkg_add.setStatus(TransPackageContent.STATUS.STATUS_ACTIVE);
        transPackageContentDao.save(pkg_add);
        return true;
    }

    // ����Ӱ������Ƴ�
    public boolean MoveExpressFromPackage(String id, String sourcePkgId) {
        TransPackage sourcePkg = transPackageDao.get(sourcePkgId);
        if ((sourcePkg.getStatus() > 0) && (sourcePkg.getStatus() < 3)) {
            return false;
        }

        TransPackageContent pkg_add = transPackageContentDao.get(id, sourcePkgId);
        pkg_add.setStatus(TransPackageContent.STATUS.STATUS_OUTOF_PACKAGE);
        transPackageContentDao.save(pkg_add);
        return true;
    }

    // ����ڰ������ƶ�
    public boolean MoveExpressBetweenPackage(String id, String sourcePkgId, String targetPkgId) {
        // ��Ҫ�����������
        MoveExpressFromPackage(id, sourcePkgId);
        MoveExpressIntoPackage(id, targetPkgId);
        return true;
    }

    // �ύ��ݵ�ID
    @Override
    public Response DeliveryExpressSheetId(String id, int uid) {
        try {
            String pkgId = userInfoDao.get(uid).getDelivePackageID();
            ExpressSheet nes = expressSheetDao.get(id);
            if (nes.getStatus() != ExpressSheet.STATUS.STATUS_TRANSPORT) {
                return Response.ok("����˵�״̬����!�޷�����").header("EntityClass", "E_ExpressSheet").build();
            }

            if (transPackageContentDao.getSn(id, pkgId) == 0) {
                // ��ʱ��һ������ʽ,��·�˰����Ĵ��ݹ���,�Լ��Ļ�������һ��
                MoveExpressBetweenPackage(id, userInfoDao.get(uid).getReceivePackageID(), pkgId);
                return Response.ok("����˵�״̬����!\n�����Ϣû�������ɼ�������!").header("EntityClass", "E_ExpressSheet").build();
            }

            nes.setDeliver(String.valueOf(uid));
            nes.setDeliveTime(getCurrentDate());
            nes.setStatus(ExpressSheet.STATUS.STATUS_DELIVERY);
            expressSheetDao.save(nes);
            // ���ɼ�������ɾ��
            MoveExpressFromPackage(nes.getID(), pkgId);
            // ���û����ʷ��¼,���Ѹ����ռ��ͽ����ļ�¼
            return Response.ok(nes).header("EntityClass", "ExpressSheet").build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // ��ð����б� �д��󣬻�ѭ������
    @Override
    public List<TransPackage> getTransPackageList(String property, String restrictions, String value) {
        List<TransPackage> list = new ArrayList<TransPackage>();
        switch (restrictions.toLowerCase()) {
        case "eq":
            list = transPackageDao.findBy(property, value, "ID", true);
            break;
        case "like":
            list = transPackageDao.findLike(property, value + "%", "ID", true);
            break;
        }
        return list;
    }

    // ��ð���
    @Override
    public Response getTransPackage(String id) {
        TransPackage es = transPackageDao.get(id);
        return Response.ok(es).header("EntityClass", "TransPackage").build();
    }

    // �½�����
    @Override
    public Response newTransPackage(String id, int uid) {
        try {
            TransPackage npk = new TransPackage();
            npk.setID(id);
            // npk.setStatus(value);
            npk.setCreateTime(new Date());
            transPackageDao.save(npk);
            return Response.ok(npk).header("EntityClass", "TransPackage").build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // �������
    @Override
    public Response saveTransPackage(TransPackage obj) {
        try {
            transPackageDao.save(obj);
            return Response.ok(obj).header("EntityClass", "R_TransPackage").build();
        } catch (Exception e) {
            return Response.serverError().entity(e.getMessage()).build();
        }
    }

    // ��ð���������Ϣ
    @Override
    public List<PackageRoute> getPackageRouteList(String packageID) {
        return packageRouteDao.getPackageRouteList(packageID);
    }

    // ���ð���������Ϣ
    public Response setPackageRoute(String packageID, float x, float y) {
        PackageRoute packageRoute = new PackageRoute();
        packageRoute.setPkg(transPackageDao.get(packageID));
        packageRoute.setX(x);
        packageRoute.setY(y);
        packageRoute.setTm(getCurrentDate());
        packageRouteDao.save(packageRoute);
        return Response.ok("ok").header("EntityClass", "PackageRoute").build();
    }

    // ����UID���receivePackageID
    public Response getReceivePackageID(String UID) {
        // ����receivePackageIDΪ UID + ʱ�� ��23λ
        String receivePackageID = new StringBuilder(UID).append(System.currentTimeMillis()).toString();
        // ����userinfo���receivePackageID, URull������DptID
        String dptID = userInfoDao.setReceivePackageID(UID, receivePackageID);
        System.out.println(dptID);
        // ����transpackage����ֵID��TargetNode(=DptID) ������
        TransPackage transPackage = new TransPackage();
        transPackage.setID(receivePackageID);
        transPackage.setTargetNode(dptID);
        transPackage.setStatus(0);
        transPackage.setCreateTime(getCurrentDate());
        transPackageDao.save(transPackage);
        // ����userspackage
        UsersPackage usersPackage = new UsersPackage();
        usersPackage.setUserU(userInfoDao.get(Integer.parseInt(UID)));
        usersPackage.setPkg(transPackage);
        usersPackageDao.save(usersPackage);

        return Response.ok(receivePackageID).header("EntityClass", "ReceivePackageID").build();
    }

    // ������Ա����PackageID
    public Response cleanPackageID(String UID, String flag) {
        UserInfo userInfo = userInfoDao.get(Integer.parseInt(UID));
        userInfo.setURull(0);
        if (String.valueOf(flag.charAt(0)).equals("1")) {
            userInfo.setReceivePackageID(null);
        }
        if (String.valueOf(flag.charAt(1)).equals("1")) {
            userInfo.setDelivePackageID(null);
        }
        if (String.valueOf(flag.charAt(2)).equals("1")) {
            userInfo.setTransPackageID(null);
        }
        userInfoDao.update(userInfo);
        return Response.ok("ɾ���ɹ�").header("EntityClass", "PackageID").build();

    }

    // ���Ա����ӿ�
    public Response unpacking(String UID, String PackageID, float x, float y) {
        //����packageID��ȡ������ԱUID
        int lastUID = usersPackageDao.getUIDByPackageID(PackageID);
        //���ò����Ա
        UserInfo userInfo = userInfoDao.get(Integer.parseInt(UID));
        userInfo.setDelivePackageID(PackageID);
        userInfo.setURull(UserInfo.URull.URull_UNPACKING);
        userInfoDao.update(userInfo);
        //���İ���״̬
        TransPackage transPackage = transPackageDao.get(PackageID);
        transPackage.setStatus(TransPackage.STATUS.STATUS_SORTING);
        transPackageDao.update(transPackage);
        //��ӵ�usersPackage
        UsersPackage usersPackage = new UsersPackage();
        usersPackage.setUserU(userInfo);
        usersPackage.setPkg(transPackage);
        usersPackageDao.save(usersPackage);
        //��ӵ�transHistory
        TransHistory transHistory = new TransHistory();
        transHistory.setPkg(transPackage);
        transHistory.setActTime(getCurrentDate());
        transHistory.setUIDFrom(lastUID);
        transHistory.setUIDTo(Integer.parseInt(UID));
        transHistory.setX(x);
        transHistory.setY(y);
        transHistoryDao.save(transHistory);
        
        return Response.ok("����ɹ�").header("EntityClass", "unpacking").build();
    }
}
