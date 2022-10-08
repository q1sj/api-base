# 框架介绍
用尽可能精简的依赖实现大部分项目所需的通用功能  
封装部分复杂的第三方依赖,开发时专注业务编写无需关心具体实现
## 运行环境
jdk1.8  
mysql5.7    
## 依赖版本
只能使用以下依赖(parent dependencyManagement中的依赖),不允许私自添加 修改
```text
spring-boot-starter-parent:2.3.3.RELEASE
spring-boot-starter-web
spring-boot-starter-test
spring-boot-starter-aop
spring-boot-starter-validation
jackson
lombok
commons-lang3

spring-boot-starter-data-elasticsearch:2.3.3.RELEASE(?)
shiro-core:1.9.0(?)
shiro-spring:1.9.0
mysql-connector-java:8.0.17
druid-spring-boot-starter:1.1.13
easyexcel:3.0.0-beta1
commons-io:2.5
joda-time:2.10.14
mybatis-plus-boot-starter:3.5.2
```
## common模块功能介绍
### 文件存储
com.xsy.file.service.FileRecordService  
```java
/**
* 保存文件
*
* @param data     文件内容
* @param fileName 文件名称
* @param source   数据来源
* @param userId   上传用户id
* @param ip       上传用户ip
* @param expireMs 过期毫秒值
* @return
*/
FileRecordEntity save(byte[] data, String fileName, String source, String userId, String ip, long expireMs) throws IOException;
/**
* 获取文件内容
* 
* @param path {@link FileRecordEntity#getPath()}
* @return
* @throws IOException
*/
byte[] getFileBytes(String path) throws IOException;

/**
* 删除文件
* @param path {@link FileRecordEntity#getPath()}
* @return
*/
boolean delete(String path);
```
com.xsy.file.controller.FileRecordController  
通用上传文件接口,建议根据业务重新编写限制文件大小,文件类型的上传接口
```java
/**
* 上传文件
*
* @param file
* @return
*/
@PostMapping("/upload")
public Result<FileRecordEntity> upload(MultipartFile file, HttpServletRequest request)
```
使用com.xsy.file.utils.FileUrlGenerateUtils生成图片接口相对路径,前端根据baseUrl+相对路径展示图片
```java
/**
* 查看图片
*
* @param response
* @param path {@link FileRecordEntity#getPath()}
*/
@GetMapping(IMG_MAPPING)
public void img(HttpServletResponse response, @RequestParam String path)
/**
* 下载文件
*
* @param response
* @param path {@link FileRecordEntity#getPath()}
*/
@GetMapping(DOWNLOAD_MAPPING)
public void download(HttpServletResponse response, @RequestParam String path)
```
### 日志
com.xsy.base.log.PostApiLogAop @PostMapping标注的方法都会记录操作日志  
com.xsy.base.log.IgnoreLog @IgnoreLog标注的方法忽略PostApiLogAop日志  
com.xsy.base.log.LogController 日志下载方 便生产环境排查问题  
lombok.extern.slf4j.Slf4j  
### 定时任务
使用spring自带定时任务  
@org.springframework.scheduling.annotation.Scheduled
### 参数管理
com.xsy.sys.service.SysConfigService
```java
/**
 * 新增或修改
 *
 * @param key
 * @param val
 * @param <T>
 */
<T> void put(BaseConfigKey<T> key, T val);
/**
 * 获取
 *
 * @param key
 * @param <T>
 * @return
 */
<T> T get(BaseConfigKey<T> key);
```
### 全局异常处理
com.xsy.base.exception.GlobalExceptionHandler  
捕获常见框架异常,业务异常捕获在common.handler包下创建
### 版本号
com.xsy.base.controller.AppInfoController  
/api/version获取应用版本号编译时间,用于检查新项目升级是否成功,老版本根据更新文档升级
### 用户认证授权
#### token校验配置
com.xsy.项目名.common.config.ShiroFilterMapConfig  
默认会添加filterMap.putIfAbsent("/**", "oauth2");
```java
@Component
public class ShiroFilterMapConfig extends BaseShiroFilterMapConfig {
    {
        // smart-doc
        filterMap.put("/doc/**", "anon");
        // 设备推送接口
        filterMap.put(ApiController.PUSH_POST_MAPPING, "anon");
    }
}
```
#### 权限校验
controller加上@org.apache.shiro.authz.annotation.RequiresPermissions  
value和菜单permissions字段一致
### 接口文档生成(?)
smart-doc-maven-plugin
TODO
## 工具类
不要创建重复的工具类 所有人使用同一工具类 保证统一逻辑  
### json
com.xsy.base.util.JsonUtils
### 参数校验
com.xsy.base.util.ValidatorUtils
### 字符串
com.xsy.base.util.StringUtils
### 断言
com.xsy.base.util.BizAssertUtils
### 日期
com.xsy.base.util.DateUtils
### 枚举
com.xsy.base.util.EnumUtils
### excel导出
com.xsy.base.util.ExcelUtils
### http
org.springframework.web.client.RestTemplate
### base64
com.xsy.base.util.Base64Utils
### 文件
com.xsy.base.util.FileUtils
## 快速开始
### git clone基础项目
TODO
### 基础数据库导入
TODO
### 创建业务表
### 代码生成
TODO
## 业务开发说明
### 项目结构说明
```text
com.xsy
    Application 启动类
com.xsy.项目名.common 通用业务
    annotation 注解
    config 配置类
    handler 自定义处理器,切面
    ... 其他包名和业务模块保持一致
com.xsy.项目名.业务模块名 业务模块
    controller
    service
        impl
    dao
    enums 枚举常量
    util 业务相关工具类
    pojo 实体类
        entity
        dto
        vo
    exception 业务异常
    task 定时任务
resource
    mapper
        业务模块名
            xxxDao.xml
```
### controller
接口返回类型必须为com.xsy.base.utils.Result 并指定具体泛型 禁止使用Map<String,Object>    
接口入参在pojo目录下编写DTO 禁止使用Map<String,Object>  
接口请求方式 查询GET RequestParam传参 增删改POST RequestBody传参(文件上传除外)
### 文件
所有存储文件统一调用com.xsy.file.service.FileRecordService.save  
原因:  
- 自行输出文件可能导致存在多个不同目录 不便统一管理  
- 自行输出文件若没有做好详细记录容易产生垃圾文件  
- 后期如需调整存储策略方便
### 日志
使用@lombok.extern.slf4j.Slf4j
生产环境禁止直接使用 System.out 或 System.err 输出日志或使用e.printStackTrace()打印异常堆栈  
### 日志等级
ERROR  
该级别的错误需要马上被处理  
WARN  
不应该出现但是不影响程序、当前请求正常运行的异常情况  
INFO  
- 重要数据的增删改
- 对外暴露接口的请求与响应
- 重要信息（或关键步骤）
- 调用其它系统接口的请求与响应
- 定时任务的执行情况(开始，结束，耗时等..)
- switch case语句块中的default
- if...else if...else中很少出现的else情况
- 等等...
DEBUG  
对系统每一步的运行状态进行精确的记录  
一般来说，在生产环境中，不会输出该级别的日志。
### 参数管理
- 需要动态调整的配置使用参数管理 不要写在yml中  
- 多个参数值中存在重复部分 应该抽取
 ```text
api1=http://192.168.1.1:8080/api/aaa
api2=http://192.168.1.1:8080/api/bbb
修改为:
baseUrl=http://192.168.1.1:8080/api
api1=/aaa
api2=/bbb
 ```
