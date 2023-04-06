# OA_Project
1.本项目所使用的技术：
| 基础框架：SpringBoot                                         |
| ------------------------------------------------------------ |
| 数据缓存：Redis                                              |
| 数据库：MyBatisPlus + MySQL                                  |
| 权限控制：SpringSecurity                                     |
| 工作流引擎：Activiti7                                        |
| 前端技术：vue-admin-template + Node.js + Npm + Vue + ElementUI + Axios |
| 微信公众号：公众号菜单 + 微信授权登录 + 消息推送             |
| 内网穿透：ngrok             |

2.oa办公系统----后端，端口号是8800

项目分成管理端和员工端。

管理端有以下几个模块：

      2.1、系统管理模块：
      （1）用户管理、角色管理、菜单管理
      （2）表之间关系

      2.2、审批模块
      （1）审批类型管理
      （2）审批模板管理
      （3）审批列表

      2.3、公众号菜单管理模块

员工端主要实现微信授权登录、提交oa审批、消息推送等功能。

3.oa前端----实现与后端的接口以及管理员和员工的页面渲染。端口号是9090。
