package com.gzq.crius.bloomfilter;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @description: 配置类
 * @author guozhenquan
 * @date 2021/7/31 15:47
 * @version 1.0
 */
public class BloomFilterConfig {

    private List<Map<String, Object>> itemsMap;

    public List<BloomConfigItem> getItems() {
        List<BloomConfigItem> items = new ArrayList();
        itemsMap.forEach(e -> {
            BloomConfigItem bloomConfigItem = JSON.parseObject(JSON.toJSONString(e), BloomConfigItem.class);
            items.add(bloomConfigItem);
        });
        return items;
    }

    public List<Map<String, Object>> getItemsMap() {
        return itemsMap;
    }

    public void setItemsMap(List<Map<String, Object>> itemsMap) {
        this.itemsMap = itemsMap;
    }

    static class BloomConfigItem {

        private String name;
        private String key;
        private Long expectedInsertions;
        private Double falseProbability;

        public Long getExpectedInsertions() {
            return expectedInsertions;
        }

        public void setExpectedInsertions(Long expectedInsertions) {
            this.expectedInsertions = expectedInsertions;
        }

        public double getFalseProbability() {
            return falseProbability;
        }

        public void setFalseProbability(double falseProbability) {
            this.falseProbability = falseProbability;
        }

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