- 类内共享的key直接在类内部 private static final 定义  
- 业务模块内共享的key在业务模块名.enums包下定义  
- 通用的key在common.enums包下定义  
```java
public class EventConfigKeyConstant {
    /**
     * 正常事件是否保存图片
     * 测试阶段需要保存
     */
    public static final BooleanConfigKey NORMAL_EVENT_SAVE_PIC_KEY = new BooleanConfigKey("file.normalEvent.savePic", false);
    /**
     * 事件图片保存天数
     */
    public static  final IntConfigKey EVENT_PIC_SAVE_DAY_KEY = new IntConfigKey("file.event.picSaveDay", 30);
}
```
### 版本号
版本号命名方式:主版本号.次版本号-build-yyyyMMddHHmmss  
例如1.0-build-202209220714
1. 主版本号：产品方向改变，或者大规模 API 不兼容，或者架构不兼容升级。
2. 次版本号：保持相对兼容性，增加主要功能特性，影响范围极小的 API 不兼容修改。
3. 修订号省略使用编译日期代替 保持完全兼容性，修复 BUG、新增次要功能特性等。编译日期会自动生成

主版本,次版本打包发布前修改版本号pom.xml中的project version提交git 做好文档记录  
文档位置 项目根目录/doc/update.md  
文档包含以下内容
```text
版本号
发布时间
新增功能
增强功能
bug修复
开发人员
服务器部署环境有调整的 记录sh脚本
数据库有修改的 记录sql语句
```
## 部署
TODO
