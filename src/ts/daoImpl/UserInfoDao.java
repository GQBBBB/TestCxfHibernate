package ts.daoImpl;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;

import ts.daoBase.BaseDao;
import ts.model.CustomerInfo;
import ts.model.ExpressSheet;
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
        super.update(userInfo);
        return userInfo.getDptID();
    }
}
