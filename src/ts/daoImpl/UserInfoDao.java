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
        UserInfo userInfo = super.get(id);;
        return userInfo;
    }
}
