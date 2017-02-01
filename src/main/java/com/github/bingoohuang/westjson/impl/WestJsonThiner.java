package com.github.bingoohuang.westjson.impl;

import com.alibaba.fastjson.serializer.NameFilter;
import com.alibaba.fastjson.serializer.SerializeFilter;
import com.alibaba.fastjson.serializer.ValueFilter;
import com.github.bingoohuang.westjson.utils.BaseX;
import com.github.bingoohuang.westjson.utils.WestJsonUtils;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import lombok.Getter;
import lombok.val;

import java.util.Map;

import static com.alibaba.fastjson.JSON.toJSONString;
import static com.github.bingoohuang.westjson.utils.WestJsonUtils.bytesLen;

/**
 * @author bingoohuang [bingoohuang@gmail.com] Created on 2017/2/1.
 */
public class WestJsonThiner {
    private Map<String, String> innerKeyMapping = Maps.newHashMap();
    private NameFilter nameFilter = createNameFilter();

    private Multiset<String> valueBag = HashMultiset.create(); // 存放相同name-value的数量
    private Map<String, String> innerValueMapping = Maps.newHashMap();
    private ValueFilter firstValueFilter = createFirstValueFilter();
    private ValueFilter secondValueFilter = createSecondValueFilter();
    @Getter private Map<String, String> keyMapping;
    @Getter private Map<String, String> valueMapping;

    public ValueFilter createSecondValueFilter() {
        // 第一遍过滤器，如果启动了值映射分析，则还需要第二个过滤器，在第一次过滤器中进行值映射分析。
        return new ValueFilter() {
            @Override
            public Object process(Object source, String name, Object value) {
                if (!(value instanceof String)) return value;

                val key = (String) value;
                if (bytesLen(key) <= 3) return key;

                String valueCode = innerValueMapping.get(key);
                if (valueCode != null) return '@' + valueCode;

                if (valueBag.count(key) < 3) return value;

                valueCode = BaseX.base62(innerValueMapping.size());
                innerValueMapping.put(key, valueCode);
                return '@' + valueCode;
            }
        };
    }

    private ValueFilter createFirstValueFilter() {
        return new ValueFilter() {
            @Override
            public Object process(Object source, String name, Object value) {
                if (!(value instanceof String)) return value;

                String strValue = (String) value;
                if (bytesLen(strValue) <= 3) return value;

                String mappedValue = innerValueMapping.get(strValue);
                if (mappedValue != null) return mappedValue;

                valueBag.add(strValue);
                return value;
            }
        };
    }

    private NameFilter createNameFilter() {
        return new NameFilter() {
            @Override
            public String process(Object source, String name, Object value) {
                String mappedName = innerKeyMapping.get(name);
                if (mappedName != null) return mappedName;

                mappedName = BaseX.base62(innerKeyMapping.size());
                innerKeyMapping.put(name, mappedName);

                return mappedName;
            }
        };
    }

    public String thin(Object bean) {
        toJSONString(bean, new SerializeFilter[]{firstValueFilter});
        val ret = toJSONString(bean, new SerializeFilter[]{nameFilter, secondValueFilter});

        this.keyMapping = WestJsonUtils.invert(innerKeyMapping);
        this.valueMapping = WestJsonUtils.invert(innerValueMapping);

        return ret;
    }
}
