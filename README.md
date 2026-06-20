# 图书管理系统设计文档

## 一、项目背景与目标

本项目为一个基于Spring Boot、Spring Data JPA、Thymeleaf和MySQL开发的Web图书管理系统，旨在实现图书的增删改查、可视化管理以及后续扩展借阅、用户管理等功能。系统适合用于中小型图书馆或高校自助图书管理。

---

## 二、系统架构

### 1. 技术选型

- **后端框架**：Spring Boot 2.x
- **ORM**：Spring Data JPA
- **数据库**：MySQL 8.x
- **前端模板**：Thymeleaf
- **构建工具**：Maven
- **开发语言**：Java 8+

### 2. 系统结构

整体采用MVC架构，分为三层：

- **Controller层**：负责处理HTTP请求，调用业务逻辑，返回视图或数据。
- **Service层**：封装业务逻辑，协调数据操作。
- **Repository层**：数据访问层，使用JPA进行ORM映射。

---

## 三、功能模块设计

### 1. 图书管理

- 图书列表展示
- 图书添加
- 图书信息编辑
- 图书删除（有借阅记录时禁删并提示）
- 图书信息表单美化与校验

### 2. 用户与权限管理（预留，可后续扩展）

- 登录认证
- 权限控制（如管理员/普通用户）

### 3. 主页展示

- 进入系统首页，展示欢迎信息和导航按钮

---

## 四、数据库设计

### 1. book 表

| 字段名     | 类型         | 说明     |
|------------|--------------|----------|
| id         | BIGINT       | 主键，自增 |
| title      | VARCHAR(255) | 书名     |
| author     | VARCHAR(255) | 作者     |
| publisher  | VARCHAR(255) | 出版社   |
| stock      | INT          | 库存数量 |

（可根据需要扩展如ISBN、价格、分类等字段）

### 2. 其它表

如有借阅、用户等功能，可增加 `user`、`borrow_record` 等表。

---

## 五、主要代码结构

```
com.example.library
├── controller
│   └── BookController.java
├── entity
│   └── Book.java
├── repository
│   └── BookRepository.java
├── service
│   └── BookService.java
├── LibraryApplication.java
└── resources
    ├── templates
    │   ├── index.html
    │   ├── book_list.html
    │   └── book_form.html
    └── application.yml
```

---

## 六、核心业务流程

### 1. 图书列表

- 访问 `/books`，Controller通过Service查询所有Book，返回 `book_list.html`，页面展示所有图书及操作按钮。

### 2. 添加图书

- 点击“添加图书”，跳转 `/books/add`，显示空白表单。
- 填写后POST提交到 `/books/add`，保存数据，重定向回列表。

### 3. 编辑图书

- 列表页点击“编辑”，跳转 `/books/edit/{id}`，回显原数据。
- 修改后POST提交，更新数据，重定向回列表。

### 4. 删除图书

- 列表页“删除”按钮请求 `/books/delete/{id}`，调用Service删除。
- 若有关联借阅记录则捕获异常并提示，列表页显示错误信息。

---

## 七、前端界面设计

- 采用简洁现代的毛玻璃卡片风格
- 响应式布局，支持PC和移动端浏览
- 列表、表单、操作按钮风格统一
- 重要操作有确认提示和友好报错

---

## 八、配置说明

**application.yml** 主要配置了：

- 端口（8080）
- 数据库连接（MySQL，用户名密码等）
- JPA自动建表与SQL日志
- Thymeleaf模板缓存关闭

---

## 九、后续扩展建议

- 增加用户与权限管理
- 增加图书分类、封面、ISBN等字段
- 增加借阅、归还、借阅历史等功能
- RESTful API支持与前后端分离
- 单元测试与集成测试

---

## 十、部署与运行

1. 安装并启动MySQL，创建数据库与表并填充测试数据
2. 修改application.yml为实际数据库配置
3. Maven打包或直接IDE运行
4. 浏览器打开 [http://localhost:8080/](http://localhost:8080/) 访问系统

---

## 十一、常见问题FAQ

- 若报数据库连接错误，请检查MySQL服务是否启动、用户名密码是否正确、数据库是否存在
- 若报模板解析错误，请检查模板字段与实体类字段是否一致
- 若访问404或Whitelabel Error Page，请检查路由和Controller映射

---

