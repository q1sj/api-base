package com.xsy.sys.enums;

import com.xsy.base.util.JsonUtils;

import java.util.LinkedHashMap;

/**
 * @author Q1sj
 * @date 2023.4.23 15:21
 */
public enum SysConfigValueTypeEnum {
    //
    STRING {
        @Override
        public boolean valid(String val) {
            return true;
        }
    },
    INT {
        @Override
        public boolean valid(String val) {
            try {
                Integer.parseInt(val);
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        }
    },
    LONG {
        @Override
        public boolean valid(String val) {
            try {
                Long.parseLong(val);
            } catch (NumberFormatException e) {
                return false;
            }
            return true;
        }
    },
    BOOLEAN {
        @Override
        public boolean valid(String val) {
            return "TRUE".equalsIgnoreCase(val) || "FALSE".equalsIgnoreCase(val);
        }
    },
    JSON {
        @Override
        public boolean valid(String val) {
            try {
                JsonUtils.parseObject(val, LinkedHashMap.class);
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    },
    JSON_ARRAY {
        @Override
        public boolean valid(String val) {
            try {
                JsonUtils.parseArray(val, LinkedHashMap.class);
            } catch (Exception e) {
                return false;
            }
            return true;
        }
    };

    public abstract boolean valid(String val);

}
