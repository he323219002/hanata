本代码部署地址：http://www.hanata.top

### 架构图

![](https://img2020.cnblogs.com/blog/1496566/202007/1496566-20200713233251638-1103614005.jpg)

### 项目效果图

#### 主页

![主页](https://img2020.cnblogs.com/blog/1496566/202007/1496566-20200713225658718-658017037.png)

#### 列表页

![](https://img2020.cnblogs.com/blog/1496566/202007/1496566-20200713225939668-1346037368.png)

#### 评论详情

![](https://img2020.cnblogs.com/blog/1496566/202007/1496566-20200713230421141-870479885.png)

#### 后台管理

![](https://img2020.cnblogs.com/blog/1496566/202007/1496566-20200713230519034-179863247.png)

![](https://img2020.cnblogs.com/blog/1496566/202007/1496566-20200713230659962-153321172.png)

## 技术栈

#### 后端

1. SpringBoot
2. Spring + SpringMVC + Mybatis 
3. JWT 
4. ElasticSearch
5. Docker

#### 前端

1. Vue + Vue Router + Vuex
2. ElementUI
3. mavon-editor + tinymce
4. axios

### 项目运行

1. 项目依赖，运行之前需要依赖以下环境，也可以用docker，自定义好映射端口即可
   - mysql
   - nginx (解决跨域，部署多个vue项目)
   - redis
   - elasticSearch 7.7.0
2. 配置好yaml文件，代码中有个yaml.demo可供参考
3. spring项目直接运行
4. vue项目npm run serve 启动
5. 前端依赖 tinymce， maven-editor

### 感谢

本项目感谢陈舰长[Mirai-Kuriyama](https://github.com/Mirai-Kuriyama) 和卢大佬提供的前端技术支持。

本人也非技术大佬，此项目是工作之余自己倒腾的，如有不足还请贴代码指正。一键部署现在还在研究中，某些部署的依赖可能无法细说，项目价值不大，如果大家能找到自己想要实现的功能，我会感到非常开心。继续学习，继续前进。

如需转载请附上来源，谢谢支持。