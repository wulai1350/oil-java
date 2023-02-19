/**
* 版权声明：厦门睿商网络科技有限公司 版权所有 违者必究
* 日    期：2020-02-12
*/
package com.rzico.core.service;

import com.rzico.base.BaseMapper;
import com.rzico.base.impl.BaseServiceImpl;
import com.rzico.core.entity.SysSequence;
import com.rzico.util.FreemarkerUtils;
import com.rzico.util.RedisHandler;
import freemarker.template.TemplateException;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.rzico.core.mapper.SysSequenceMapper;
import org.springframework.util.Assert;

import javax.annotation.Resource;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * <pre>
 *  生成唯一系号
 * </pre>
 *
 * @author 张森荣
 * @version 1.0
 */
@Service
public class SysSequenceService extends BaseServiceImpl<SysSequence, String> {

    @Autowired
    private SysSequenceMapper sysSequenceMapper;

    @Resource
    RedisHandler redisHandler;

    @Override
    public BaseMapper<SysSequence, String> getMapper() {
        return sysSequenceMapper;
    }

    //返回带日期的，用于单号
    public String generate(String type) {
        Assert.notNull(type);
        HiloOptimizer hiloOptimizer = new HiloOptimizer(type,"{.now?string('yyyyMMdd')}",100);
        return hiloOptimizer.generate();
    }

    //返回数值型序号,如商户号
    public Long generateNum(String type) {
        Assert.notNull(type);
        HiloOptimizer hiloOptimizer = new HiloOptimizer(type,"",100);
        return Long.parseLong(hiloOptimizer.generate());
    }

    //获取redis缓存号
    public Integer generateRedisNum(String type) {

        Assert.notNull(type);
        String lastValue = (String) redisHandler.get("redis-"+type);

        Integer num = 1;
        if (lastValue!=null) {
            num = Integer.parseInt(lastValue.substring(2));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd");
        String day = sdf.format(new Date());
        if (lastValue!=null && lastValue.substring(0,2).equals(day)) {
            num++;
        } else {
            num = 1;
        }

        lastValue = day.concat(String.valueOf(num));
        redisHandler.set("redis-"+type,lastValue);

        return num;
    }

    private long getLastValue(String type) {
        Map<String,Object> params = new HashMap<>();
        params.put("id",type);
        params.put("value",1);
        Integer updateRow = sysSequenceMapper.addValue(params);
        if (updateRow==0) {
            SysSequence sysSequence = new SysSequence();
            sysSequence.setId(type);
            sysSequence.setLastValue(1L);
            sysSequenceMapper.insert(sysSequence);
        }
        SysSequence sysSequence = sysSequenceMapper.selectByPrimaryKey(type);
        return sysSequence.getLastValue();
    }

    /**
     * 高低位算法
     */
    private class HiloOptimizer {

        private String type;

        private String prefix;

        private long maxLo;

        private long lo;

        private long hi;

        private long lastValue;

        public HiloOptimizer(String type, String prefix, int maxLo) {
            Integer lo = (Integer) redisHandler.get("sequence-"+type);
            if (lo!=null) {
                this.lo = lo;
            } else {
                this.lo = maxLo + 1;
            }
            this.maxLo = maxLo;
            this.type = type;
            this.prefix = prefix != null ? prefix.replace("{", "${") : "";
        }

        public String generate() {
            if (lo > maxLo) {
                lastValue = getLastValue(type);
                lo = lastValue == 0 ? 1 : 0;
                hi = lastValue * (maxLo + 1);
            }
            lo = redisHandler.increment("sequence-"+type,1);
            try {
                return FreemarkerUtils.process(prefix, null) + (hi + lo);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TemplateException e) {
                e.printStackTrace();
            }
            return String.valueOf(hi + lo);
        }
    }

}
