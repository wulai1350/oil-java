/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-01-20
*/
package com.rzico.core.entity;

import lombok.Data;

import java.util.Collections;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

/**
 * <pre>
 *   区域管理数据库对象
 * </pre>
 * @author Rzico Boot
 * @version 1.0
 */
@Data
public class SysArea{
    //区域编码
    @Id
    protected String id;
    //区域名称
    protected String name;
    //行政编码
    protected String areaCode;
    //区域全名
    protected String fullName;
    //父级编号
    protected String parentId;
    //排序
    protected Integer dataSort;
    //关系树
    protected String treePath;
    //创建者
    protected String createBy;
    //创建时间
    protected java.util.Date createDate;
    //更新者
    protected String modifyBy;
    //更新时间
    protected java.util.Date modifyDate;

    //银联区域编码
    protected String unionPayCode;

    protected List<SysArea> childrens = Collections.emptyList();
}
