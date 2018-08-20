import com.facedamon.orm.core.Query;
import com.facedamon.orm.handler.BeanListHandler;
import com.facedamon.orm.process.Under2CamelProcessor;
import com.facedamon.orm.util.DataSourceHolder;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
* @Description:
* @Author:         facedamon
* @CreateDate:     2018/7/26 15:21
* @UpdateUser:     facedamon
* @UpdateDate:     2018/7/26 15:21
* @UpdateRemark:   修改内容
* @Version:        1.0
*/
public class JunitTestCase {

    private Connection conn;

    @Before
    public void before(){
        this.conn = DataSourceHolder.getConnectionQuietly();
    }

    @Test
    public void test() throws SQLException {
        Query query = new Query();
        /*List<Map<String,Object>> list = query.query(this.conn,"select host,user from user",new MapListHandler(),null);

        if (null != list && !list.isEmpty()){
            for (Map<String,Object> item : list){
                for (Map.Entry<String,Object> map : item.entrySet()){
                    System.out.print(" "+map.getKey() + ": " + map.getValue());
                }
                System.out.println();
            }
        }*/
        List<Hello> beanList = query.query(this.conn,"select host,user from user",new BeanListHandler<Hello>(Hello.class),null);

        if (null != beanList && !beanList.isEmpty()){
            for (Hello item : beanList){
                System.out.print(item.getHost()+":"+item.getUser());
                System.out.println();
            }
        }
    }

    @Test
    public void strTest(){
        String str = "abcDrfGjo";
        String to = Under2CamelProcessor.camel2Under(str);
        System.out.println(to);
    }
}
