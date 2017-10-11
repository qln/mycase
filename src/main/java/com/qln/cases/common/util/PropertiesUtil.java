package com.qln.cases.common.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

public class PropertiesUtil extends Properties {
    private static final Logger logger = Logger.getLogger(PropertiesUtil.class);
    private static final long serialVersionUID = 1L;

    public PropertiesUtil() {
        super();
    }

    /**
     * @param confType fastdfs,db,redis and so on.
     * @return
     */
    public static PropertiesUtil getByConfType(String confType) {
        PropertiesUtil appProperties = null;
        try {
            appProperties = new PropertiesUtil(PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties"));
            String active = appProperties.getProperty("spring.profiles.active");
            String path = PropertiesUtil.class.getClassLoader().getResource("").getPath();
            // if(!path.endsWith(File.separator))
            // {
            // path=path+File.separator;
            // }
            logger.info("============config file folder:【" + path + "】");
            Resource resource = null;
            if (StringUtils.isNotBlank(active)) {
                logger.info("============config profile:【" + active + "】");
                resource = new FileSystemResource(path + confType + "-" + active + ".properties");
                logger.info("============ load config file:【" + path + confType + "-" + active + ".properties" + "】");
                if (resource.exists()) {
                    appProperties = new PropertiesUtil(resource.getInputStream());
                } else {
                    resource = new ClassPathResource(confType + "-" + active + ".properties");
                    if (!resource.exists()) {
                        logger.info("类路径下没有对应的配置文件或者jar里没有默认的" + confType + "-" + active + ".properties配置");
                        logger.info("所以使用" + confType + "-" + active + ".properties配置");
                        resource = new FileSystemResource(path + "application-" + active + ".properties");
                    }

                    appProperties = new PropertiesUtil(resource.getInputStream());
                }

            } else {
                // 有confType配置
                resource = new FileSystemResource(path + confType + ".properties");
                if (resource.exists()) {
                    logger.info("所以使用" + confType + "-" + active + ".properties配置");
                    appProperties = new PropertiesUtil(resource.getInputStream());
                } else {
                    logger.info("使用jar里类路径里配置:" + confType + ".properties");
                    resource = new ClassPathResource(confType + ".properties");
                    appProperties = new PropertiesUtil(resource.getInputStream());
                }
            }
        } catch (Exception e) {
            logger.error("============config error:【" + confType + "】", e);
        }
        return appProperties;

    }

    public PropertiesUtil(String configFilePath) throws FileNotFoundException, IOException {
        load(new FileInputStream(configFilePath));
    }

    public PropertiesUtil(InputStream inputStream) throws FileNotFoundException, IOException {
        load(inputStream);
    }

    /**
     * @param defaults
     */
    public PropertiesUtil(Properties defaults) {
        super(defaults);
    }

    public String getProperty(String key, String defaultVal) {
        String oval = getProperty(key);
        if (oval != null) {
            return oval;
        } else {
            return defaultVal;
        }
    }

    public Integer getIntProperty(String key) {
        String oval = getProperty(key);
        if (oval != null) {
            return Integer.parseInt(oval.trim());
        } else {
            return null;
        }
    }

    public Long getLongProperty(String key) {
        try {
            String oval = getProperty(key);
            if (oval != null) {
                return Long.parseLong(oval.trim());
            } else {
                return new Long(0);
            }
        } catch (Exception e) {
            return new Long(0);
        }

    }

    public Long getLongProperty(String key, Long defaultVal) {
        String oval = getProperty(key);
        if (oval != null) {
            return Long.parseLong(oval.trim());
        } else {
            return defaultVal;
        }
    }

    public Integer getIntProperty(String key, Integer defaultVal) {
        String oval = getProperty(key);
        if (oval != null) {
            return Integer.parseInt(oval.trim());
        } else {
            return defaultVal;
        }
    }

    public Boolean getBooleanProperty(String key, Boolean defaultVal) {
        String oval = getProperty(key);
        try {
            if (oval != null) {
                return Boolean.parseBoolean(oval);
            } else {
                return defaultVal;
            }
        } catch (Exception e) {
            return defaultVal;
        }
    }
}
