package ts.daoImpl;


import ts.daoBase.BaseDao;
import ts.model.UserInfo;

public class UserInfoDao extends BaseDao<UserInfo, Integer> {
    public UserInfoDao() {
        super(UserInfo.class);
    }

    public UserInfo get(int id) {
        UserInfo userInfo;
        try {
            userInfo = super.get(id);
        } catch (Exception e) {
            return null;
        }
        return userInfo;
    }

    public String setReceivePackageID(String UID, String receivePackageID) {
        UserInfo userInfo = get(Integer.parseInt(UID));
        userInfo.setReceivePackageID(receivePackageID);
        userInfo.setURull(1);
        super.update(userInfo);
        return userInfo.getDptID();
    }
    
}
